package io.github.traelmyx.longstandingfixes.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** The game-test server has no real player-name cache; the MC-99601 tests give it one. */
@Mixin(MinecraftServer.class)
public interface MinecraftServerServicesAccessor {
	@Accessor("services")
	Services longstandingfixes$services();

	@Mutable
	@Accessor("services")
	void longstandingfixes$setServices(Services services);
}
