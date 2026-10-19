package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
*///?} else
import net.minecraft.nbt.CompoundTag;

/**
 * MC-111801 / MC-253476: item cooldowns (ender pearls, a shield knocked out by an axe, goat horns) were never saved, so
 * logging out and back in cleared them. After a change of dimension the server still had them but the player's game
 * forgot them, so it let them try and then failed. They are saved with the player now and re-sent after a dimension
 * change.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerCooldownsMixin {
	@Unique
	private static final String longstandingfixes$KEY = "long_standing_fixes:cooldowns";
	@Unique
	private String longstandingfixes$pendingCooldowns;
	@Unique
	private Level longstandingfixes$lastLevel;

	@Unique
	private boolean longstandingfixes$enabled() {
		return LongStandingFixes.keepCooldowns && !LongStandingFixes.VANILLA_OBJECTS.contains(this);
	}

	//? if >=1.21.11 {
	/*@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$saveCooldowns(ValueOutput output, CallbackInfo ci) {
		if (!this.longstandingfixes$enabled()) return;
		String saved = ((LongStandingFixes.Cooldowns) ((ServerPlayer) (Object) this).getCooldowns()).longstandingfixes$save();
		if (!saved.isEmpty()) output.putString(longstandingfixes$KEY, saved);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$readCooldowns(ValueInput input, CallbackInfo ci) {
		String saved = input.getStringOr(longstandingfixes$KEY, "");
		this.longstandingfixes$pendingCooldowns = saved.isEmpty() ? null : saved;
	}
	*///?} else {
	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$saveCooldowns(CompoundTag tag, CallbackInfo ci) {
		if (!this.longstandingfixes$enabled()) return;
		String saved = ((LongStandingFixes.Cooldowns) ((ServerPlayer) (Object) this).getCooldowns()).longstandingfixes$save();
		if (!saved.isEmpty()) tag.putString(longstandingfixes$KEY, saved);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void longstandingfixes$readCooldowns(CompoundTag tag, CallbackInfo ci) {
		String saved = tag.getString(longstandingfixes$KEY);
		this.longstandingfixes$pendingCooldowns = saved.isEmpty() ? null : saved;
	}
	//?}

	/** Cooldowns are restored on the first tick, once the player has a connection to send them through. */
	@Inject(method = "tick", at = @At("HEAD"))
	private void longstandingfixes$restoreAndResend(CallbackInfo ci) {
		ServerPlayer self = (ServerPlayer) (Object) this;
		if (self.connection == null) return;
		var cooldowns = (LongStandingFixes.Cooldowns) self.getCooldowns();
		if (this.longstandingfixes$pendingCooldowns != null) {
			if (this.longstandingfixes$enabled()) cooldowns.longstandingfixes$load(this.longstandingfixes$pendingCooldowns);
			this.longstandingfixes$pendingCooldowns = null;
		}
		Level level = self.level();
		if (this.longstandingfixes$lastLevel != null && this.longstandingfixes$lastLevel != level && this.longstandingfixes$enabled())
			cooldowns.longstandingfixes$resend();
		this.longstandingfixes$lastLevel = level;
	}
}
