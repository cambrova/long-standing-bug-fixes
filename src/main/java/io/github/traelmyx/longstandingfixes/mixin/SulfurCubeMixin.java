package io.github.traelmyx.longstandingfixes.mixin;

//? if >=26 {
/*import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^*
 * MC-308361: a sulfur cube you let out of a bucket never despawns, but the small cubes it splits into when it dies don't
 * remember where it came from and despawn as soon as you walk away. They count as bucketed too now, like their parent.
 ^/
@Mixin(SulfurCube.class)
public abstract class SulfurCubeMixin {
	@Inject(method = "setUpSplitCube", at = @At("TAIL"))
	private void longstandingfixes$keepFromBucket(AbstractCubeMob cubeMob, int halfSize, float xd, float zd, CallbackInfo ci) {
		SulfurCube parent = (SulfurCube) (Object) this;
		if (LongStandingFixes.bucketedCubesSplitPersistent && parent.fromBucket() && cubeMob instanceof SulfurCube child
			&& !LongStandingFixes.VANILLA_ENTITIES.contains(parent.getUUID()))
			child.setFromBucket(true);
	}
}
*///?} else {
/** Not applied before 26.2, which has no sulfur cubes (left out of the mixin list for those versions). */
public final class SulfurCubeMixin {
	private SulfurCubeMixin() {
	}
}
//?}
