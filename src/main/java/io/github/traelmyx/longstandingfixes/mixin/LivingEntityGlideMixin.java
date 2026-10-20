package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//? if >=1.21.11 {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?} else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC-298489: gliding into water keeps the elytra open until you touch the ground. Water movement takes over, so you sink
 * or swim like normal, but the elytra keeps losing durability every second the whole time. Gliding now ends when you're
 * in water, the same as it does on landing.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityGlideMixin extends Entity {
	protected LivingEntityGlideMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	//? if >=1.21.11 {
	/*@Inject(method = "canGlide", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$noGlidingInWater(CallbackInfoReturnable<Boolean> cir) {
		if (this.longstandingfixes$stopsInWater()) cir.setReturnValue(false);
	}
	*///?} else {
	@Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$noGlidingInWater(CallbackInfo ci) {
		if (this.getSharedFlag(7) && this.longstandingfixes$stopsInWater()) {
			this.setSharedFlag(7, false);
			ci.cancel();
		}
	}
	//?}

	@Unique
	private boolean longstandingfixes$stopsInWater() {
		return LongStandingFixes.elytraStopsInWater && !this.level().isClientSide() && this.isInWater()
			&& !LongStandingFixes.VANILLA_OBJECTS.contains(this);
	}
}
