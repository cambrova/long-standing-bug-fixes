package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-205843: shulkers sit fixed to a block and Levitation can't lift them, yet they still take the effect, swirl its
 * particles and pass it on through a beacon or arrow test. Shulkers can no longer get Levitation.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityEffectMixin {
	@Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$shulkersStayPut(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof Shulker shulker && LongStandingFixes.shulkersNoLevitation && effect.is(MobEffects.LEVITATION)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(shulker.getUUID()))
			cir.setReturnValue(false);
	}
}
