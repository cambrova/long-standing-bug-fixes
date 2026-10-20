package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;
import java.util.Optional;
//? if >=1.21.11 {
/*import net.minecraft.server.players.UserNameToIdResolver;
*///?} else
import net.minecraft.server.players.GameProfileCache;

/**
 * MC-99601: {@code /whitelist add}, {@code /op}, {@code /ban} and the rest look up a player name the server hasn't seen
 * with Mojang, on the main thread, so the whole server stands still until the answer comes (often seconds). When a player or
 * the console types such a command, the lookup now runs in the background and the command runs again by itself once the
 * name is known. Command blocks, functions and RCON keep the old behaviour, since they expect their result straight away.
 */
@Mixin(GameProfileArgument.class)
public abstract class GameProfileArgumentMixin {
	@Unique
	private static final DynamicCommandExceptionType LOOKING_UP = new DynamicCommandExceptionType(
		name -> Component.literal("Looking up " + name + " with Mojang; the command finishes on its own in a moment."));

	//? if >=1.21.11 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/UserNameToIdResolver;get(Ljava/lang/String;)Ljava/util/Optional;"))
	private static Optional<?> longstandingfixes$lookUpInBackground(UserNameToIdResolver cache, String name, Operation<Optional<?>> original) throws CommandSyntaxException {
	*///?} else {
	@WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Ljava/util/Optional;"))
	private static Optional<?> longstandingfixes$lookUpInBackground(GameProfileCache cache, String name, Operation<Optional<?>> original) throws CommandSyntaxException {
	//?}
		LongStandingFixes.RunningCommand running = LongStandingFixes.RUNNING_COMMAND.get();
		String key = name.toLowerCase(Locale.ROOT);
		if (!LongStandingFixes.namesLookedUpInBackground || running == null || !StringUtil.isValidPlayerName(name)
			|| LongStandingFixes.VANILLA_NAMES.contains(key) || !(cache instanceof LongStandingFixes.ProfileCachePeek peek) || peek.longstandingfixes$isCached(key))
			return original.call(cache, name);
		// the background lookup came back empty: let the command fail the usual way
		if (LongStandingFixes.FAILED_LOOKUPS.remove(key)) return Optional.empty();
		MinecraftServer server = running.source().getServer();
		LongStandingFixes.LOOKUPS.execute(() -> {
			boolean found;
			try {
				found = cache.get(name).isPresent();
			} catch (RuntimeException e) {
				LongStandingFixes.LOGGER.warn("Could not look up player {}", name, e);
				found = false;
			}
			boolean known = found;
			server.execute(() -> {
				if (!known) LongStandingFixes.FAILED_LOOKUPS.add(key);
				server.getCommands().performPrefixedCommand(running.source(), running.command());
			});
		});
		throw LOOKING_UP.create(name);
	}
}
