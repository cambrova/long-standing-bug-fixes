package io.github.traelmyx.mojirafixpack.mixin;

import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
*///?} else {
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
//?}

/**
 * MC-217397: up to 1.10, a skeleton that was on fire shot burning arrows, more often the higher the local difficulty. The
 * check got lost when wither skeletons were split off in 16w32a. Same rule as then: on fire, and the regional difficulty
 * beats a coin flip between 0 and 1.
 */
@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin {
	@Inject(method = "getArrow", at = @At("RETURN"))
	private void mojirafixpack$burningArrows(ItemStack arrow, float velocity, ItemStack weapon, CallbackInfoReturnable<AbstractArrow> cir) {
		AbstractSkeleton skeleton = (AbstractSkeleton) (Object) this;
		if (!MojiraFixPack.burningSkeletons || !skeleton.isOnFire() || MojiraFixPack.VANILLA_ENTITIES.contains(skeleton.getUUID()))
			return;
		if (skeleton.level().getCurrentDifficultyAt(skeleton.blockPosition()).isHarderThan(skeleton.getRandom().nextInt(2)))
			cir.getReturnValue().igniteForSeconds(5.0F);
	}
}
