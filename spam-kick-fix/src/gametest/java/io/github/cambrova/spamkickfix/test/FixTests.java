package io.github.cambrova.spamkickfix.test;

import io.github.cambrova.spamkickfix.SpamKickFix;
import net.minecraft.gametest.framework.GameTestHelper;

/** Game tests for Spam Kick Fix. Each check has a vanilla twin that shows the problem still happens without the mod. */
public final class FixTests {
	private FixTests() {
	}

	private static final java.util.Map<Object, io.netty.channel.embedded.EmbeddedChannel> CHANNELS = java.util.Collections.synchronizedMap(new java.util.IdentityHashMap<>());

	/** A plain, non-op server player in the player list. */
	private static net.minecraft.server.level.ServerPlayer realPlayer(GameTestHelper helper, String name) {
		var server = helper.getLevel().getServer();
		var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), name), false);
		var player = new net.minecraft.server.level.ServerPlayer(server, helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
		var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
		CHANNELS.put(player, new io.netty.channel.embedded.EmbeddedChannel(connection));
		server.getPlayerList().placeNewPlayer(connection, player, cookie);
		//? if >=1.21.11 {
		/*player.connection.handleAcceptPlayerLoad(new net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket());
		*///?}
		return player;
	}

	private static void leave(GameTestHelper helper, net.minecraft.server.level.ServerPlayer player) {
		SpamKickFix.VANILLA_OBJECTS.remove(player.connection);
		helper.getLevel().getServer().getPlayerList().remove(player);
		CHANNELS.remove(player);
	}

	private static boolean kicked(net.minecraft.server.level.ServerPlayer player) {
		var channel = CHANNELS.get(player);
		channel.runPendingTasks();
		return !channel.isOpen();
	}

	/** Types a command the way a client does. */
	private static void command(net.minecraft.server.level.ServerPlayer player, String command) {
		player.connection.handleChatCommand(new net.minecraft.network.protocol.game.ServerboundChatCommandPacket(command));
	}

	/**
	 * The server lags: two real seconds pass between two of its ticks. A player who had chatted the counter up to 190 (just
	 * under the kick at 200) should cool down by the 40 steps of those two seconds, not by 1.
	 */
	private static void realTime(GameTestHelper helper, boolean fixed) {
		var player = realPlayer(helper, fixed ? "test-chatter" : "test-chatter-vanilla");
		if (!fixed) SpamKickFix.VANILLA_OBJECTS.add(player.connection);
		var counters = (SpamKickFix.SpamCounters) player.connection;
		long[] now = {1_000_000_000L};
		var clock = SpamKickFix.CLOCK;
		SpamKickFix.CLOCK = () -> now[0];
		int left;
		try {
			player.connection.tick();
			counters.spamkickfix$setChatSpam(190);
			now[0] += 2_000_000_000L;
			player.connection.tick();
			left = counters.spamkickfix$chatSpam();
		} finally {
			SpamKickFix.CLOCK = clock;
		}
		leave(helper, player);
		System.out.printf("[spam_kick_fix] real-time %s: chat counter 190 -> %d over one 2-second tick%n", fixed ? "fixed" : "vanilla", left);
		if (fixed) helper.assertTrue(left == 150, "the chat counter only cooled down to " + left + ", expected 150");
		else helper.assertTrue(left == 189, "vanilla cooled down to " + left + ", expected 189");
		helper.succeed();
	}

	/** On a lagging server the limits cool down by real time. */
	public static void realTimeCooldown(GameTestHelper helper) {
		realTime(helper, true);
	}

	/** Without the mod they cool down by one step per tick, however long the tick took. */
	public static void realTimeCooldownVanilla(GameTestHelper helper) {
		realTime(helper, false);
	}

	/** A player who has just chatted 8 messages runs 8 commands. */
	private static void ownLimit(GameTestHelper helper, boolean fixed) {
		//? if <26 {
		var player = realPlayer(helper, fixed ? "test-commander" : "test-commander-vanilla");
		if (!fixed) SpamKickFix.VANILLA_OBJECTS.add(player.connection);
		((SpamKickFix.SpamCounters) player.connection).spamkickfix$setChatSpam(160);
		int sent = 0;
		while (sent < 8 && !kicked(player)) {
			command(player, "help");
			sent++;
		}
		boolean out = kicked(player);
		leave(helper, player);
		System.out.printf("[spam_kick_fix] commands-own-limit %s: after 8 chat messages, kicked %s after %d commands%n", fixed ? "fixed" : "vanilla", out, sent);
		if (fixed) helper.assertTrue(!out, "the player was kicked after " + sent + " commands; commands should have their own limit");
		else helper.assertTrue(out, "vanilla did not kick; commands and chat should share one limit");
		helper.succeed();
		//?} else {
		/*// 26.2 counts commands apart from chat itself
		helper.succeed();
		*///?}
	}

	/** Commands have their own limit, so chatting doesn't use up the allowance for commands. */
	public static void commandsOwnLimit(GameTestHelper helper) {
		ownLimit(helper, true);
	}

	/** In vanilla 1.21.1 / 1.21.11 they share one. */
	public static void commandsOwnLimitVanilla(GameTestHelper helper) {
		ownLimit(helper, false);
	}

	/** With the command limit set to 0, a player runs 30 commands in a row. */
	private static void limitOff(GameTestHelper helper, boolean fixed) {
		//? if <26 {
		var player = realPlayer(helper, fixed ? "test-builder" : "test-builder-vanilla");
		if (!fixed) SpamKickFix.VANILLA_OBJECTS.add(player.connection);
		int limit = SpamKickFix.commandLimitSeconds;
		SpamKickFix.commandLimitSeconds = 0;
		int sent = 0;
		try {
			while (sent < 30 && !kicked(player)) {
				command(player, "help");
				sent++;
			}
		} finally {
			SpamKickFix.commandLimitSeconds = limit;
		}
		boolean out = kicked(player);
		leave(helper, player);
		System.out.printf("[spam_kick_fix] command-limit-off %s: kicked %s after %d commands%n", fixed ? "fixed" : "vanilla", out, sent);
		if (fixed) helper.assertTrue(!out && sent == 30, "the player was kicked after " + sent + " commands with the limit off");
		else helper.assertTrue(out, "vanilla did not kick after " + sent + " commands");
		helper.succeed();
		//?} else {
		/*// 26.2 takes 0 in server.properties for this
		helper.succeed();
		*///?}
	}

	/** A limit of 0 never kicks. */
	public static void commandLimitOff(GameTestHelper helper) {
		limitOff(helper, true);
	}

	/** Vanilla kicks after 10 or 11 quick commands. */
	public static void commandLimitOffVanilla(GameTestHelper helper) {
		limitOff(helper, false);
	}
}
