package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-186909: every click on a creeper that is already lit used up another bit of flint and steel durability, or another
 * fire charge. Lighting it once is enough; clicks on a lit creeper now do nothing.
 */
@Mixin(Creeper.class)
public abstract class CreeperMixin {
	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$lightOnce(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		Creeper creeper = (Creeper) (Object) this;
		if (LongStandingFixes.creeperIgniters && creeper.isIgnited() && player.getItemInHand(hand).is(ItemTags.CREEPER_IGNITERS)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(creeper.getUUID()))
			cir.setReturnValue(InteractionResult.PASS);
	}
}
