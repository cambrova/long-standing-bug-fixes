package io.github.traelmyx.longstandingfixes.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumSet;

/** The kinds of goal a mob may not run right now, e.g. moving while a rider steers it (MC-262182). */
@Mixin(GoalSelector.class)
public interface GoalSelectorAccessor {
	@Accessor("disabledFlags")
	EnumSet<Goal.Flag> longstandingfixes$disabledFlags();
}
