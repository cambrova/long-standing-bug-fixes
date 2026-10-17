package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.11 {
/*import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?} else {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
//?}

/**
 * MC-210491: hitting a fireball (or any projectile you can bat back) knocks it away but makes no vibration, so sculk
 * sensors ignore it, while hitting any other entity counts as damaging it. Batting a projectile away now gives the same
 * vibration as hitting a boat or a minecart.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {
	//? if >=1.21.11 {
	/*@Inject(method = "deflectProjectile", at = @At("RETURN"))
	private void longstandingfixes$vibration(Entity projectile, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ()) longstandingfixes$vibrate(projectile);
	}
	*///?} else {
	@WrapOperation(method = "attack", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/entity/projectile/Projectile;deflect(Lnet/minecraft/world/entity/projectile/ProjectileDeflection;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Z)Z"))
	private boolean longstandingfixes$vibration(Projectile projectile, ProjectileDeflection deflection, Entity deflector, Entity owner, boolean byAttack, Operation<Boolean> original) {
		boolean deflected = original.call(projectile, deflection, deflector, owner, byAttack);
		if (deflected) longstandingfixes$vibrate(projectile);
		return deflected;
	}
	//?}

	@org.spongepowered.asm.mixin.Unique
	private void longstandingfixes$vibrate(Entity projectile) {
		Player player = (Player) (Object) this;
		if (LongStandingFixes.sculkBattedProjectiles && !player.level().isClientSide() && !LongStandingFixes.VANILLA_ENTITIES.contains(projectile.getUUID()))
			projectile.gameEvent(GameEvent.ENTITY_DAMAGE, player);
	}
}
