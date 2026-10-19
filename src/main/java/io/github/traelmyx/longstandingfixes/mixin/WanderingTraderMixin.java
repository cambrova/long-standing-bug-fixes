package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
*///?} else
import net.minecraft.world.entity.npc.WanderingTrader;

/**
 * MC-262810: wandering traders run from zombies, every kind of illager, vexes and zoglins, but not from ravagers, which
 * trample them. They keep their distance from ravagers too now, like they do from evokers.
 */
@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
	private WanderingTraderMixin(EntityType<? extends AbstractVillager> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void longstandingfixes$fleeRavagers(CallbackInfo ci) {
		if (!LongStandingFixes.tradersFleeRavagers) return;
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Ravager.class, 12.0F, 0.5, 0.5,
			ravager -> !LongStandingFixes.VANILLA_ENTITIES.contains(this.getUUID())));
	}
}
