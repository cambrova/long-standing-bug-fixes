package io.github.traelmyx.longstandingfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.animal.fox.Fox;
*///?} else
import net.minecraft.world.entity.animal.Fox;

/**
 * MC-153010: with doMobLoot off, mobs drop nothing when they die, except foxes, which still drop whatever they carry in
 * their mouth. They follow the rule now. (NeoForge drops the mouth item from dropEquipment instead, hence both names.)
 */
@Mixin(Fox.class)
public abstract class FoxMixin {
	//? if >=1.21.11 {
	/*@WrapOperation(method = {"dropAllDeathLoot", "dropEquipment"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fox/Fox;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	private ItemEntity longstandingfixes$respectMobLoot(Fox fox, ServerLevel level, ItemStack stack, Operation<ItemEntity> original) {
		if (LongStandingFixes.foxMobLoot && !LongStandingFixes.mobLoot(level) && !LongStandingFixes.VANILLA_ENTITIES.contains(fox.getUUID())) return null;
		return original.call(fox, level, stack);
	}
	*///?} else {
	@WrapOperation(method = {"dropAllDeathLoot", "dropEquipment"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Fox;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	private ItemEntity longstandingfixes$respectMobLoot(Fox fox, ItemStack stack, Operation<ItemEntity> original) {
		if (LongStandingFixes.foxMobLoot && fox.level() instanceof ServerLevel level && !LongStandingFixes.mobLoot(level)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(fox.getUUID())) return null;
		return original.call(fox, stack);
	}
	//?}
}
