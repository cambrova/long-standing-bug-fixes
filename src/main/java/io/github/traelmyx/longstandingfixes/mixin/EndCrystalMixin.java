package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-101851: snowballs, eggs and ender pearls deal no damage, yet one hit blows up an end crystal, so anyone can set off
 * crystals from a distance with a stack of snowballs. Projectile hits that deal no damage leave crystals alone now;
 * arrows, tridents and everything else still break them.
 */
@Mixin(EndCrystal.class)
public abstract class EndCrystalMixin {
	//? if >=1.21.11 {
	/*@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$harmlessHitsDoNothing(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
	*///?} else {
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$harmlessHitsDoNothing(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
	//?}
		EndCrystal self = (EndCrystal) (Object) this;
		if (LongStandingFixes.crystalsIgnoreHarmlessHits && amount <= 0.0F && source.is(DamageTypeTags.IS_PROJECTILE)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(self.getUUID()))
			cir.setReturnValue(false);
	}
}
