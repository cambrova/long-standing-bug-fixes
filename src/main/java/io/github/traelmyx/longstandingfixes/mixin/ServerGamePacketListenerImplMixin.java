package io.github.traelmyx.longstandingfixes.mixin;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.11
/*import net.minecraft.util.TickThrottler;*/

/**
 * MC-276819: the chat, command and item-drop spam counters cool down by one step per server tick. On a server running
 * below 20 ticks a second they cool down slower in real time, so normal chatting or clicking items out of JEI gets
 * players kicked for spamming. The counters now also cool down for the real time that passed, as if the server had
 * kept up. At 20 ticks a second nothing changes.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin implements LongStandingFixes.SpamCounters {
	@Shadow
	public ServerPlayer player;

	//? if >=26 {
	/*@Shadow
	@Final
	private TickThrottler chatSpamThrottler;
	@Shadow
	@Final
	private TickThrottler commandSpamThrottler;
	@Shadow
	@Final
	private TickThrottler dropSpamThrottler;
	*///?} else if >=1.21.11 {
	/*@Shadow
	@Final
	private TickThrottler chatSpamThrottler;
	@Shadow
	@Final
	private TickThrottler dropSpamThrottler;
	*///?} else {
	@Shadow
	private int chatSpamTickCount;
	@Shadow
	private int dropSpamTickCount;
	//?}

	@Unique
	private static final long longstandingfixes$TICK = 50_000_000L;
	@Unique
	private long longstandingfixes$lastTick;
	@Unique
	private long longstandingfixes$behind;

	@Inject(method = "tick", at = @At("TAIL"))
	private void longstandingfixes$coolDownInRealTime(CallbackInfo ci) {
		long now = LongStandingFixes.CLOCK.getAsLong();
		if (this.longstandingfixes$lastTick != 0 && LongStandingFixes.spamRealTime && !LongStandingFixes.VANILLA_OBJECTS.contains(this.player)) {
			this.longstandingfixes$behind = Math.max(0, this.longstandingfixes$behind + now - this.longstandingfixes$lastTick - longstandingfixes$TICK);
			int missed = (int) Math.min(200, this.longstandingfixes$behind / longstandingfixes$TICK);
			this.longstandingfixes$behind -= missed * longstandingfixes$TICK;
			if (missed > 0) this.longstandingfixes$coolDown(missed);
		}
		this.longstandingfixes$lastTick = now;
	}

	@Unique
	private void longstandingfixes$coolDown(int steps) {
		//? if >=1.21.11 {
		/*for (int i = 0; i < steps; i++) {
			this.chatSpamThrottler.tick();
			this.dropSpamThrottler.tick();
			//? if >=26
			this.commandSpamThrottler.tick();
		}
		*///?} else {
		this.chatSpamTickCount = Math.max(0, this.chatSpamTickCount - steps);
		this.dropSpamTickCount = Math.max(0, this.dropSpamTickCount - steps);
		//?}
	}

	@Override
	public int longstandingfixes$chatSpam() {
		//? if >=1.21.11 {
		/*return ((TickThrottlerAccessor) this.chatSpamThrottler).longstandingfixes$count();
		*///?} else
		return this.chatSpamTickCount;
	}

	@Override
	public void longstandingfixes$setChatSpam(int count) {
		//? if >=1.21.11 {
		/*((TickThrottlerAccessor) this.chatSpamThrottler).longstandingfixes$setCount(count);
		*///?} else
		this.chatSpamTickCount = count;
	}
}
