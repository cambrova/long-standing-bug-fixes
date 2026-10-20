package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC-262182: since 1.19.4 a mob riding another mob steers it, and the mount's own movement and look goals are switched off
 * so the two don't fight over the wheel. Melee attacks are one of those goals, so a spider, zombie or golem carrying a rider
 * never hits anything again, even with its target right in front of it. Mounts that fight in melee now still strike a target
 * in reach, at the usual once-a-second pace; the rider keeps steering.
 */
@Mixin(Mob.class)
public abstract class MobRiddenAttackMixin {
	@Unique
	private int longstandingfixes$riddenAttackCooldown;

	@Inject(method = "serverAiStep", at = @At("TAIL"))
	private void longstandingfixes$attackWhileRidden(CallbackInfo ci) {
		Mob mob = (Mob) (Object) this;
		if (this.longstandingfixes$riddenAttackCooldown > 0) this.longstandingfixes$riddenAttackCooldown--;
		if (!LongStandingFixes.riddenMobsAttack || this.longstandingfixes$riddenAttackCooldown > 0 || !(mob.getControllingPassenger() instanceof Mob)
			|| LongStandingFixes.VANILLA_ENTITIES.contains(mob.getUUID()))
			return;
		var goals = ((MobGoalsAccessor) mob).longstandingfixes$goals();
		// only while the rider has the mount's own goals switched off (raiders on a ravager keep them), and only for melee fighters
		if (!((GoalSelectorAccessor) goals).longstandingfixes$disabledFlags().contains(Goal.Flag.MOVE)
			|| goals.getAvailableGoals().stream().noneMatch(goal -> goal.getGoal() instanceof MeleeAttackGoal))
			return;
		LivingEntity target = mob.getTarget();
		if (target == null || !target.isAlive() || !mob.isWithinMeleeAttackRange(target) || !mob.getSensing().hasLineOfSight(target))
			return;
		this.longstandingfixes$riddenAttackCooldown = 20;
		//? if >=26.3 {
		/*mob.swingForAttack(InteractionHand.MAIN_HAND);
		*///?} else
		mob.swing(InteractionHand.MAIN_HAND);
		//? if >=1.21.11 {
		/*mob.doHurtTarget((net.minecraft.server.level.ServerLevel) mob.level(), target);
		*///?} else
		mob.doHurtTarget(target);
	}
}
