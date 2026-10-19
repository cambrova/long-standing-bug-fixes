package io.github.traelmyx.longstandingfixes.mixin;

//? if >=1.21.11 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/^*
 * MC-279003: since 1.21.4 explosions knock entities back through Entity.push, which wind charges ignore (so mobs walking
 * into them don't shove them around). That left wind charges unmoved by TNT and by other wind charges. Explosions set
 * their speed directly again, the way they did up to 1.21.3.
 ^/
@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
	//? if >=26.3 {
	@WrapOperation(method = "hurtEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;pushFromExplosion(Lnet/minecraft/world/phys/Vec3;)V"))
	//?} else
	/^@WrapOperation(method = "hurtEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;push(Lnet/minecraft/world/phys/Vec3;)V"))^/
	private void longstandingfixes$pushWindCharges(Entity entity, Vec3 knockback, Operation<Void> original) {
		if (LongStandingFixes.explosionsPushWindCharges && entity instanceof AbstractWindCharge
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(entity.getUUID())) {
			entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
			entity.needsSync = true;
		} else original.call(entity, knockback);
	}
}
*///?} else {
/** Not applied on 1.21.1, where explosions still move wind charges (left out of the mixin list for that version). */
public final class ServerExplosionMixin {
	private ServerExplosionMixin() {
	}
}
//?}
