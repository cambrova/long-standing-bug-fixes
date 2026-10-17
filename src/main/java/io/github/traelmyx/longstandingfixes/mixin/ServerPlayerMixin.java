package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MC-219854: spectators were made immune to the void in 21w10a (MC-214790, "works as intended"), and from 21w11a they take
 * void damage again, so staff spectating below an End island die. Only void damage is blocked.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	//? if >=1.21.11 {
	/*@Inject(method = "isInvulnerableTo(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$spectatorsIgnoreTheVoid(net.minecraft.server.level.ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
	*///?} else {
	@Inject(method = "isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
	private void longstandingfixes$spectatorsIgnoreTheVoid(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
	//?}
		ServerPlayer self = (ServerPlayer) (Object) this;
		if (LongStandingFixes.spectatorVoid && self.isSpectator() && source.is(DamageTypes.FELL_OUT_OF_WORLD)
			&& !LongStandingFixes.VANILLA_OBJECTS.contains(self))
			cir.setReturnValue(true);
	}
}
