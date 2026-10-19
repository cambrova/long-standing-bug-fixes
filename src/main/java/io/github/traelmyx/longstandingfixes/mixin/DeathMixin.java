package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * MC-164171 / MC-109083, part one: notes when an entity is dying, so the items it drops can be told apart (see
 * ItemEntityDeathDropMixin). Players have their own die method, so both classes are wrapped.
 */
@Mixin({LivingEntity.class, ServerPlayer.class})
public abstract class DeathMixin {
	@WrapMethod(method = "die")
	private void longstandingfixes$markDeathDrops(DamageSource source, Operation<Void> original) {
		Object self = this;
		boolean mark = LongStandingFixes.keepDeathDrops && !LongStandingFixes.VANILLA_OBJECTS.contains(self)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(((LivingEntity) self).getUUID());
		if (mark) LongStandingFixes.DYING.set(LongStandingFixes.DYING.get() + 1);
		try {
			original.call(source);
		} finally {
			if (mark) LongStandingFixes.DYING.set(LongStandingFixes.DYING.get() - 1);
		}
	}
}
