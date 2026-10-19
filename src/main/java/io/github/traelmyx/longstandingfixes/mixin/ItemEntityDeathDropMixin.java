package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-164171 / MC-109083: a wither skull or ghast fireball hurts what it hits first and explodes right after, in the same
 * tick. When the hit kills a player, their whole inventory drops and that same explosion destroys it. Items dropped by a
 * death ignore explosions during the tick they appear in; from the next tick on they are ordinary items.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityDeathDropMixin {
	@Unique
	private long longstandingfixes$droppedOnDeathAt = Long.MIN_VALUE;

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;DDD)V", at = @At("TAIL"))
	private void longstandingfixes$noteDeathDrop(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (LongStandingFixes.DYING.get() > 0 && !self.level().isClientSide()) this.longstandingfixes$droppedOnDeathAt = self.level().getGameTime();
	}

	@Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$survivesTheKillingBlast(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (this.longstandingfixes$droppedOnDeathAt == self.level().getGameTime()) cir.setReturnValue(true);
	}
}
