package io.github.traelmyx.mojirafixpack.mixin;

import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
*///?} else {
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
//?}

import java.util.Comparator;

/**
 * MC-159069: before 1.14 a baby villager would walk up to an iron golem holding out a poppy, look at it for a moment and
 * take it. Since the switch to the new villager AI they ignore it. Here the nearest baby villager walks over again, and once
 * it stands next to the golem it takes the flower.
 */
@Mixin(IronGolem.class)
public abstract class IronGolemMixin {
	@Inject(method = "aiStep", at = @At("TAIL"))
	private void mojirafixpack$babyTakesPoppy(CallbackInfo ci) {
		IronGolem golem = (IronGolem) (Object) this;
		if (!MojiraFixPack.golemPoppy || golem.level().isClientSide() || golem.getOfferFlowerTick() <= 0 || golem.tickCount % 5 != 0
			|| MojiraFixPack.VANILLA_ENTITIES.contains(golem.getUUID()))
			return;
		Villager baby = golem.level().getEntitiesOfClass(Villager.class, golem.getBoundingBox().inflate(8.0), Villager::isBaby).stream()
			.min(Comparator.comparingDouble(golem::distanceToSqr)).orElse(null);
		if (baby == null)
			return;
		baby.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(golem, true));
		if (baby.distanceToSqr(golem) > 2.5 * 2.5) {
			baby.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(golem, 0.5F, 1));
		} else if (golem.getRandom().nextInt(4) == 0) {
			// taken: the golem lowers its arm, as in 1.13
			golem.offerFlower(false);
		}
	}
}
