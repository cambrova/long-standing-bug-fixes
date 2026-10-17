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
	//?}
}
//?}
