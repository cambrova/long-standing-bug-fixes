package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.GameProfileRepository;

import java.util.Map;
import java.util.Optional;
//? if >=1.21.11 {
/*import net.minecraft.server.players.CachedUserNameToIdResolver;
import net.minecraft.server.players.NameAndId;
*///?} else {
import com.mojang.authlib.GameProfile;
import net.minecraft.server.players.GameProfileCache;
//?}

/** MC-99601: the player-name cache says whether a name is known without asking Mojang. */
//? if >=1.21.11 {
/*@Mixin(CachedUserNameToIdResolver.class)
*///?} else
@Mixin(GameProfileCache.class)
public abstract class ProfileCacheMixin implements LongStandingFixes.ProfileCachePeek {
	@Shadow
	@Final
	private Map<String, ?> profilesByName;

	@Override
	public boolean longstandingfixes$isCached(String name) {
		return this.profilesByName.containsKey(name.toLowerCase(java.util.Locale.ROOT));
	}

	// the game tests stand in for a slow answer from Mojang with a pause and an offline profile
	//? if >=1.21.11 {
	/*@Inject(method = "lookupGameProfile", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$slowTestLookup(GameProfileRepository repository, String name, CallbackInfoReturnable<Optional<NameAndId>> cir) {
		if (LongStandingFixes.slowTestLookup(name)) cir.setReturnValue(Optional.of(NameAndId.createOffline(name)));
	}
	*///?} else {
	@Inject(method = "lookupGameProfile", at = @At("HEAD"), cancellable = true)
	private static void longstandingfixes$slowTestLookup(GameProfileRepository repository, String name, CallbackInfoReturnable<Optional<GameProfile>> cir) {
		if (LongStandingFixes.slowTestLookup(name)) cir.setReturnValue(Optional.of(net.minecraft.core.UUIDUtil.createOfflineProfile(name)));
	}
	//?}
}
