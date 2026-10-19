package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

/**
 * MC-262422: lightning in a thunderstorm aims at a random living entity near the strike, spectators included, so it
 * kept striking next to players who were only watching. Spectators are no longer picked.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
	@ModifyArg(method = "findLightningTargetAround", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"), index = 2)
	private Predicate<LivingEntity> longstandingfixes$skipSpectators(Predicate<LivingEntity> original) {
		if (!LongStandingFixes.lightningSkipsSpectators) return original;
		return original.and(entity -> !entity.isSpectator() || LongStandingFixes.VANILLA_OBJECTS.contains(entity));
	}
}
