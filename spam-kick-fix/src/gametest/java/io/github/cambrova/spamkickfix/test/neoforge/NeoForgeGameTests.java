package io.github.cambrova.spamkickfix.test.neoforge;

//? if neoforge {
import io.github.cambrova.spamkickfix.test.ClientSmoke;
import io.github.cambrova.spamkickfix.test.FixTests;
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
	public static final String MOD_ID = "spam_kick_fix_gametest";

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
		FUNCTIONS.register("real_time_cooldown", () -> FixTests::realTimeCooldown);
		FUNCTIONS.register("real_time_cooldown_vanilla", () -> FixTests::realTimeCooldownVanilla);
		FUNCTIONS.register("commands_own_limit", () -> FixTests::commandsOwnLimit);
		FUNCTIONS.register("commands_own_limit_vanilla", () -> FixTests::commandsOwnLimitVanilla);
		FUNCTIONS.register("command_limit_off", () -> FixTests::commandLimitOff);
		FUNCTIONS.register("command_limit_off_vanilla", () -> FixTests::commandLimitOffVanilla);
	}

	private static void registerTests(RegisterGameTestsEvent event) {
		//? if >=26 {
		Holder<TestEnvironmentDefinition<?>> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));
		//?} else
		/^Holder<TestEnvironmentDefinition> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));^/
		register(event, env, "real_time_cooldown", 40, 1, 1);
		register(event, env, "real_time_cooldown_vanilla", 40, 1, 1);
		register(event, env, "commands_own_limit", 40, 1, 1);
		register(event, env, "commands_own_limit_vanilla", 40, 1, 1);
		register(event, env, "command_limit_off", 40, 1, 1);
		register(event, env, "command_limit_off_vanilla", 40, 1, 1);
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
	@GameTest(template = "empty", timeoutTicks = 40)
	public static void realTimeCooldown(GameTestHelper helper) {
		FixTests.realTimeCooldown(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void realTimeCooldownVanilla(GameTestHelper helper) {
		FixTests.realTimeCooldownVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void commandsOwnLimit(GameTestHelper helper) {
		FixTests.commandsOwnLimit(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void commandsOwnLimitVanilla(GameTestHelper helper) {
		FixTests.commandsOwnLimitVanilla(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void commandLimitOff(GameTestHelper helper) {
		FixTests.commandLimitOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 40)
	public static void commandLimitOffVanilla(GameTestHelper helper) {
		FixTests.commandLimitOffVanilla(helper);
	}
	//?}
}
//?}
