package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.brigadier.ParseResults;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * MC-99601: remembers which command a player or the server console is running, so a player-name lookup inside it can run
 * the command again once the name is known (see {@link GameProfileArgumentMixin}).
 */
@Mixin(Commands.class)
public abstract class CommandsMixin {
	@WrapMethod(method = "performCommand")
	private void longstandingfixes$rememberCommand(ParseResults<CommandSourceStack> parse, String command, Operation<Void> original) {
		CommandSourceStack source = parse.getContext().getSource();
		boolean typed = source.isPlayer() || ((CommandSourceStackAccessor) source).longstandingfixes$source() instanceof MinecraftServer;
		LongStandingFixes.RunningCommand previous = LongStandingFixes.RUNNING_COMMAND.get();
		LongStandingFixes.RUNNING_COMMAND.set(typed ? new LongStandingFixes.RunningCommand(source, command) : null);
		try {
			original.call(parse, command);
		} finally {
			LongStandingFixes.RUNNING_COMMAND.set(previous);
		}
	}
}
