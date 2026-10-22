package io.github.cambrova.spamkickfix.test.fabric;

//? if fabric {
/*import io.github.cambrova.spamkickfix.test.FixTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "spam_kick_fix_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void realTimeCooldown(GameTestHelper helper) {
		FixTests.realTimeCooldown(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void realTimeCooldownVanilla(GameTestHelper helper) {
		FixTests.realTimeCooldownVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void commandsOwnLimit(GameTestHelper helper) {
		FixTests.commandsOwnLimit(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void commandsOwnLimitVanilla(GameTestHelper helper) {
		FixTests.commandsOwnLimitVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void commandLimitOff(GameTestHelper helper) {
		FixTests.commandLimitOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void commandLimitOffVanilla(GameTestHelper helper) {
		FixTests.commandLimitOffVanilla(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "spam_kick_fix_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void realTimeCooldown(GameTestHelper helper) {
		FixTests.realTimeCooldown(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void realTimeCooldownVanilla(GameTestHelper helper) {
		FixTests.realTimeCooldownVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void commandsOwnLimit(GameTestHelper helper) {
		FixTests.commandsOwnLimit(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void commandsOwnLimitVanilla(GameTestHelper helper) {
		FixTests.commandsOwnLimitVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void commandLimitOff(GameTestHelper helper) {
		FixTests.commandLimitOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void commandLimitOffVanilla(GameTestHelper helper) {
		FixTests.commandLimitOffVanilla(helper);
	}
}
^///?}
*///?}
