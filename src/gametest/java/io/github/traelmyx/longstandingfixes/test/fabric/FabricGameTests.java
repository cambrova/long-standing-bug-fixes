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

	@GameTest(structure = EMPTY, maxTicks = 320)
	public void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 320)
	public void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireballVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void catBlocksOnlyItsChest(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChest(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void catBlocksOnlyItsChestVanilla(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChestVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void dispenserLightsPortal(GameTestHelper helper) {
		FixTests.dispenserLightsPortal(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void dispenserLightsPortalVanilla(GameTestHelper helper) {
		FixTests.dispenserLightsPortalVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 60)
	public void honeySlideSpeed(GameTestHelper helper) {
		FixTests.honeySlideSpeed(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 60)
	public void honeySlideSpeedVanilla(GameTestHelper helper) {
		FixTests.honeySlideSpeedVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void explosionPushesWindCharge(GameTestHelper helper) {
		FixTests.explosionPushesWindCharge(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void explosionPushesWindChargeVanilla(GameTestHelper helper) {
		FixTests.explosionPushesWindChargeVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void shieldRepelsMob(GameTestHelper helper) {
		FixTests.shieldRepelsMob(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void shieldRepelsMobVanilla(GameTestHelper helper) {
		FixTests.shieldRepelsMobVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void villagerDemandStopsAtZero(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZero(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void villagerDemandStopsAtZeroVanilla(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZeroVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void unbreakableFlintAndSteelKept(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKept(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void unbreakableFlintAndSteelKeptVanilla(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKeptVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void creeperDefusesForCreative(GameTestHelper helper) {
		FixTests.creeperDefusesForCreative(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void creeperDefusesForCreativeVanilla(GameTestHelper helper) {
		FixTests.creeperDefusesForCreativeVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void lightningSkipsSpectators(GameTestHelper helper) {
		FixTests.lightningSkipsSpectators(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void lightningSkipsSpectatorsVanilla(GameTestHelper helper) {
		FixTests.lightningSkipsSpectatorsVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void foxFollowsDoMobLoot(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLoot(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void foxFollowsDoMobLootVanilla(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLootVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cauldronFollowsMobGriefing(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefing(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cauldronFollowsMobGriefingVanilla(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefingVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spearKnockbackOnPlayers(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayers(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spearKnockbackOnPlayersVanilla(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayersVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void killingBlastSparesDrops(GameTestHelper helper) {
		FixTests.killingBlastSparesDrops(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void killingBlastSparesDropsVanilla(GameTestHelper helper) {
		FixTests.killingBlastSparesDropsVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void bonusHealthSurvivesReload(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReload(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void bonusHealthSurvivesReloadVanilla(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReloadVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cooldownSurvivesRelog(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelog(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cooldownSurvivesRelogVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelogVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cooldownSurvivesDimensionChange(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChange(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void cooldownSurvivesDimensionChangeVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChangeVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spamLimitInRealTime(GameTestHelper helper) {
		FixTests.spamLimitInRealTime(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void spamLimitInRealTimeVanilla(GameTestHelper helper) {
		FixTests.spamLimitInRealTimeVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void crystalIgnoresSnowball(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowball(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void crystalIgnoresSnowballVanilla(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowballVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void traderFleesRavager(GameTestHelper helper) {
		FixTests.traderFleesRavager(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void traderFleesRavagerVanilla(GameTestHelper helper) {
		FixTests.traderFleesRavagerVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void shulkerRefusesLevitation(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitation(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void shulkerRefusesLevitationVanilla(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitationVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 260)
	public void golemSparesTeammate(GameTestHelper helper) {
		FixTests.golemSparesTeammate(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 260)
	public void golemSparesTeammateVanilla(GameTestHelper helper) {
		FixTests.golemSparesTeammateVanilla(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void adventureSpawnOnSurface(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurface(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 40)
	public void adventureSpawnOnSurfaceVanilla(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurfaceVanilla(helper);
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

	@GameTest(template = EMPTY, timeoutTicks = 320)
	public void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 320)
	public void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireballVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void catBlocksOnlyItsChest(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChest(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void catBlocksOnlyItsChestVanilla(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChestVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void dispenserLightsPortal(GameTestHelper helper) {
		FixTests.dispenserLightsPortal(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void dispenserLightsPortalVanilla(GameTestHelper helper) {
		FixTests.dispenserLightsPortalVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 60)
	public void honeySlideSpeed(GameTestHelper helper) {
		FixTests.honeySlideSpeed(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 60)
	public void honeySlideSpeedVanilla(GameTestHelper helper) {
		FixTests.honeySlideSpeedVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void explosionPushesWindCharge(GameTestHelper helper) {
		FixTests.explosionPushesWindCharge(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void explosionPushesWindChargeVanilla(GameTestHelper helper) {
		FixTests.explosionPushesWindChargeVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void shieldRepelsMob(GameTestHelper helper) {
		FixTests.shieldRepelsMob(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void shieldRepelsMobVanilla(GameTestHelper helper) {
		FixTests.shieldRepelsMobVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void villagerDemandStopsAtZero(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZero(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void villagerDemandStopsAtZeroVanilla(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZeroVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void unbreakableFlintAndSteelKept(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKept(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void unbreakableFlintAndSteelKeptVanilla(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKeptVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void creeperDefusesForCreative(GameTestHelper helper) {
		FixTests.creeperDefusesForCreative(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void creeperDefusesForCreativeVanilla(GameTestHelper helper) {
		FixTests.creeperDefusesForCreativeVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void lightningSkipsSpectators(GameTestHelper helper) {
		FixTests.lightningSkipsSpectators(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void lightningSkipsSpectatorsVanilla(GameTestHelper helper) {
		FixTests.lightningSkipsSpectatorsVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void foxFollowsDoMobLoot(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLoot(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void foxFollowsDoMobLootVanilla(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLootVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cauldronFollowsMobGriefing(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefing(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cauldronFollowsMobGriefingVanilla(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefingVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spearKnockbackOnPlayers(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayers(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spearKnockbackOnPlayersVanilla(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayersVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void killingBlastSparesDrops(GameTestHelper helper) {
		FixTests.killingBlastSparesDrops(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void killingBlastSparesDropsVanilla(GameTestHelper helper) {
		FixTests.killingBlastSparesDropsVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void bonusHealthSurvivesReload(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReload(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void bonusHealthSurvivesReloadVanilla(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReloadVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cooldownSurvivesRelog(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelog(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cooldownSurvivesRelogVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelogVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cooldownSurvivesDimensionChange(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChange(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void cooldownSurvivesDimensionChangeVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChangeVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spamLimitInRealTime(GameTestHelper helper) {
		FixTests.spamLimitInRealTime(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void spamLimitInRealTimeVanilla(GameTestHelper helper) {
		FixTests.spamLimitInRealTimeVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void crystalIgnoresSnowball(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowball(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void crystalIgnoresSnowballVanilla(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowballVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void traderFleesRavager(GameTestHelper helper) {
		FixTests.traderFleesRavager(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100)
	public void traderFleesRavagerVanilla(GameTestHelper helper) {
		FixTests.traderFleesRavagerVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void shulkerRefusesLevitation(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitation(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void shulkerRefusesLevitationVanilla(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitationVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 260)
	public void golemSparesTeammate(GameTestHelper helper) {
		FixTests.golemSparesTeammate(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 260)
	public void golemSparesTeammateVanilla(GameTestHelper helper) {
		FixTests.golemSparesTeammateVanilla(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void adventureSpawnOnSurface(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurface(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public void adventureSpawnOnSurfaceVanilla(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurfaceVanilla(helper);
	}
}
^///?}
*///?}
