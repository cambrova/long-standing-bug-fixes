package io.github.traelmyx.longstandingfixes.mixin;

//? if <1.21.5 {
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** MC-164171 on 1.21.1, where items use Entity's ignoreExplosion: fresh death drops ignore the killing blast. */
@Mixin(Entity.class)
public abstract class EntityExplosionMixin {
	@Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$deathDropsSurvive(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof LongStandingFixes.DeathDrop drop && drop.longstandingfixes$freshDeathDrop()) cir.setReturnValue(true);
	}
}
//?} else {
/*/^* Not applied from 1.21.5, where items have their own ignoreExplosion (left out of the mixin list for those versions). ^/
public final class EntityExplosionMixin {
	private EntityExplosionMixin() {
	}
}
*///?}
