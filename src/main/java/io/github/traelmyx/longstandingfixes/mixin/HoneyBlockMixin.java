package io.github.traelmyx.longstandingfixes.mixin;

//? if >=1.21.11 {
/*import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/^*
 * MC-278572: since 1.21.2 honey converts an entity's fall speed as if gravity (0.08) and drag (0.98) had already been
 * applied after the block's effect, which is how mobs and players tick now. Items, TNT and minecarts still get block
 * effects straight after they move, like before, so for them the conversion only makes them slide down about
 * twice as fast. Non-living entities skip the conversion and slide at the 1.21.1 speed again.
 ^/
@Mixin(HoneyBlock.class)
public abstract class HoneyBlockMixin {
	@Unique
	private static final ThreadLocal<Boolean> longstandingfixes$plain = ThreadLocal.withInitial(() -> false);

	@Unique
	private static void longstandingfixes$enter(Entity entity) {
		longstandingfixes$plain.set(LongStandingFixes.honeySlide && !(entity instanceof LivingEntity)
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(entity.getUUID()));
	}

	@Inject(method = "isSlidingDown", at = @At("HEAD"))
	private void longstandingfixes$enterSliding(BlockPos pos, Entity entity, CallbackInfoReturnable<Boolean> cir) {
		longstandingfixes$enter(entity);
	}

	@Inject(method = "isSlidingDown", at = @At("RETURN"))
	private void longstandingfixes$leaveSliding(BlockPos pos, Entity entity, CallbackInfoReturnable<Boolean> cir) {
		longstandingfixes$plain.set(false);
	}

	@Inject(method = "doSlideMovement", at = @At("HEAD"))
	private void longstandingfixes$enterSlide(Entity entity, CallbackInfo ci) {
		longstandingfixes$enter(entity);
	}

	@Inject(method = "doSlideMovement", at = @At("RETURN"))
	private void longstandingfixes$leaveSlide(Entity entity, CallbackInfo ci) {
		longstandingfixes$plain.set(false);
	}

	@Inject(method = "getOldDeltaY", at = @At("HEAD"), cancellable = true)
	private static void longstandingfixes$plainOld(double deltaY, CallbackInfoReturnable<Double> cir) {
		if (longstandingfixes$plain.get()) cir.setReturnValue(deltaY);
	}

	@Inject(method = "getNewDeltaY", at = @At("HEAD"), cancellable = true)
	private static void longstandingfixes$plainNew(double deltaY, CallbackInfoReturnable<Double> cir) {
		if (longstandingfixes$plain.get()) cir.setReturnValue(deltaY);
	}
}
*///?} else {
/** Not applied on 1.21.1, where honey still slows every entity the same way (left out of the mixin list for that version). */
public final class HoneyBlockMixin {
	private HoneyBlockMixin() {
	}
}
//?}
