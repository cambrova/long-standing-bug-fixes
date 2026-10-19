package io.github.traelmyx.longstandingfixes.mixin;

//? if <1.21.5 {
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC-147694: up to 1.13 a mob whose hit landed on a raised shield was knocked back. 1.14 moved that call onto the attacker
 * but kept pushing the defender, so mobs stopped being repelled. The attacker is pushed away from the shield again.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$repelAttacker(LivingEntity defender, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!LongStandingFixes.shieldsRepelMobs || LongStandingFixes.VANILLA_ENTITIES.contains(self.getUUID())) return;
		self.knockback(0.5, defender.getX() - self.getX(), defender.getZ() - self.getZ());
		ci.cancel();
	}
}
//?} else {
/*/^* Not applied from 1.21.5, where shields were reworked (left out of the mixin list for those versions). ^/
public final class LivingEntityMixin {
	private LivingEntityMixin() {
	}
}
*///?}
