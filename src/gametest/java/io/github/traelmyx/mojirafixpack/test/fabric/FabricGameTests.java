package io.github.traelmyx.mojirafixpack.test.fabric;

//? if fabric {
/*import io.github.traelmyx.mojirafixpack.test.FixTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "mojira_fix_pack_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 200)
	public void campfireComparator(GameTestHelper helper) {
		FixTests.campfireComparator(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 200)
	public void campfireComparatorVanilla(GameTestHelper helper) {
		FixTests.campfireComparatorVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 400)
	public void babyTakesPoppy(GameTestHelper helper) {
		FixTests.babyTakesPoppy(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 400)
	public void babyTakesPoppyVanilla(GameTestHelper helper) {
		FixTests.babyTakesPoppyVanilla(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "mojira_fix_pack_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public void campfireComparator(GameTestHelper helper) {
		FixTests.campfireComparator(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public void campfireComparatorVanilla(GameTestHelper helper) {
		FixTests.campfireComparatorVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public void babyTakesPoppy(GameTestHelper helper) {
		FixTests.babyTakesPoppy(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public void babyTakesPoppyVanilla(GameTestHelper helper) {
		FixTests.babyTakesPoppyVanilla(helper);
	}
}
^///?}
*///?}
