package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >=1.21.11 {
/*import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
*///?} else
import net.minecraft.world.level.LevelAccessor;

/**
 * MC-213961: the stems of a big dripleaf check their support a tick later, so you can swap a stem for another block without
 * the plant noticing. The leaf on top checks straight away, so swapping the stem right under it always breaks the leaf.
 * The leaf now waits a tick too, like its stems.
 */
@Mixin(BigDripleafBlock.class)
public abstract class BigDripleafBlockMixin {
	//? if >=1.21.11 {
	/*@Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$checkNextTick(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
		BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
	*///?} else {
	@Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$checkNextTick(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos,
		BlockPos neighbourPos, CallbackInfoReturnable<BlockState> cir) {
		LevelAccessor ticks = level;
	//?}
		if (!LongStandingFixes.dripleafWaitsATick || direction != Direction.DOWN || state.canSurvive(level, pos) || LongStandingFixes.VANILLA_BLOCKS.contains(pos))
			return;
		ticks.scheduleTick(pos, (BigDripleafBlock) (Object) this, 1);
		if (state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED))
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		cir.setReturnValue(state);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$breakIfUnsupported(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (LongStandingFixes.dripleafWaitsATick && !state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
			ci.cancel();
		}
	}
}
