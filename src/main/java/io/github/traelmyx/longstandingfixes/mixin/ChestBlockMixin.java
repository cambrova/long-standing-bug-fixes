package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.ChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

/**
 * MC-265635: a cat sitting on a chest also kept the chest below that one shut. The game looks for cats in the block
 * space above a chest, and a cat sitting on the next chest up reaches down into it. Only cats sitting on this chest count.
 */
@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {
	@ModifyExpressionValue(method = "isCatSittingOnChest", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/level/LevelAccessor;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
	private static <T extends Entity> List<T> long_standing_fixes$onlyCatsOnThisChest(List<T> cats, @Local(argsOnly = true) BlockPos pos) {
		if (!LongStandingFixes.catOnChestAbove || cats.isEmpty() || LongStandingFixes.VANILLA_BLOCKS.contains(pos))
			return cats;
		// a cat sitting on this chest stands at its lid, below pos.y + 1; one on the chest above stands near pos.y + 2
		return cats.stream().filter(cat -> cat.getY() < pos.getY() + 1.5).toList();
	}
}
