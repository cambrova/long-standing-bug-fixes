package io.github.traelmyx.longstandingfixes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Reads when an item cooldown ends (MC-111801 / MC-253476). */
@Mixin(targets = "net.minecraft.world.item.ItemCooldowns$CooldownInstance")
public interface CooldownInstanceAccessor {
	@Accessor("endTime")
	int longstandingfixes$endTime();
}
