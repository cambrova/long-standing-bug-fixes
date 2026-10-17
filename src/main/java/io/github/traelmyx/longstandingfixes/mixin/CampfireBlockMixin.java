package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.spongepowered.asm.mixin.Mixin;

/** MC-166272: a comparator reading a campfire counts the food on it, like any other container. */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BaseEntityBlock {
	// extending the real superclass lets the remapper see these two override vanilla methods and rename them in production
	protected CampfireBlockMixin(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return LongStandingFixes.campfireComparator;
	}

	@Override
	//? if >=1.21.11 {
	/*protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, net.minecraft.core.Direction direction) {
	*///?} else
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (!LongStandingFixes.campfireComparator || LongStandingFixes.VANILLA_BLOCKS.contains(pos) || !(level.getBlockEntity(pos) instanceof CampfireBlockEntity campfire))
			return 0;
		int filled = 0;
		for (ItemStack stack : campfire.getItems()) {
			if (!stack.isEmpty()) filled++;
		}
		return LongStandingFixes.campfireSignal(filled);
	}
}
