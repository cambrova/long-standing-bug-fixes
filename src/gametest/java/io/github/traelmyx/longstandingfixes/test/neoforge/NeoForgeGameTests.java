package io.github.traelmyx.longstandingfixes.test.neoforge;

//? if neoforge {
import io.github.traelmyx.longstandingfixes.test.ClientSmoke;
import io.github.traelmyx.longstandingfixes.test.FixTests;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
//? if >=1.21.5 {
/*import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
*///?} else {
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
//?}

//? if <1.21.5 {
@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
//?}
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
	public static final String MOD_ID = "long_standing_fixes_gametest";

	public NeoForgeGameTests(IEventBus modBus, Dist dist) {
		//? if >=1.21.5 {
		/*FUNCTIONS.register(modBus);
		modBus.addListener(NeoForgeGameTests::registerTests);
		*///?}
		if (dist.isClient() && ClientSmoke.start()) {
			NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
		}
	}

	//? if >=1.21.5 {
	/*private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, MOD_ID);
	private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(MOD_ID, "empty");

	static {
		FUNCTIONS.register("campfire_comparator", () -> FixTests::campfireComparator);
		FUNCTIONS.register("campfire_comparator_vanilla", () -> FixTests::campfireComparatorVanilla);
		FUNCTIONS.register("baby_takes_poppy", () -> FixTests::babyTakesPoppy);
		FUNCTIONS.register("baby_takes_poppy_vanilla", () -> FixTests::babyTakesPoppyVanilla);
		FUNCTIONS.register("burning_skeleton_arrows", () -> FixTests::burningSkeletonArrows);
		FUNCTIONS.register("burning_skeleton_arrows_vanilla", () -> FixTests::burningSkeletonArrowsVanilla);
		FUNCTIONS.register("wind_charge_pushes_items", () -> FixTests::windChargePushesItems);
		FUNCTIONS.register("wind_charge_pushes_items_vanilla", () -> FixTests::windChargePushesItemsVanilla);
		FUNCTIONS.register("spectator_ignores_void", () -> FixTests::spectatorIgnoresVoid);
		FUNCTIONS.register("spectator_ignores_void_vanilla", () -> FixTests::spectatorIgnoresVoidVanilla);
		FUNCTIONS.register("broken_name_logged_once", () -> FixTests::brokenNameLoggedOnce);
		FUNCTIONS.register("broken_name_logged_once_vanilla", () -> FixTests::brokenNameLoggedOnceVanilla);
		FUNCTIONS.register("creeper_lit_once", () -> FixTests::creeperLitOnce);
		FUNCTIONS.register("creeper_lit_once_vanilla", () -> FixTests::creeperLitOnceVanilla);
		FUNCTIONS.register("falling_blocks_land", () -> FixTests::fallingBlocksLand);
		FUNCTIONS.register("falling_blocks_land_vanilla", () -> FixTests::fallingBlocksLandVanilla);
		FUNCTIONS.register("lighten_up_not_for_wax", () -> FixTests::lightenUpNotForWax);
		FUNCTIONS.register("lighten_up_not_for_wax_vanilla", () -> FixTests::lightenUpNotForWaxVanilla);
		FUNCTIONS.register("sculk_hears_batted_fireball", () -> FixTests::sculkHearsBattedFireball);
		FUNCTIONS.register("sculk_hears_batted_fireball_vanilla", () -> FixTests::sculkHearsBattedFireballVanilla);
		FUNCTIONS.register("cat_blocks_only_its_chest", () -> FixTests::catBlocksOnlyItsChest);
		FUNCTIONS.register("cat_blocks_only_its_chest_vanilla", () -> FixTests::catBlocksOnlyItsChestVanilla);
		FUNCTIONS.register("dispenser_lights_portal", () -> FixTests::dispenserLightsPortal);
		FUNCTIONS.register("dispenser_lights_portal_vanilla", () -> FixTests::dispenserLightsPortalVanilla);
		FUNCTIONS.register("honey_slide_speed", () -> FixTests::honeySlideSpeed);
		FUNCTIONS.register("honey_slide_speed_vanilla", () -> FixTests::honeySlideSpeedVanilla);
		FUNCTIONS.register("explosion_pushes_wind_charge", () -> FixTests::explosionPushesWindCharge);
		FUNCTIONS.register("explosion_pushes_wind_charge_vanilla", () -> FixTests::explosionPushesWindChargeVanilla);
		FUNCTIONS.register("shield_repels_mob", () -> FixTests::shieldRepelsMob);
		FUNCTIONS.register("shield_repels_mob_vanilla", () -> FixTests::shieldRepelsMobVanilla);
		FUNCTIONS.register("villager_demand_stops_at_zero", () -> FixTests::villagerDemandStopsAtZero);
		FUNCTIONS.register("villager_demand_stops_at_zero_vanilla", () -> FixTests::villagerDemandStopsAtZeroVanilla);
		FUNCTIONS.register("unbreakable_flint_and_steel_kept", () -> FixTests::unbreakableFlintAndSteelKept);
		FUNCTIONS.register("unbreakable_flint_and_steel_kept_vanilla", () -> FixTests::unbreakableFlintAndSteelKeptVanilla);
		FUNCTIONS.register("creeper_defuses_for_creative", () -> FixTests::creeperDefusesForCreative);
		FUNCTIONS.register("creeper_defuses_for_creative_vanilla", () -> FixTests::creeperDefusesForCreativeVanilla);
		FUNCTIONS.register("lightning_skips_spectators", () -> FixTests::lightningSkipsSpectators);
		FUNCTIONS.register("lightning_skips_spectators_vanilla", () -> FixTests::lightningSkipsSpectatorsVanilla);
		FUNCTIONS.register("fox_follows_do_mob_loot", () -> FixTests::foxFollowsDoMobLoot);
		FUNCTIONS.register("fox_follows_do_mob_loot_vanilla", () -> FixTests::foxFollowsDoMobLootVanilla);
		FUNCTIONS.register("cauldron_follows_mob_griefing", () -> FixTests::cauldronFollowsMobGriefing);
		FUNCTIONS.register("cauldron_follows_mob_griefing_vanilla", () -> FixTests::cauldronFollowsMobGriefingVanilla);
		FUNCTIONS.register("spear_knockback_on_players", () -> FixTests::spearKnockbackOnPlayers);
		FUNCTIONS.register("spear_knockback_on_players_vanilla", () -> FixTests::spearKnockbackOnPlayersVanilla);
		FUNCTIONS.register("killing_blast_spares_drops", () -> FixTests::killingBlastSparesDrops);
		FUNCTIONS.register("killing_blast_spares_drops_vanilla", () -> FixTests::killingBlastSparesDropsVanilla);
		FUNCTIONS.register("bonus_health_survives_reload", () -> FixTests::bonusHealthSurvivesReload);
		FUNCTIONS.register("bonus_health_survives_reload_vanilla", () -> FixTests::bonusHealthSurvivesReloadVanilla);
		FUNCTIONS.register("cooldown_survives_relog", () -> FixTests::cooldownSurvivesRelog);
		FUNCTIONS.register("cooldown_survives_relog_vanilla", () -> FixTests::cooldownSurvivesRelogVanilla);
		FUNCTIONS.register("cooldown_survives_dimension_change", () -> FixTests::cooldownSurvivesDimensionChange);
		FUNCTIONS.register("cooldown_survives_dimension_change_vanilla", () -> FixTests::cooldownSurvivesDimensionChangeVanilla);
		FUNCTIONS.register("spam_limit_in_real_time", () -> FixTests::spamLimitInRealTime);
		FUNCTIONS.register("spam_limit_in_real_time_vanilla", () -> FixTests::spamLimitInRealTimeVanilla);
		FUNCTIONS.register("crystal_ignores_snowball", () -> FixTests::crystalIgnoresSnowball);
		FUNCTIONS.register("crystal_ignores_snowball_vanilla", () -> FixTests::crystalIgnoresSnowballVanilla);
		FUNCTIONS.register("trader_flees_ravager", () -> FixTests::traderFleesRavager);
		FUNCTIONS.register("trader_flees_ravager_vanilla", () -> FixTests::traderFleesRavagerVanilla);
		FUNCTIONS.register("shulker_refuses_levitation", () -> FixTests::shulkerRefusesLevitation);
		FUNCTIONS.register("shulker_refuses_levitation_vanilla", () -> FixTests::shulkerRefusesLevitationVanilla);
		FUNCTIONS.register("golem_spares_teammate", () -> FixTests::golemSparesTeammate);
		FUNCTIONS.register("golem_spares_teammate_vanilla", () -> FixTests::golemSparesTeammateVanilla);
		FUNCTIONS.register("adventure_spawn_on_surface", () -> FixTests::adventureSpawnOnSurface);
		FUNCTIONS.register("adventure_spawn_on_surface_vanilla", () -> FixTests::adventureSpawnOnSurfaceVanilla);
		FUNCTIONS.register("elytra_stops_in_water", () -> FixTests::elytraStopsInWater);
		FUNCTIONS.register("elytra_stops_in_water_vanilla", () -> FixTests::elytraStopsInWaterVanilla);
		FUNCTIONS.register("bucketed_sulfur_cubes_split_persistent", () -> FixTests::bucketedSulfurCubesSplitPersistent);
		FUNCTIONS.register("bucketed_sulfur_cubes_split_persistent_vanilla", () -> FixTests::bucketedSulfurCubesSplitPersistentVanilla);
		FUNCTIONS.register("ridden_mobs_attack", () -> FixTests::riddenMobsAttack);
		FUNCTIONS.register("ridden_mobs_attack_vanilla", () -> FixTests::riddenMobsAttackVanilla);
		FUNCTIONS.register("names_looked_up_in_background", () -> FixTests::namesLookedUpInBackground);
		FUNCTIONS.register("names_looked_up_in_background_vanilla", () -> FixTests::namesLookedUpInBackgroundVanilla);
	}

	private static void registerTests(RegisterGameTestsEvent event) {
		//? if >=26 {
		Holder<TestEnvironmentDefinition<?>> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));
		//?} else
		/^Holder<TestEnvironmentDefinition> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));^/
		register(event, env, "campfire_comparator", 200, 1, 1);
		register(event, env, "campfire_comparator_vanilla", 200, 1, 1);
		register(event, env, "baby_takes_poppy", 400, 1, 1);
		register(event, env, "baby_takes_poppy_vanilla", 400, 1, 1);
		register(event, env, "burning_skeleton_arrows", 100, 1, 1);
		register(event, env, "burning_skeleton_arrows_vanilla", 100, 1, 1);
		register(event, env, "wind_charge_pushes_items", 100, 1, 1);
		register(event, env, "wind_charge_pushes_items_vanilla", 100, 1, 1);
		register(event, env, "spectator_ignores_void", 40, 1, 1);
		register(event, env, "spectator_ignores_void_vanilla", 40, 1, 1);
		register(event, env, "broken_name_logged_once", 40, 1, 1);
		register(event, env, "broken_name_logged_once_vanilla", 40, 1, 1);
		register(event, env, "creeper_lit_once", 40, 1, 1);
		register(event, env, "creeper_lit_once_vanilla", 40, 1, 1);
		register(event, env, "falling_blocks_land", 120, 1, 1);
		register(event, env, "falling_blocks_land_vanilla", 120, 1, 1);
		register(event, env, "lighten_up_not_for_wax", 40, 1, 1);
		register(event, env, "lighten_up_not_for_wax_vanilla", 40, 1, 1);
		register(event, env, "sculk_hears_batted_fireball", 320, 1, 1);
		register(event, env, "sculk_hears_batted_fireball_vanilla", 320, 1, 1);
		register(event, env, "cat_blocks_only_its_chest", 40, 1, 1);
		register(event, env, "cat_blocks_only_its_chest_vanilla", 40, 1, 1);
		register(event, env, "dispenser_lights_portal", 40, 1, 1);
		register(event, env, "dispenser_lights_portal_vanilla", 40, 1, 1);
		register(event, env, "honey_slide_speed", 60, 1, 1);
		register(event, env, "honey_slide_speed_vanilla", 60, 1, 1);
		register(event, env, "explosion_pushes_wind_charge", 40, 1, 1);
		register(event, env, "explosion_pushes_wind_charge_vanilla", 40, 1, 1);
		register(event, env, "shield_repels_mob", 40, 1, 1);
		register(event, env, "shield_repels_mob_vanilla", 40, 1, 1);
		register(event, env, "villager_demand_stops_at_zero", 40, 1, 1);
		register(event, env, "villager_demand_stops_at_zero_vanilla", 40, 1, 1);
		register(event, env, "unbreakable_flint_and_steel_kept", 40, 1, 1);
		register(event, env, "unbreakable_flint_and_steel_kept_vanilla", 40, 1, 1);
		register(event, env, "creeper_defuses_for_creative", 40, 1, 1);
		register(event, env, "creeper_defuses_for_creative_vanilla", 40, 1, 1);
		register(event, env, "lightning_skips_spectators", 40, 1, 1);
		register(event, env, "lightning_skips_spectators_vanilla", 40, 1, 1);
		register(event, env, "fox_follows_do_mob_loot", 40, 1, 1);
		register(event, env, "fox_follows_do_mob_loot_vanilla", 40, 1, 1);
		register(event, env, "cauldron_follows_mob_griefing", 40, 1, 1);
		register(event, env, "cauldron_follows_mob_griefing_vanilla", 40, 1, 1);
		register(event, env, "spear_knockback_on_players", 40, 1, 1);
		register(event, env, "spear_knockback_on_players_vanilla", 40, 1, 1);
		register(event, env, "killing_blast_spares_drops", 40, 1, 1);
		register(event, env, "killing_blast_spares_drops_vanilla", 40, 1, 1);
		register(event, env, "bonus_health_survives_reload", 40, 1, 1);
		register(event, env, "bonus_health_survives_reload_vanilla", 40, 1, 1);
		register(event, env, "cooldown_survives_relog", 40, 1, 1);
		register(event, env, "cooldown_survives_relog_vanilla", 40, 1, 1);
		register(event, env, "cooldown_survives_dimension_change", 40, 1, 1);
		register(event, env, "cooldown_survives_dimension_change_vanilla", 40, 1, 1);
		register(event, env, "spam_limit_in_real_time", 40, 1, 1);
		register(event, env, "spam_limit_in_real_time_vanilla", 40, 1, 1);
		register(event, env, "crystal_ignores_snowball", 40, 1, 1);
		register(event, env, "crystal_ignores_snowball_vanilla", 40, 1, 1);
		register(event, env, "trader_flees_ravager", 100, 1, 1);
		register(event, env, "trader_flees_ravager_vanilla", 100, 1, 1);
		register(event, env, "shulker_refuses_levitation", 40, 1, 1);
		register(event, env, "shulker_refuses_levitation_vanilla", 40, 1, 1);
		register(event, env, "golem_spares_teammate", 260, 1, 1);
		register(event, env, "golem_spares_teammate_vanilla", 260, 1, 1);
		register(event, env, "adventure_spawn_on_surface", 40, 1, 1);
		register(event, env, "adventure_spawn_on_surface_vanilla", 40, 1, 1);
		register(event, env, "elytra_stops_in_water", 40, 1, 1);
		register(event, env, "elytra_stops_in_water_vanilla", 40, 1, 1);
		register(event, env, "bucketed_sulfur_cubes_split_persistent", 40, 1, 1);
		register(event, env, "bucketed_sulfur_cubes_split_persistent_vanilla", 40, 1, 1);
		register(event, env, "ridden_mobs_attack", 120, 1, 1);
		register(event, env, "ridden_mobs_attack_vanilla", 120, 1, 1);
		register(event, env, "names_looked_up_in_background", 1200, 1, 1);
		register(event, env, "names_looked_up_in_background_vanilla", 1200, 1, 1);
	}

	//? if >=26 {
	private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, int maxTicks, int attempts, int successes) {
	//?} else
	/^private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition> env, String name, int maxTicks, int attempts, int successes) {^/
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		event.registerTest(id, new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, id),
			//? if >=26.3 {
			new TestData<>(env, net.minecraft.world.level.Level.OVERWORLD, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			//?} else if >=26 {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			^///?} else {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false)));
			^///?}
	}
	*///?} else {
	@GameTest(template = "empty", timeoutTicks = 200)
	public static void campfireComparator(GameTestHelper helper) {
		FixTests.campfireComparator(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 200)
	public static void campfireComparatorVanilla(GameTestHelper helper) {
		FixTests.campfireComparatorVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 400)
	public static void babyTakesPoppy(GameTestHelper helper) {
		FixTests.babyTakesPoppy(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 400)
	public static void babyTakesPoppyVanilla(GameTestHelper helper) {
		FixTests.babyTakesPoppyVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void burningSkeletonArrows(GameTestHelper helper) {
		FixTests.burningSkeletonArrows(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void burningSkeletonArrowsVanilla(GameTestHelper helper) {
		FixTests.burningSkeletonArrowsVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void windChargePushesItems(GameTestHelper helper) {
		FixTests.windChargePushesItems(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void windChargePushesItemsVanilla(GameTestHelper helper) {
		FixTests.windChargePushesItemsVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spectatorIgnoresVoid(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoid(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spectatorIgnoresVoidVanilla(GameTestHelper helper) {
		FixTests.spectatorIgnoresVoidVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void brokenNameLoggedOnce(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnce(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void brokenNameLoggedOnceVanilla(GameTestHelper helper) {
		FixTests.brokenNameLoggedOnceVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void creeperLitOnce(GameTestHelper helper) {
		FixTests.creeperLitOnce(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void creeperLitOnceVanilla(GameTestHelper helper) {
		FixTests.creeperLitOnceVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 120)
	public static void fallingBlocksLand(GameTestHelper helper) {
		FixTests.fallingBlocksLand(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 120)
	public static void fallingBlocksLandVanilla(GameTestHelper helper) {
		FixTests.fallingBlocksLandVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void lightenUpNotForWax(GameTestHelper helper) {
		FixTests.lightenUpNotForWax(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void lightenUpNotForWaxVanilla(GameTestHelper helper) {
		FixTests.lightenUpNotForWaxVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 320)
	public static void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 320)
	public static void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireballVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void catBlocksOnlyItsChest(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChest(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void catBlocksOnlyItsChestVanilla(GameTestHelper helper) {
		FixTests.catBlocksOnlyItsChestVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void dispenserLightsPortal(GameTestHelper helper) {
		FixTests.dispenserLightsPortal(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void dispenserLightsPortalVanilla(GameTestHelper helper) {
		FixTests.dispenserLightsPortalVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 60)
	public static void honeySlideSpeed(GameTestHelper helper) {
		FixTests.honeySlideSpeed(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 60)
	public static void honeySlideSpeedVanilla(GameTestHelper helper) {
		FixTests.honeySlideSpeedVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void explosionPushesWindCharge(GameTestHelper helper) {
		FixTests.explosionPushesWindCharge(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void explosionPushesWindChargeVanilla(GameTestHelper helper) {
		FixTests.explosionPushesWindChargeVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void shieldRepelsMob(GameTestHelper helper) {
		FixTests.shieldRepelsMob(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void shieldRepelsMobVanilla(GameTestHelper helper) {
		FixTests.shieldRepelsMobVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void villagerDemandStopsAtZero(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZero(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void villagerDemandStopsAtZeroVanilla(GameTestHelper helper) {
		FixTests.villagerDemandStopsAtZeroVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void unbreakableFlintAndSteelKept(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKept(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void unbreakableFlintAndSteelKeptVanilla(GameTestHelper helper) {
		FixTests.unbreakableFlintAndSteelKeptVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void creeperDefusesForCreative(GameTestHelper helper) {
		FixTests.creeperDefusesForCreative(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void creeperDefusesForCreativeVanilla(GameTestHelper helper) {
		FixTests.creeperDefusesForCreativeVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void lightningSkipsSpectators(GameTestHelper helper) {
		FixTests.lightningSkipsSpectators(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void lightningSkipsSpectatorsVanilla(GameTestHelper helper) {
		FixTests.lightningSkipsSpectatorsVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void foxFollowsDoMobLoot(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLoot(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void foxFollowsDoMobLootVanilla(GameTestHelper helper) {
		FixTests.foxFollowsDoMobLootVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cauldronFollowsMobGriefing(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefing(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cauldronFollowsMobGriefingVanilla(GameTestHelper helper) {
		FixTests.cauldronFollowsMobGriefingVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spearKnockbackOnPlayers(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayers(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spearKnockbackOnPlayersVanilla(GameTestHelper helper) {
		FixTests.spearKnockbackOnPlayersVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void killingBlastSparesDrops(GameTestHelper helper) {
		FixTests.killingBlastSparesDrops(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void killingBlastSparesDropsVanilla(GameTestHelper helper) {
		FixTests.killingBlastSparesDropsVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void bonusHealthSurvivesReload(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReload(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void bonusHealthSurvivesReloadVanilla(GameTestHelper helper) {
		FixTests.bonusHealthSurvivesReloadVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cooldownSurvivesRelog(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelog(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cooldownSurvivesRelogVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesRelogVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cooldownSurvivesDimensionChange(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChange(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void cooldownSurvivesDimensionChangeVanilla(GameTestHelper helper) {
		FixTests.cooldownSurvivesDimensionChangeVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spamLimitInRealTime(GameTestHelper helper) {
		FixTests.spamLimitInRealTime(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void spamLimitInRealTimeVanilla(GameTestHelper helper) {
		FixTests.spamLimitInRealTimeVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void crystalIgnoresSnowball(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowball(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void crystalIgnoresSnowballVanilla(GameTestHelper helper) {
		FixTests.crystalIgnoresSnowballVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void traderFleesRavager(GameTestHelper helper) {
		FixTests.traderFleesRavager(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100)
	public static void traderFleesRavagerVanilla(GameTestHelper helper) {
		FixTests.traderFleesRavagerVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void shulkerRefusesLevitation(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitation(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void shulkerRefusesLevitationVanilla(GameTestHelper helper) {
		FixTests.shulkerRefusesLevitationVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 260)
	public static void golemSparesTeammate(GameTestHelper helper) {
		FixTests.golemSparesTeammate(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 260)
	public static void golemSparesTeammateVanilla(GameTestHelper helper) {
		FixTests.golemSparesTeammateVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void adventureSpawnOnSurface(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurface(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void adventureSpawnOnSurfaceVanilla(GameTestHelper helper) {
		FixTests.adventureSpawnOnSurfaceVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void elytraStopsInWater(GameTestHelper helper) {
		FixTests.elytraStopsInWater(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void elytraStopsInWaterVanilla(GameTestHelper helper) {
		FixTests.elytraStopsInWaterVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void bucketedSulfurCubesSplitPersistent(GameTestHelper helper) {
		FixTests.bucketedSulfurCubesSplitPersistent(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void bucketedSulfurCubesSplitPersistentVanilla(GameTestHelper helper) {
		FixTests.bucketedSulfurCubesSplitPersistentVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 120)
	public static void riddenMobsAttack(GameTestHelper helper) {
		FixTests.riddenMobsAttack(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 120)
	public static void riddenMobsAttackVanilla(GameTestHelper helper) {
		FixTests.riddenMobsAttackVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 1200)
	public static void namesLookedUpInBackground(GameTestHelper helper) {
		FixTests.namesLookedUpInBackground(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 1200)
	public static void namesLookedUpInBackgroundVanilla(GameTestHelper helper) {
		FixTests.namesLookedUpInBackgroundVanilla(helper);
	}
	//?}
}
//?}
