package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** MC-269636, see {@link ItemUsedOnLocationTriggerMixin}: taking the wax off a copper bulb doesn't award Lighten Up. */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
	@Inject(method = "award", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$notForWax(AdvancementHolder advancement, String criterion, CallbackInfoReturnable<Boolean> cir) {
		if (LongStandingFixes.UNWAXING_BULB.get() != null && advancement.id().toString().equals("minecraft:adventure/lighten_up"))
			cir.setReturnValue(false);
	}
}
