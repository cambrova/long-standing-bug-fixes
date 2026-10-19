package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC-179072: a creeper that has started to swell keeps going when its player switches to creative or spectator, and
 * blows up next to them. Those players can't be targeted, so the creeper now calms down, as if they had walked away.
 */
@Mixin(SwellGoal.class)
public abstract class SwellGoalMixin {
	@Shadow
	@Final
	private Creeper creeper;
	@Shadow
	private LivingEntity target;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$defuseForCreative(CallbackInfo ci) {
		if (LongStandingFixes.creepersDefuse && this.target instanceof Player player && (player.isCreative() || player.isSpectator())
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(this.creeper.getUUID())) {
			this.creeper.setSwellDir(-1);
			ci.cancel();
		}
	}
}
