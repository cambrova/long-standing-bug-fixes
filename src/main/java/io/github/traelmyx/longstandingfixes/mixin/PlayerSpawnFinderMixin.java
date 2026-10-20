package io.github.traelmyx.longstandingfixes.mixin;

//? if >=1.21.11 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/^*
 * MC-302519: when the default game mode is Adventure, the spawn search skips the random spread and just looks for open
 * space above and below the world spawn. Since 1.21.9 nothing keeps the spawn chunk loaded, and in an unloaded chunk
 * every block counts as open space, so the search runs all the way down and players respawn inside the bedrock. The chunk
 * is loaded before the search now, the way the normal spread search loads each spot it tries.
 ^/
@Mixin(PlayerSpawnFinder.class)
public abstract class PlayerSpawnFinderMixin {
	@WrapOperation(method = "findSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/PlayerSpawnFinder;fixupSpawnHeight(Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"))
	private static Vec3 longstandingfixes$loadSpawnChunk(CollisionGetter level, BlockPos pos, Operation<Vec3> original) {
		if (LongStandingFixes.adventureSpawnOnSurface && level instanceof ServerLevel serverLevel && !LongStandingFixes.VANILLA_BLOCKS.contains(pos))
			serverLevel.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
		return original.call(level, pos);
	}
}
*///?} else {
/** Not applied on 1.21.1, which has no spawn finder yet and keeps spawn chunks loaded (left out of the mixin list for that version). */
public final class PlayerSpawnFinderMixin {
	private PlayerSpawnFinderMixin() {
	}
}
//?}
