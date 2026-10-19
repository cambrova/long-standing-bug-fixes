package io.github.traelmyx.longstandingfixes.mixin;

//? if >=26 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/^*
 * MC-310859: a spear knocks its target back twice, first by a fixed 0.4 and then by the weapon's own knockback, which is
 * where the Knockback enchantment goes. For a player, the first push is sent to their game and then the "send my new
 * speed" flag is cleared, so the second push never reaches them and Knockback does nothing to players. The first push
 * now waits, and both are sent together, the way mobs get them. 1.21.11 still pushed once with the sum, so it is fine.
 ^/
@Mixin(Player.class)
public abstract class PlayerStabMixin {
	@Unique
	private static final ThreadLocal<Boolean> longstandingfixes$holding = ThreadLocal.withInitial(() -> false);
	@Unique
	private static final ThreadLocal<Boolean> longstandingfixes$held = ThreadLocal.withInitial(() -> false);

	@WrapOperation(method = "stabAttack", at = @At(value = "INVOKE", ordinal = 0,
		target = "Lnet/minecraft/world/entity/player/Player;causeExtraKnockback(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/damagesource/DamageSource;FZ)V"))
	private void longstandingfixes$holdFirstPush(Player self, Entity target, float amount, Vec3 oldMovement, DamageSource source, float damage,
		boolean fromEffect, Operation<Void> original) {
		boolean hold = LongStandingFixes.spearKnockbackPlayers && target instanceof ServerPlayer && !LongStandingFixes.VANILLA_OBJECTS.contains(target);
		longstandingfixes$holding.set(hold);
		try {
			original.call(self, target, amount, oldMovement, source, damage, fromEffect);
		} finally {
			longstandingfixes$holding.set(false);
		}
	}

	@WrapOperation(method = "stabAttack", at = @At(value = "INVOKE", ordinal = 1,
		target = "Lnet/minecraft/world/entity/player/Player;causeExtraKnockback(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/damagesource/DamageSource;FZ)V"))
	private void longstandingfixes$sendBothPushes(Player self, Entity target, float amount, Vec3 oldMovement, DamageSource source, float damage,
		boolean fromEffect, Operation<Void> original) {
		if (longstandingfixes$held.get()) {
			//? if >=26.3 {
			target.syncVelocity = true;
			//?} else
			/^target.hurtMarked = true;^/
			longstandingfixes$held.set(false);
		}
		original.call(self, target, amount, oldMovement, source, damage, fromEffect);
	}

	//? if >=26.3 {
	@ModifyExpressionValue(method = "causeExtraKnockback", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;syncVelocity:Z", opcode = Opcodes.GETFIELD))
	//?} else
	/^@ModifyExpressionValue(method = "causeExtraKnockback", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;hurtMarked:Z", opcode = Opcodes.GETFIELD))^/
	private boolean longstandingfixes$waitWithFirstPush(boolean sync) {
		if (!longstandingfixes$holding.get()) return sync;
		if (sync) longstandingfixes$held.set(true);
		return false;
	}
}
*///?} else {
/** Not applied before 26.1: 1.21.1 has no spears and 1.21.11 pushes once (left out of the mixin list for those versions). */
public final class PlayerStabMixin {
	private PlayerStabMixin() {
	}
}
//?}
