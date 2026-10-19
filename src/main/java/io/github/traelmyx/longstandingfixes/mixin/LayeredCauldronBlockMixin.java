package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if <1.21.11
import net.minecraft.world.level.Level;

/**
 * MC-248588: a burning mob that walks into a water or powder snow cauldron uses up a level of it even with mobGriefing
 * off, the rule that keeps mobs from changing blocks. Mobs leave the cauldron alone then (they still stop burning).
 * Players are not mobs and are not affected. The check sits in a lambda from 1.21.2 on, hence the wildcard.
 */
@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin {
	//? if >=1.21.11 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;mayInteract(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean longstandingfixes$mobGriefing(Entity entity, ServerLevel level, BlockPos pos, Operation<Boolean> original) {
		if (LongStandingFixes.cauldronMobGriefing && !(entity instanceof Player) && !LongStandingFixes.mobGriefing(level)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(entity.getUUID())) return false;
		return original.call(entity, level, pos);
	}
	*///?} else {
	@WrapOperation(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;mayInteract(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean longstandingfixes$mobGriefing(Entity entity, Level level, BlockPos pos, Operation<Boolean> original) {
		if (LongStandingFixes.cauldronMobGriefing && !(entity instanceof Player) && level instanceof ServerLevel serverLevel
			&& !LongStandingFixes.mobGriefing(serverLevel) && !LongStandingFixes.VANILLA_ENTITIES.contains(entity.getUUID())) return false;
		return original.call(entity, level, pos);
	}
	//?}
}
