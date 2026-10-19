package io.github.traelmyx.longstandingfixes.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** A mob's goals, for adding the ravager goal (MC-262810) and for the game tests. */
@Mixin(Mob.class)
public interface MobGoalsAccessor {
	@Accessor("goalSelector")
	GoalSelector longstandingfixes$goals();
}
