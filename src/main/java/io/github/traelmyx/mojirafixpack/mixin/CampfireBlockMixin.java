package io.github.traelmyx.mojirafixpack.mixin;

import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/** MC-166272: a comparator reading a campfire counts the food on it, like any other container. */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return MojiraFixPack.campfireComparator;
	}

	//? if >=1.21.11 {
	/*protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, net.minecraft.core.Direction direction) {
	*///?} else
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (!MojiraFixPack.campfireComparator || MojiraFixPack.VANILLA_BLOCKS.contains(pos) || !(level.getBlockEntity(pos) instanceof CampfireBlockEntity campfire))
			return 0;
		int filled = 0;
		for (ItemStack stack : campfire.getItems()) {
			if (!stack.isEmpty()) filled++;
		}
		return MojiraFixPack.campfireSignal(filled);
	}
}
