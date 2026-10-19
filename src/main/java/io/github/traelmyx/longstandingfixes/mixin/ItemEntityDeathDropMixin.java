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
public abstract class ItemEntityDeathDropMixin implements LongStandingFixes.DeathDrop {
	@Unique
	private long longstandingfixes$droppedOnDeathAt = Long.MIN_VALUE;

	@Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
	private void longstandingfixes$noteDeathDrop(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (LongStandingFixes.DYING.get() > 0 && !self.level().isClientSide()) this.longstandingfixes$droppedOnDeathAt = self.level().getGameTime();
	}

	@Override
	public boolean longstandingfixes$freshDeathDrop() {
		return this.longstandingfixes$droppedOnDeathAt == ((ItemEntity) (Object) this).level().getGameTime();
	}

	// items only have their own ignoreExplosion from 1.21.5; before that EntityExplosionMixin asks freshDeathDrop
	//? if >=1.21.5 {
	/*@Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$survivesTheKillingBlast(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
		if (this.longstandingfixes$freshDeathDrop()) cir.setReturnValue(true);
	}
	*///?}
}
