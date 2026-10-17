package io.github.traelmyx.mojirafixpack.test;

import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
*///?} else {
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
//?}

/** Each fix is tested twice: with the fix, and with the same setup kept on vanilla behaviour, where the bug must show. */
public final class FixTests {
	private FixTests() {
	}

	private static final BlockPos CAMPFIRE = new BlockPos(1, 1, 1), COMPARATOR = new BlockPos(1, 1, 2);

	private static void campfire(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 3; x++) for (int z = 0; z < 4; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		helper.setBlock(CAMPFIRE, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));
		if (!fixed) MojiraFixPack.VANILLA_BLOCKS.add(helper.absolutePos(CAMPFIRE));
		// facing north, the comparator reads the campfire behind it
		helper.setBlock(COMPARATOR, Blocks.COMPARATOR.defaultBlockState().setValue(ComparatorBlock.FACING, Direction.NORTH));
		CampfireBlockEntity campfire = (CampfireBlockEntity) helper.getBlockEntity(CAMPFIRE);
		campfire.getItems().set(0, new ItemStack(Items.BEEF));
		campfire.setChanged();
		helper.runAfterDelay(6, () -> {
			int one = ((ComparatorBlockEntity) helper.getBlockEntity(COMPARATOR)).getOutputSignal();
			for (int i = 1; i < 4; i++) campfire.getItems().set(i, new ItemStack(Items.BEEF));
			campfire.setChanged();
			helper.runAfterDelay(6, () -> {
				int four = ((ComparatorBlockEntity) helper.getBlockEntity(COMPARATOR)).getOutputSignal();
				MojiraFixPack.VANILLA_BLOCKS.remove(helper.absolutePos(CAMPFIRE));
				if (fixed) helper.assertTrue(one == 4 && four == 15, "comparator gave " + one + " for one item and " + four + " for four");
				else helper.assertTrue(one == 0 && four == 0, "vanilla comparator gave " + one + " and " + four + "; is MC-166272 fixed?");
				helper.succeed();
			});
		});
	}

	/** MC-166272: a comparator reads 4 for one item on a campfire and 15 for four. */
	public static void campfireComparator(GameTestHelper helper) {
		campfire(helper, true);
	}

	/** Without the fix the comparator stays at 0. */
	public static void campfireComparatorVanilla(GameTestHelper helper) {
		campfire(helper, false);
	}

	private static void poppy(GameTestHelper helper, boolean fixed) {
		// a walled 7x7 pen so the baby stays near the golem
		for (int x = 0; x < 7; x++) for (int z = 0; z < 7; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			if (x == 0 || z == 0 || x == 6 || z == 6) {
				helper.setBlock(new BlockPos(x, 1, z), Blocks.GLASS);
				helper.setBlock(new BlockPos(x, 2, z), Blocks.GLASS);
			}
		}
		//? if >=26 {
		/*IronGolem golem = helper.spawn(net.minecraft.world.entity.EntityTypes.IRON_GOLEM, new BlockPos(3, 1, 3));
		Villager baby = helper.spawn(net.minecraft.world.entity.EntityTypes.VILLAGER, new BlockPos(5, 1, 5));
		*///?} else {
		IronGolem golem = helper.spawn(EntityType.IRON_GOLEM, new BlockPos(3, 1, 3));
		Villager baby = helper.spawn(EntityType.VILLAGER, new BlockPos(5, 1, 5));
		//?}
		baby.setBaby(true);
		golem.setNoAi(true);
		if (!fixed) MojiraFixPack.VANILLA_ENTITIES.add(golem.getUUID());
		golem.offerFlower(true);
		helper.runAfterDelay(300, () -> {
			int left = golem.getOfferFlowerTick();
			MojiraFixPack.VANILLA_ENTITIES.remove(golem.getUUID());
			if (fixed) helper.assertTrue(left == 0, "the golem is still holding out its poppy (" + left + " ticks left)");
			else helper.assertTrue(left > 0, "a baby villager took the poppy without the fix; is MC-159069 fixed?");
			helper.succeed();
		});
	}

	/** MC-159069: a baby villager in reach takes the poppy an iron golem holds out. */
	public static void babyTakesPoppy(GameTestHelper helper) {
		poppy(helper, true);
	}

	/** Without the fix the golem holds the poppy until its 20 seconds run out. */
	public static void babyTakesPoppyVanilla(GameTestHelper helper) {
		poppy(helper, false);
	}
}
