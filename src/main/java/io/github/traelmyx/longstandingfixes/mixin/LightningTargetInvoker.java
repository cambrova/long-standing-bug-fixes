package io.github.traelmyx.longstandingfixes.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Lets the game tests ask where lightning would strike (MC-262422). */
@Mixin(ServerLevel.class)
public interface LightningTargetInvoker {
	@Invoker("findLightningTargetAround")
	BlockPos longstandingfixes$findLightningTargetAround(BlockPos pos);
}
