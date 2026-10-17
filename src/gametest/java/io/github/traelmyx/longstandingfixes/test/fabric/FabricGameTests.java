package io.github.traelmyx.longstandingfixes.test.fabric;

//? if fabric {
/*import io.github.traelmyx.longstandingfixes.test.FixTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "long_standing_fixes_gametest:empty";

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

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void burningSkeletonArrows(GameTestHelper helper) {
		FixTests.burningSkeletonArrows(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void burningSkeletonArrowsVanilla(GameTestHelper helper) {
		FixTests.burningSkeletonArrowsVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void windChargePushesItems(GameTestHelper helper) {
		FixTests.windChargePushesItems(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void windChargePushesItemsVanilla(GameTestHelper helper) {
		FixTests.windChargePushesItemsVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spectatorIgnoresVoid(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoid(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spectatorIgnoresVoidVanilla(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoidVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void brokenNameLoggedOnce(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnce(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void brokenNameLoggedOnceVanilla(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnceVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void creeperLitOnce(GameTestHelper helper) {
		FixTests.creeperLitOnce(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void creeperLitOnceVanilla(GameTestHelper helper) {
		FixTests.creeperLitOnceVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 120)
	public void fallingBlocksLand(GameTestHelper helper) {
		FixTests.fallingBlocksLand(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 120)
	public void fallingBlocksLandVanilla(GameTestHelper helper) {
		FixTests.fallingBlocksLandVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void lightenUpNotForWax(GameTestHelper helper) {
		FixTests.lightenUpNotForWax(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void lightenUpNotForWaxVanilla(GameTestHelper helper) {
		FixTests.lightenUpNotForWaxVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 120)
	public void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 120)
	public void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireballVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 120)
	public void spyglassProbe(GameTestHelper helper) {
		FixTests.spyglassProbe(helper);
	}
}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "long_standing_fixes_gametest:empty";

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

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void burningSkeletonArrows(GameTestHelper helper) {
		FixTests.burningSkeletonArrows(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void burningSkeletonArrowsVanilla(GameTestHelper helper) {
		FixTests.burningSkeletonArrowsVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void windChargePushesItems(GameTestHelper helper) {
		FixTests.windChargePushesItems(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void windChargePushesItemsVanilla(GameTestHelper helper) {
		FixTests.windChargePushesItemsVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spectatorIgnoresVoid(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoid(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spectatorIgnoresVoidVanilla(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoidVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void brokenNameLoggedOnce(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnce(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void brokenNameLoggedOnceVanilla(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnceVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void creeperLitOnce(GameTestHelper helper) {
		FixTests.creeperLitOnce(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void creeperLitOnceVanilla(GameTestHelper helper) {
		FixTests.creeperLitOnceVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 120)
	public void fallingBlocksLand(GameTestHelper helper) {
		FixTests.fallingBlocksLand(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 120)
	public void fallingBlocksLandVanilla(GameTestHelper helper) {
		FixTests.fallingBlocksLandVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void lightenUpNotForWax(GameTestHelper helper) {
		FixTests.lightenUpNotForWax(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void lightenUpNotForWaxVanilla(GameTestHelper helper) {
		FixTests.lightenUpNotForWaxVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 120)
	public void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 120)
	public void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireballVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 120)
	public void spyglassProbe(GameTestHelper helper) {
		FixTests.spyglassProbe(helper);
	}
}
^///?}
*///?}
