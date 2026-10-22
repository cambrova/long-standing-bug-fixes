package io.github.cambrova.spamkickfix.mixin;

//? if >=1.21.11 {
/*import net.minecraft.util.TickThrottler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/^* Reads and sets a spam counter. ^/
@Mixin(TickThrottler.class)
public interface TickThrottlerAccessor {
	@Accessor("count")
	int spamkickfix$count();

	@Accessor("count")
	void spamkickfix$setCount(int count);
}
*///?} else {
/** 1.21.1 keeps its spam counters as plain ints (left out of the mixin list for that version). */
public final class TickThrottlerAccessor {
	private TickThrottlerAccessor() {
	}
}
//?}
