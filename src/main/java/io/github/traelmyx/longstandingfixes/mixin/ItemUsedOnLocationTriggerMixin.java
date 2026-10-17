package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=26 {
/*import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
*///?} else if >=1.21.11 {
/*import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
*///?} else {
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
//?}

/**
 * MC-269636: "Lighten Up" is for scraping a lit copper bulb so it shines brighter, but the advancement also lists the waxed
 * bulbs, and using an axe on a waxed bulb only takes the wax off. While the item-used-on-block trigger runs for a waxed
 * copper bulb, {@link PlayerAdvancementsMixin} holds back Lighten Up; everything else that trigger feeds (like "Wax Off")
 * is untouched.
 */
@Mixin(ItemUsedOnLocationTrigger.class)
public abstract class ItemUsedOnLocationTriggerMixin {
	@Inject(method = "trigger", at = @At("HEAD"))
	private void longstandingfixes$markWaxedBulb(CallbackInfo ci, @Local(argsOnly = true) ServerPlayer player, @Local(argsOnly = true) BlockPos pos) {
		if (!LongStandingFixes.lightenUp || LongStandingFixes.VANILLA_OBJECTS.contains(player))
			return;
		String path = BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(pos).getBlock()).getPath();
		if (path.startsWith("waxed_") && path.endsWith("copper_bulb"))
			LongStandingFixes.UNWAXING_BULB.set(Boolean.TRUE);
	}

	@Inject(method = "trigger", at = @At("RETURN"))
	private void longstandingfixes$clearWaxedBulb(CallbackInfo ci) {
		LongStandingFixes.UNWAXING_BULB.remove();
	}
}
