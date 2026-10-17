package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * MC-261789: a falling block that lands fast below Y=0 can end up a hair under the top of the block it landed on, at
 * y = -7.000000000000001 instead of -7. Its block position is then the block it is standing on, so it can't be placed and
 * breaks into an item. When it has landed that close under a whole number, the block above is the one it is in.
 */
@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;blockPosition()Lnet/minecraft/core/BlockPos;", ordinal = 0))
	private BlockPos longstandingfixes$landingPos(BlockPos pos) {
		FallingBlockEntity self = (FallingBlockEntity) (Object) this;
		if (!LongStandingFixes.fallingBlockPrecision || !self.onGround() || LongStandingFixes.VANILLA_ENTITIES.contains(self.getUUID()))
			return pos;
		double y = self.getY(), above = Math.ceil(y);
		return above != y && above - y < 1.0E-7 ? BlockPos.containing(self.getX(), above, self.getZ()) : pos;
	}
}
