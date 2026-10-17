package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-135198: a dispenser with flint and steel can't light a nether portal through an air block that doesn't touch the
 * frame, for example the middle of a 5x5 portal. The fire is only allowed there if a frame block is right next to it.
 * Now an empty portal frame around the block is enough, in either direction.
 */
@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {
	@Inject(method = "isPortal", at = @At("RETURN"), cancellable = true)
	private static void long_standing_fixes$portalWithoutTouchingFrame(Level level, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() || !LongStandingFixes.dispenserLightsPortals || LongStandingFixes.VANILLA_BLOCKS.contains(pos))
			return;
		if (level.dimension() != Level.OVERWORLD && level.dimension() != Level.NETHER)
			return;
		if (PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X).isPresent() || PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.Z).isPresent())
			cir.setReturnValue(true);
	}
}
