package io.github.traelmyx.mojirafixpack.mixin;

//? if >=1.21.11 {
/*import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/^*
 * MC-280273: since 1.21.5 dropped items ignore wind charge bursts entirely, so they can't be blown around any more. Only
 * wind charges are let through: they push but never hurt, and other explosions that skip items (with mobGriefing off) stay
 * as they are.
 ^/
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	@Inject(method = "ignoreExplosion", at = @At("HEAD"), cancellable = true)
	private void mojirafixpack$windChargesPushItems(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (MojiraFixPack.windChargeItems && explosion.getDirectSourceEntity() instanceof AbstractWindCharge
			&& !MojiraFixPack.VANILLA_ENTITIES.contains(self.getUUID()))
			cir.setReturnValue(false);
	}
}
*///?} else {
/** Not applied before 1.21.5, where wind charges still push items (left out of the mixin list for that version). */
public final class ItemEntityMixin {
	private ItemEntityMixin() {
	}
}
//?}
