package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11 {
/*import net.minecraft.world.level.storage.ValueInput;
*///?} else
import net.minecraft.nbt.CompoundTag;

/**
 * MC-17876: health is read back before the bonus max health from worn gear is applied (that happens on the first tick),
 * so anyone above 20 health with health-boosting armour comes back from a relog or chunk reload capped at 20. The saved
 * health is kept aside and restored once the gear's bonus is back, never above the real maximum.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityHealthMixin {
	@Unique
	private float longstandingfixes$savedHealth = -1;

	//? if >=1.21.11 {
	/*@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$rememberHealth(ValueInput input, CallbackInfo ci) {
		float saved = input.getFloatOr("Health", -1);
	*///?} else {
	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$rememberHealth(CompoundTag tag, CallbackInfo ci) {
		float saved = tag.contains("Health", 99) ? tag.getFloat("Health") : -1;
	//?}
		LivingEntity self = (LivingEntity) (Object) this;
		this.longstandingfixes$savedHealth = LongStandingFixes.keepBonusHealth && saved > self.getHealth() ? saved : -1;
	}

	@Inject(method = "detectEquipmentUpdates", at = @At("TAIL"))
	private void longstandingfixes$restoreHealth(CallbackInfo ci) {
		if (this.longstandingfixes$savedHealth < 0) return;
		LivingEntity self = (LivingEntity) (Object) this;
		if (!LongStandingFixes.VANILLA_ENTITIES.contains(self.getUUID()) && !LongStandingFixes.VANILLA_OBJECTS.contains(self) && self.isAlive())
			self.setHealth(Math.min(this.longstandingfixes$savedHealth, self.getMaxHealth()));
		this.longstandingfixes$savedHealth = -1;
	}
}
