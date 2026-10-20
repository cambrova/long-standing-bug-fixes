package io.github.traelmyx.longstandingfixes.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Who is behind a command: a player, the server console, a command block (MC-99601). */
@Mixin(CommandSourceStack.class)
public interface CommandSourceStackAccessor {
	@Accessor("source")
	CommandSource longstandingfixes$source();
}
