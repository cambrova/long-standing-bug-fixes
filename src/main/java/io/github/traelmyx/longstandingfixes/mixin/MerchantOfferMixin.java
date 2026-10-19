package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC-163962: every restock without trades lowers a trade's demand further, with no floor, so old villagers sit at
 * demand values in the millions below zero and their prices never go up again however much you trade. Demand now
 * stops at 0, which is where a trade nobody uses would be anyway.
 */
@Mixin(MerchantOffer.class)
public abstract class MerchantOfferMixin {
	@Shadow
	private int demand;

	@Inject(method = "updateDemand", at = @At("TAIL"))
	private void longstandingfixes$noNegativeDemand(CallbackInfo ci) {
		if (LongStandingFixes.villagerDemand && this.demand < 0 && !LongStandingFixes.VANILLA_OBJECTS.contains(this)) this.demand = 0;
	}
}
