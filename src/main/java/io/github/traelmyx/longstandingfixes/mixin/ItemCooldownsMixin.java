package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
//?}

/** MC-111801 / MC-253476: lets a player's item cooldowns be written out, read back and sent to their game again. */
@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin implements LongStandingFixes.Cooldowns {
	@Shadow
	private int tickCount;

	//? if >=1.21.11 {
	/*@Shadow
	@Final
	private Map<Identifier, ?> cooldowns;

	@Shadow
	public abstract void addCooldown(Identifier group, int ticks);

	@Shadow
	protected abstract void onCooldownStarted(Identifier group, int ticks);

	private static String longstandingfixes$id(Identifier key) {
		return key.toString();
	}

	private static Identifier longstandingfixes$key(String id) {
		return Identifier.tryParse(id);
	}
	*///?} else {
	@Shadow
	@Final
	private Map<Item, ?> cooldowns;

	@Shadow
	public abstract void addCooldown(Item item, int ticks);

	@Shadow
	protected abstract void onCooldownStarted(Item item, int ticks);

	private static String longstandingfixes$id(Item key) {
		return BuiltInRegistries.ITEM.getKey(key).toString();
	}

	private static Item longstandingfixes$key(String id) {
		ResourceLocation location = ResourceLocation.tryParse(id);
		return location == null ? null : BuiltInRegistries.ITEM.getOptional(location).orElse(null);
	}
	//?}

	@Override
	public String longstandingfixes$save() {
		StringBuilder out = new StringBuilder();
		this.cooldowns.forEach((key, cooldown) -> {
			int left = ((CooldownInstanceAccessor) cooldown).longstandingfixes$endTime() - this.tickCount;
			if (left > 0) out.append(out.isEmpty() ? "" : ";").append(longstandingfixes$id(key)).append('=').append(left);
		});
		return out.toString();
	}

	@Override
	public void longstandingfixes$load(String saved) {
		for (String entry : saved.split(";")) {
			int eq = entry.lastIndexOf('=');
			if (eq <= 0) continue;
			var key = longstandingfixes$key(entry.substring(0, eq));
			int left;
			try {
				left = Integer.parseInt(entry.substring(eq + 1));
			} catch (NumberFormatException e) {
				continue;
			}
			if (key != null && left > 0) this.addCooldown(key, left);
		}
	}

	@Override
	public void longstandingfixes$resend() {
		this.cooldowns.forEach((key, cooldown) -> {
			int left = ((CooldownInstanceAccessor) cooldown).longstandingfixes$endTime() - this.tickCount;
			if (left > 0) this.onCooldownStarted(key, left);
		});
	}
}
