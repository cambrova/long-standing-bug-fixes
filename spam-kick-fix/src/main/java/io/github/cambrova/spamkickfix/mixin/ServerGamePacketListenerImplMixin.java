package io.github.cambrova.spamkickfix.mixin;

import io.github.cambrova.spamkickfix.SpamKickFix;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <26 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
//?}
//? if >=1.21.11
/*import net.minecraft.util.TickThrottler;*/

/**
 * The chat and command spam limits cool down by one step per server tick. On a server running below 20 ticks a second
 * they cool down slower in real time, so normal chatting gets players kicked "for spamming" (MC-276819). They now also
 * cool down for the real time that passed. On 1.21.1 and 1.21.11 commands also get their own limit, as in 26.2, and both
 * limits come from the config.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin implements SpamKickFix.SpamCounters {
	@Shadow
	public ServerPlayer player;

	//? if >=26 {
	/*@Shadow
	@Final
	private TickThrottler chatSpamThrottler;
	@Shadow
	@Final
	private TickThrottler commandSpamThrottler;
	@Shadow
	@Final
	private TickThrottler dropSpamThrottler;
	*///?} else if >=1.21.11 {
	/*@Shadow
	@Final
	private TickThrottler chatSpamThrottler;
	@Shadow
	@Final
	private TickThrottler dropSpamThrottler;
	*///?} else {
	@Shadow
	private int chatSpamTickCount;
	@Shadow
	private int dropSpamTickCount;
	//?}

	@Unique
	private static final long spamkickfix$TICK = 50_000_000L;
	@Unique
	private long spamkickfix$lastTick;
	@Unique
	private long spamkickfix$behind;
	/** Commands' own counter on 1.21.1 / 1.21.11 (26.2 has one built in). */
	@Unique
	private int spamkickfix$commandSpam;
	@Unique
	private boolean spamkickfix$inChat;

	@Inject(method = "tick", at = @At("TAIL"))
	private void spamkickfix$coolDown(CallbackInfo ci) {
		if (this.spamkickfix$commandSpam > 0) this.spamkickfix$commandSpam--;
		long now = SpamKickFix.CLOCK.getAsLong();
		if (this.spamkickfix$lastTick != 0 && SpamKickFix.realTimeCooldown && !SpamKickFix.VANILLA_OBJECTS.contains(this) && !SpamKickFix.otherModCools()) {
			this.spamkickfix$behind = Math.max(0, this.spamkickfix$behind + now - this.spamkickfix$lastTick - spamkickfix$TICK);
			int missed = (int) Math.min(200, this.spamkickfix$behind / spamkickfix$TICK);
			this.spamkickfix$behind -= missed * spamkickfix$TICK;
			if (missed > 0) this.spamkickfix$coolDown(missed);
		}
		this.spamkickfix$lastTick = now;
	}

	@Unique
	private void spamkickfix$coolDown(int steps) {
		this.spamkickfix$commandSpam = Math.max(0, this.spamkickfix$commandSpam - steps);
		//? if >=1.21.11 {
		/*for (int i = 0; i < steps; i++) {
			this.chatSpamThrottler.tick();
			this.dropSpamThrottler.tick();
			//? if >=26
			this.commandSpamThrottler.tick();
		}
		*///?} else {
		this.chatSpamTickCount = Math.max(0, this.chatSpamTickCount - steps);
		this.dropSpamTickCount = Math.max(0, this.dropSpamTickCount - steps);
		//?}
	}

	//? if <26 {
	@Inject(method = "broadcastChatMessage", at = @At("HEAD"))
	private void spamkickfix$enterChat(CallbackInfo ci) {
		this.spamkickfix$inChat = true;
	}

	@Inject(method = "broadcastChatMessage", at = @At("RETURN"))
	private void spamkickfix$leaveChat(CallbackInfo ci) {
		this.spamkickfix$inChat = false;
	}

	// chat messages and commands both end in detectRateSpam; commands get their own counter and both get the configured limit
	@WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;detectRateSpam()V"))
	private void spamkickfix$ownLimits(ServerGamePacketListenerImpl self, Operation<Void> original) {
		if (SpamKickFix.VANILLA_OBJECTS.contains(this)) {
			original.call(self);
			return;
		}
		int count, limit;
		if (this.spamkickfix$inChat) {
			count = this.spamkickfix$chatSpam() + 20;
			this.spamkickfix$setChatSpam(count);
			limit = SpamKickFix.chatLimitSeconds;
		} else {
			count = this.spamkickfix$commandSpam += 20;
			limit = SpamKickFix.commandLimitSeconds;
		}
		//? if >=1.21.11 {
		/*boolean over = count >= 20 * limit;
		var who = this.player.nameAndId();
		*///?} else {
		boolean over = count > 20 * limit;
		var who = this.player.getGameProfile();
		//?}
		var server = this.player.level().getServer();
		if (limit > 0 && over && !server.getPlayerList().isOp(who) && !server.isSingleplayerOwner(who))
			self.disconnect(Component.translatable("disconnect.spam"));
	}
	//?}

	@Override
	public int spamkickfix$chatSpam() {
		//? if >=1.21.11 {
		/*return ((TickThrottlerAccessor) this.chatSpamThrottler).spamkickfix$count();
		*///?} else
		return this.chatSpamTickCount;
	}

	@Override
	public void spamkickfix$setChatSpam(int count) {
		//? if >=1.21.11 {
		/*((TickThrottlerAccessor) this.chatSpamThrottler).spamkickfix$setCount(count);
		*///?} else
		this.chatSpamTickCount = count;
	}

	@Override
	public int spamkickfix$commandSpam() {
		//? if >=26 {
		/*return ((TickThrottlerAccessor) this.commandSpamThrottler).spamkickfix$count();
		*///?} else
		return this.spamkickfix$commandSpam;
	}
}
