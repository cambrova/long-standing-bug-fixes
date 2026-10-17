package io.github.traelmyx.longstandingfixes.mixin;

//? if <1.21.11 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * MC-168976 (1.21.1): a spawner whose entity data has a broken custom name logs a warning with a full stack trace every
 * time it builds the entity, which with a short spawn delay floods the server log. Each broken name is now logged once.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {
	@WrapOperation(method = "load", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
	private void longstandingfixes$warnOncePerName(Logger logger, String message, Object name, Object error, Operation<Void> original) {
		if (!LongStandingFixes.spawnerLogSpam || !message.contains("custom name") || LongStandingFixes.firstWarning(String.valueOf(name)))
			original.call(logger, message, name, error);
	}
}
//?} else {
/*/^* Not applied from 1.21.6 on: entity loading no longer logs broken custom names (left out of the mixin list). ^/
public final class EntityMixin {
	private EntityMixin() {
	}
}
*///?}
