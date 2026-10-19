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
		register(event, env, "sculk_hears_batted_fireball", 120, 1, 1);
		register(event, env, "sculk_hears_batted_fireball_vanilla", 120, 1, 1);
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

	@GameTest(template = "empty", timeoutTicks = 120)
	public static void sculkHearsBattedFireball(GameTestHelper helper) {
		FixTests.sculkHearsBattedFireball(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 120)
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
	//?}
}
//?}
