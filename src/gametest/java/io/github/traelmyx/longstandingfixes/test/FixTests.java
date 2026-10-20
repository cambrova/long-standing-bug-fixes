package io.github.traelmyx.longstandingfixes.test;

import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
*///?} else {
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
//?}

/** Each fix is tested twice: with the fix, and with the same setup kept on vanilla behaviour, where the bug must show. */
public final class FixTests {
	private FixTests() {
	}

	private static net.minecraft.world.level.block.entity.BlockEntity be(GameTestHelper helper, BlockPos pos) {
		return helper.getLevel().getBlockEntity(helper.absolutePos(pos));
	}

	private static final BlockPos CAMPFIRE = new BlockPos(1, 1, 1), COMPARATOR = new BlockPos(1, 1, 2);

	private static void campfire(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 3; x++) for (int z = 0; z < 4; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		helper.setBlock(CAMPFIRE, Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false));
		if (!fixed) LongStandingFixes.VANILLA_BLOCKS.add(helper.absolutePos(CAMPFIRE));
		// facing north, the comparator reads the campfire behind it
		helper.setBlock(COMPARATOR, Blocks.COMPARATOR.defaultBlockState().setValue(ComparatorBlock.FACING, Direction.NORTH));
		CampfireBlockEntity campfire = (CampfireBlockEntity) be(helper, CAMPFIRE);
		campfire.getItems().set(0, new ItemStack(Items.BEEF));
		campfire.setChanged();
		helper.runAfterDelay(6, () -> {
			int one = ((ComparatorBlockEntity) be(helper, COMPARATOR)).getOutputSignal();
			for (int i = 1; i < 4; i++) campfire.getItems().set(i, new ItemStack(Items.BEEF));
			campfire.setChanged();
			helper.runAfterDelay(6, () -> {
				int four = ((ComparatorBlockEntity) be(helper, COMPARATOR)).getOutputSignal();
				LongStandingFixes.VANILLA_BLOCKS.remove(helper.absolutePos(CAMPFIRE));
				if (fixed) helper.assertTrue(one == 4 && four == 15, "comparator gave " + one + " for one item and " + four + " for four");
				else helper.assertTrue(one == 0 && four == 0, "vanilla comparator gave " + one + " and " + four + "; is MC-166272 fixed?");
				helper.succeed();
			});
		});
	}

	/** MC-166272: a comparator reads 4 for one item on a campfire and 15 for four. */
	public static void campfireComparator(GameTestHelper helper) {
		campfire(helper, true);
	}

	/** Without the fix the comparator stays at 0. */
	public static void campfireComparatorVanilla(GameTestHelper helper) {
		campfire(helper, false);
	}

	private static void poppy(GameTestHelper helper, boolean fixed) {
		// a walled 7x7 pen so the baby stays near the golem
		for (int x = 0; x < 7; x++) for (int z = 0; z < 7; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			if (x == 0 || z == 0 || x == 6 || z == 6) {
				helper.setBlock(new BlockPos(x, 1, z), Blocks.GLASS);
				helper.setBlock(new BlockPos(x, 2, z), Blocks.GLASS);
			}
		}
		//? if >=26 {
		/*IronGolem golem = helper.spawn(net.minecraft.world.entity.EntityTypes.IRON_GOLEM, new BlockPos(3, 1, 3));
		Villager baby = helper.spawn(net.minecraft.world.entity.EntityTypes.VILLAGER, new BlockPos(5, 1, 5));
		*///?} else {
		IronGolem golem = helper.spawn(EntityType.IRON_GOLEM, new BlockPos(3, 1, 3));
		Villager baby = helper.spawn(EntityType.VILLAGER, new BlockPos(5, 1, 5));
		//?}
		baby.setBaby(true);
		golem.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(golem.getUUID());
		golem.offerFlower(true);
		helper.runAfterDelay(300, () -> {
			int left = golem.getOfferFlowerTick();
			LongStandingFixes.VANILLA_ENTITIES.remove(golem.getUUID());
			if (fixed) helper.assertTrue(left == 0, "the golem is still holding out its poppy (" + left + " ticks left)");
			else helper.assertTrue(left > 0, "a baby villager took the poppy without the fix; is MC-159069 fixed?");
			helper.succeed();
		});
	}

	/** MC-159069: a baby villager in reach takes the poppy an iron golem holds out. */
	public static void babyTakesPoppy(GameTestHelper helper) {
		poppy(helper, true);
	}

	/** Without the fix the golem holds the poppy until its 20 seconds run out. */
	public static void babyTakesPoppyVanilla(GameTestHelper helper) {
		poppy(helper, false);
	}

	private static void skeleton(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 9; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		//? if >=26 {
		/*var skeleton = helper.spawn(net.minecraft.world.entity.EntityTypes.SKELETON, new BlockPos(2, 1, 1));
		var target = helper.spawn(net.minecraft.world.entity.EntityTypes.ZOMBIE, new BlockPos(2, 1, 7));
		*///?} else {
		var skeleton = helper.spawn(EntityType.SKELETON, new BlockPos(2, 1, 1));
		var target = helper.spawn(EntityType.ZOMBIE, new BlockPos(2, 1, 7));
		//?}
		skeleton.setNoAi(true);
		target.setNoAi(true);
		skeleton.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(skeleton.getUUID());
		int burning = 0, shots = 30;
		var area = new net.minecraft.world.phys.AABB(helper.absolutePos(BlockPos.ZERO)).inflate(16);
		for (int i = 0; i < shots; i++) {
			skeleton.setRemainingFireTicks(100);
			skeleton.performRangedAttack(target, 1.0F);
		}
		for (var arrow : helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.projectile.Projectile.class, area)) {
			if (arrow.getOwner() == skeleton && arrow.isOnFire()) burning++;
			arrow.discard();
		}
		LongStandingFixes.VANILLA_ENTITIES.remove(skeleton.getUUID());
		if (fixed) helper.assertTrue(burning > 0, "a burning skeleton shot " + shots + " arrows and none was on fire");
		else helper.assertTrue(burning == 0, burning + " burning arrows without the fix; is MC-217397 fixed?");
		helper.succeed();
	}

	/** MC-217397: a skeleton on fire shoots burning arrows (on regional difficulty above 0 or 1, so not every time). */
	public static void burningSkeletonArrows(GameTestHelper helper) {
		skeleton(helper, true);
	}

	/** Without the fix none of its arrows burn. */
	public static void burningSkeletonArrowsVanilla(GameTestHelper helper) {
		skeleton(helper, false);
	}

	private static void windCharge(GameTestHelper helper, boolean fixed) {
		//? if >=1.21.11 {
		/*for (int x = 0; x < 7; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var item = helper.spawnItem(Items.DIAMOND, new net.minecraft.world.phys.Vec3(2.5, 1.0, 2.5));
		item.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(item.getUUID());
		var start = item.position();
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(3.5, 1.6, 2.5));
		var charge = new net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge(helper.getLevel(), at.x, at.y, at.z, new net.minecraft.world.phys.Vec3(0, -0.6, 0));
		helper.getLevel().addFreshEntity(charge);
		helper.runAfterDelay(10, () -> {
			double moved = Math.sqrt(Math.pow(item.getX() - start.x, 2) + Math.pow(item.getZ() - start.z, 2));
			LongStandingFixes.VANILLA_ENTITIES.remove(item.getUUID());
			if (fixed) helper.assertTrue(moved > 0.2, "the wind charge moved the item only " + moved + " blocks");
			else helper.assertTrue(moved < 0.05, "the item moved " + moved + " blocks without the fix; is MC-280273 fixed?");
			helper.succeed();
		});
		*///?} else {
		// before 1.21.5 wind charges always pushed items; nothing to fix
		helper.succeed();
		//?}
	}

	/** MC-280273: a wind charge bursting next to a dropped item blows it away. */
	public static void windChargePushesItems(GameTestHelper helper) {
		windCharge(helper, true);
	}

	/** Without the fix the item doesn't move. */
	public static void windChargePushesItemsVanilla(GameTestHelper helper) {
		windCharge(helper, false);
	}

	/** Where each test player's outgoing packets end up, to read what the server sent them. */
	private static final java.util.Map<Object, io.netty.channel.embedded.EmbeddedChannel> CHANNELS = java.util.Collections.synchronizedMap(new java.util.IdentityHashMap<>());

	/** A plain server player in the player list; the helper's mock player always says it isn't a spectator. */
	private static net.minecraft.server.level.ServerPlayer realPlayer(GameTestHelper helper, String name) {
		var server = helper.getLevel().getServer();
		var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), name), false);
		var player = new net.minecraft.server.level.ServerPlayer(server, helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
		var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
		CHANNELS.put(player, new io.netty.channel.embedded.EmbeddedChannel(connection));
		server.getPlayerList().placeNewPlayer(connection, player, cookie);
		//? if >=1.21.11 {
		/*// a mock player's client never reports it has loaded, which makes it immune to all damage
		player.connection.handleAcceptPlayerLoad(new net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket());
		*///?}
		return player;
	}

	private static void spectator(GameTestHelper helper, boolean fixed) {
		var server = helper.getLevel().getServer();
		var player = realPlayer(helper, "test-spectator");
		player.setGameMode(net.minecraft.world.level.GameType.SPECTATOR);
		helper.assertTrue(player.isSpectator(), "the mock player did not switch to spectator mode");
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		float before = player.getHealth();
		//? if >=1.21.11 {
		/*player.hurtServer(helper.getLevel(), player.damageSources().fellOutOfWorld(), 4.0F);
		*///?} else
		player.hurt(player.damageSources().fellOutOfWorld(), 4.0F);
		float after = player.getHealth();
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		server.getPlayerList().remove(player);
		if (fixed) {
			helper.assertTrue(after == before, "a spectator took void damage: " + before + " -> " + after);
		} else {
			helper.assertTrue(after < before, "a spectator took no void damage without the fix; is MC-219854 fixed?");
		}
		helper.succeed();
	}

	/** MC-219854: a spectator below the world takes no void damage. */
	public static void spectatorIgnoresVoid(GameTestHelper helper) {
		spectator(helper, true);
	}

	/** Without the fix the void hurts spectators. */
	public static void spectatorIgnoresVoidVanilla(GameTestHelper helper) {
		spectator(helper, false);
	}

	private static void brokenName(GameTestHelper helper, boolean fixed) {
		//? if <1.21.11 {
		String name = fixed ? "[{broken name, fixed run" : "[{broken name, vanilla run";
		if (!fixed) LongStandingFixes.VANILLA_NAMES.add(name);
		int before = LongStandingFixes.customNameWarnings.get();
		// what a spawner does on every attempt: build its entity from the stored data
		for (int i = 0; i < 10; i++) {
			var tag = new net.minecraft.nbt.CompoundTag();
			tag.putString("id", "minecraft:armor_stand");
			tag.putString("CustomName", name);
			net.minecraft.world.entity.EntityType.create(tag, helper.getLevel());
		}
		int logged = LongStandingFixes.customNameWarnings.get() - before;
		LongStandingFixes.VANILLA_NAMES.remove(name);
		if (fixed) helper.assertTrue(logged == 1, "the broken name was logged " + logged + " times");
		else helper.assertTrue(logged == 10, "vanilla logged the broken name " + logged + " times, expected 10; is MC-168976 fixed?");
		//?}
		helper.succeed();
	}

	/** MC-168976 (1.21.1): building an entity with a broken custom name ten times logs one warning. */
	public static void brokenNameLoggedOnce(GameTestHelper helper) {
		brokenName(helper, true);
	}

	/** Without the fix every attempt logs a warning with a stack trace. */
	public static void brokenNameLoggedOnceVanilla(GameTestHelper helper) {
		brokenName(helper, false);
	}

	private static void creeper(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var player = realPlayer(helper, "test-igniter");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		//? if >=26 {
		/*net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(net.minecraft.world.entity.EntityTypes.CREEPER, new BlockPos(2, 1, 2));
		*///?} else
		net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(EntityType.CREEPER, new BlockPos(2, 1, 2));
		creeper.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(creeper.getUUID());
		ItemStack flint = new ItemStack(Items.FLINT_AND_STEEL);
		player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, flint);
		click(player, creeper);
		click(player, creeper);
		int damage = flint.getDamageValue();
		ItemStack charges = new ItemStack(Items.FIRE_CHARGE, 5);
		player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, charges);
		click(player, creeper);
		int left = charges.getCount();
		boolean lit = creeper.isIgnited();
		LongStandingFixes.VANILLA_ENTITIES.remove(creeper.getUUID());
		creeper.discard();
		helper.getLevel().getServer().getPlayerList().remove(player);
		helper.assertTrue(lit, "the creeper was not lit by flint and steel");
		if (fixed) helper.assertTrue(damage == 1 && left == 5, "lighting once cost " + damage + " durability, and a click on the lit creeper left " + left + " of 5 fire charges");
		else helper.assertTrue(damage == 2 && left == 4, "vanilla cost " + damage + " durability and left " + left + " fire charges, expected 2 and 4; is MC-186909 fixed?");
		helper.succeed();
	}

	private static void click(net.minecraft.server.level.ServerPlayer player, net.minecraft.world.entity.Entity target) {
		//? if >=26 {
		/*player.interactOn(target, net.minecraft.world.InteractionHand.MAIN_HAND, net.minecraft.world.phys.Vec3.ZERO);
		*///?} else
		player.interactOn(target, net.minecraft.world.InteractionHand.MAIN_HAND);
	}

	/** MC-186909: lighting a creeper costs one use of flint and steel; clicking it again while lit costs nothing. */
	public static void creeperLitOnce(GameTestHelper helper) {
		creeper(helper, true);
	}

	/** Without the fix every click on the lit creeper costs durability or a fire charge. */
	public static void creeperLitOnceVanilla(GameTestHelper helper) {
		creeper(helper, false);
	}

	private static final int FALL_ROUNDS = 5;

	private static void falling(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 8; x++) for (int z = 0; z < 8; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		java.util.Random random = new java.util.Random(261789);
		int[] placed = {0}, broken = {0};
		fallRound(helper, fixed, random, 0, placed, broken);
	}

	/**
	 * 36 sand blocks land fast from random heights; count how many become blocks and how many break. The error needs a drop
	 * across a power of two, like the bug report's y=1.2 down to -7: the sand lands on a stone platform at y=-8 (top -7, as
	 * in the report), with the columns above it cleared.
	 */
	private static void fallRound(GameTestHelper helper, boolean fixed, java.util.Random random, int round, int[] placed, int[] broken) {
		var level = helper.getLevel();
		// stay below the test area's ceiling when it is above the platform (26.3 puts tests at y=6)
		int ceiling = helper.absolutePos(new BlockPos(0, 8, 0)).getY();
		int top = -7;
		double low = top + 2, high = ceiling > top ? Math.min(15, ceiling - 2) : 15;
		java.util.List<BlockPos> columns = new java.util.ArrayList<>();
		for (int x = 1; x < 7; x++) for (int z = 1; z < 7; z++) {
			BlockPos c = helper.absolutePos(new BlockPos(x, 1, z));
			columns.add(new BlockPos(c.getX(), top, c.getZ()));
		}
		for (BlockPos c : columns) {
			level.setBlockAndUpdate(c.below(), Blocks.STONE.defaultBlockState());
			for (int y = top; y <= high + 1; y++) level.setBlockAndUpdate(c.atY(y), Blocks.AIR.defaultBlockState());
		}
		java.util.List<net.minecraft.world.entity.item.FallingBlockEntity> blocks = new java.util.ArrayList<>();
		for (BlockPos c : columns) {
			// the first column replays the bug report exactly
			double y = c == columns.get(0) ? 1.2074466562545139 : low + (high - low) * random.nextDouble();
			BlockPos start = new BlockPos(c.getX(), (int) Math.floor(y), c.getZ());
			var sand = net.minecraft.world.entity.item.FallingBlockEntity.fall(level, start, Blocks.SAND.defaultBlockState());
			sand.setPos(c.getX() + 0.5, y, c.getZ() + 0.5);
			sand.setDeltaMovement(0, -10, 0);
			if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(sand.getUUID());
			blocks.add(sand);
		}
		helper.runAfterDelay(8, () -> {
			var first = columns.get(0);
			var area = new net.minecraft.world.phys.AABB(first.getX() - 1, top - 2, first.getZ() - 1, first.getX() + 7, top + 3, first.getZ() + 7);
			for (BlockPos c : columns) {
				if (level.getBlockState(c).is(Blocks.SAND)) placed[0]++;
				level.setBlockAndUpdate(c, Blocks.AIR.defaultBlockState());
				level.setBlockAndUpdate(c.below(), Blocks.AIR.defaultBlockState());
			}
			for (var item : level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, area)) {
				if (item.getItem().is(Items.SAND)) broken[0] += item.getItem().getCount();
				item.discard();
			}
			for (var sand : blocks) {
				LongStandingFixes.VANILLA_ENTITIES.remove(sand.getUUID());
				sand.discard();
			}
			if (round + 1 < FALL_ROUNDS) {
				fallRound(helper, fixed, random, round + 1, placed, broken);
				return;
			}
			System.out.printf("[long_standing_fixes] MC-261789 %s, landing at y=%d: %d landed as blocks, %d broke%n",
				fixed ? "fixed" : "vanilla", top, placed[0], broken[0]);
			int total = 36 * FALL_ROUNDS;
			if (fixed) helper.assertTrue(placed[0] == total && broken[0] == 0, placed[0] + " of " + total + " sand blocks landed, " + broken[0] + " broke");
			else helper.assertTrue(broken[0] > 0, "no sand broke without the fix (" + placed[0] + " of " + total + " landed); is MC-261789 fixed?");
			helper.succeed();
		});
	}

	/** MC-261789: sand that lands fast below Y=0 always turns into a block. */
	public static void fallingBlocksLand(GameTestHelper helper) {
		falling(helper, true);
	}

	/** Without the fix some of it lands a hair too low and breaks into items. */
	public static void fallingBlocksLandVanilla(GameTestHelper helper) {
		falling(helper, false);
	}

	private static boolean done(net.minecraft.server.level.ServerPlayer player, String id) {
		for (var advancement : ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getAdvancements().getAllAdvancements())
			if (advancement.id().toString().equals(id)) return player.getAdvancements().getOrStartProgress(advancement).isDone();
		throw new IllegalStateException("no advancement " + id);
	}

	private static void axe(net.minecraft.server.level.ServerPlayer player, BlockPos pos) {
		player.getMainHandItem().useOn(new net.minecraft.world.item.context.UseOnContext(player, net.minecraft.world.InteractionHand.MAIN_HAND,
			new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos), Direction.UP, pos, false)));
	}

	/** A vanilla block by id; the copper bulbs moved into a collection in 26.x. */
	private static net.minecraft.world.level.block.Block block(String path) {
		for (var block : net.minecraft.core.registries.BuiltInRegistries.BLOCK)
			if (net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath().equals(path)) return block;
		throw new IllegalStateException("no block " + path);
	}

	private static void lightenUp(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 6; x++) for (int z = 0; z < 4; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var lit = net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;
		BlockPos waxed = new BlockPos(1, 1, 1), oxidized = new BlockPos(4, 1, 1);
		helper.setBlock(waxed, block("waxed_oxidized_copper_bulb").defaultBlockState().setValue(lit, true));
		helper.setBlock(oxidized, block("oxidized_copper_bulb").defaultBlockState().setValue(lit, true));
		var player = realPlayer(helper, "test-scraper");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
		axe(player, helper.absolutePos(waxed));
		boolean unwaxed = helper.getLevel().getBlockState(helper.absolutePos(waxed)).is(block("oxidized_copper_bulb"));
		boolean afterWax = done(player, "minecraft:adventure/lighten_up"), waxOff = done(player, "minecraft:husbandry/wax_off");
		axe(player, helper.absolutePos(oxidized));
		boolean afterScrape = done(player, "minecraft:adventure/lighten_up");
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		helper.getLevel().getServer().getPlayerList().remove(player);
		helper.assertTrue(unwaxed, "the axe did not take the wax off the bulb");
		if (fixed) helper.assertTrue(!afterWax && waxOff && afterScrape, "Lighten Up after wax off: " + afterWax + ", Wax Off: " + waxOff + ", Lighten Up after scraping: " + afterScrape);
		else helper.assertTrue(afterWax, "vanilla did not award Lighten Up for taking wax off a bulb; is MC-269636 fixed?");
		helper.succeed();
	}

	/** MC-269636: taking the wax off a lit bulb gives "Wax Off" but not "Lighten Up"; scraping a lit bulb still gives it. */
	public static void lightenUpNotForWax(GameTestHelper helper) {
		lightenUp(helper, true);
	}

	/** Without the fix the wax alone awards Lighten Up. */
	public static void lightenUpNotForWaxVanilla(GameTestHelper helper) {
		lightenUp(helper, false);
	}

	private static boolean sensorActive(GameTestHelper helper, BlockPos pos) {
		var state = helper.getBlockState(pos);
		return state.getValue(net.minecraft.world.level.block.SculkSensorBlock.PHASE) != net.minecraft.world.level.block.state.properties.SculkSensorPhase.INACTIVE;
	}

	private static void fireball(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 8; x++) for (int z = 0; z < 8; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		BlockPos sensor = new BlockPos(1, 1, 1);
		helper.setBlock(sensor, Blocks.SCULK_SENSOR);
		var player = realPlayer(helper, "test-batter");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(5.5, 1, 5.5));
		player.setPos(at.x, at.y, at.z);
		//? if >=26 {
		/*var fireball = helper.spawn(net.minecraft.world.entity.EntityTypes.FIREBALL, new BlockPos(4, 2, 4));
		*///?} else
		var fireball = helper.spawn(EntityType.FIREBALL, new BlockPos(4, 2, 4));
		fireball.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(fireball.getUUID());
		// let the sensor settle; tests running next to this one make noise too, so wait until it is quiet
		int damageFrequency = net.minecraft.world.level.gameevent.vibrations.VibrationSystem.getGameEventFrequency(net.minecraft.world.level.gameevent.GameEvent.ENTITY_DAMAGE);
		Runnable[] attempt = new Runnable[1];
		int[] waited = {0};
		attempt[0] = () -> {
			if (sensorActive(helper, sensor) && waited[0]++ < 200) {
				helper.runAfterDelay(1, attempt[0]);
				return;
			}
			helper.assertTrue(!sensorActive(helper, sensor), "the sculk sensor never went quiet before the hit");
			player.attack(fireball);
			// gone before it can fly into anything and explode, which sensors would hear anyway
			fireball.discard();
			helper.runAfterDelay(10, () -> {
				var be = (net.minecraft.world.level.block.entity.SculkSensorBlockEntity) helper.getLevel().getBlockEntity(helper.absolutePos(sensor));
				// a neighbouring test can set the sensor off too; only a hit (entity damage) vibration counts
				boolean heard = sensorActive(helper, sensor) && be.getLastVibrationFrequency() == damageFrequency;
				LongStandingFixes.VANILLA_ENTITIES.remove(fireball.getUUID());
				helper.getLevel().getServer().getPlayerList().remove(player);
				if (fixed) helper.assertTrue(heard, "the sculk sensor did not hear the fireball being hit");
				else helper.assertTrue(!heard, "vanilla's sculk sensor heard the fireball being hit; is MC-210491 fixed?");
				helper.succeed();
			});
		};
		helper.runAfterDelay(60, attempt[0]);
	}

	/** MC-210491: a sculk sensor hears a player batting a fireball away. */
	public static void sculkHearsBattedFireball(GameTestHelper helper) {
		fireball(helper, true);
	}

	/** Without the fix it doesn't. */
	public static void sculkHearsBattedFireballVanilla(GameTestHelper helper) {
		fireball(helper, false);
	}

	private static void catChest(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 8; x++) for (int z = 0; z < 8; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		BlockPos lower = new BlockPos(3, 1, 3), upper = new BlockPos(3, 2, 3);
		helper.setBlock(lower, Blocks.CHEST);
		helper.setBlock(upper, Blocks.CHEST);
		if (!fixed) LongStandingFixes.VANILLA_BLOCKS.add(helper.absolutePos(lower));
		//? if >=26 {
		/*var cat = helper.spawn(net.minecraft.world.entity.EntityTypes.CAT, new net.minecraft.world.phys.Vec3(3.5, 2.875, 3.5));
		*///?} else
		var cat = helper.spawn(EntityType.CAT, new net.minecraft.world.phys.Vec3(3.5, 2.875, 3.5));
		cat.setNoAi(true);
		cat.setNoGravity(true);
		cat.setInSittingPose(true);
		helper.runAfterDelay(5, () -> {
			boolean upperBlocked = net.minecraft.world.level.block.ChestBlock.isChestBlockedAt(helper.getLevel(), helper.absolutePos(upper));
			boolean lowerBlocked = net.minecraft.world.level.block.ChestBlock.isChestBlockedAt(helper.getLevel(), helper.absolutePos(lower));
			LongStandingFixes.VANILLA_BLOCKS.remove(helper.absolutePos(lower));
			cat.discard();
			helper.assertTrue(upperBlocked, "the cat does not keep the chest it sits on shut");
			if (fixed) helper.assertTrue(!lowerBlocked, "the chest below the cat's chest is still blocked");
			else helper.assertTrue(lowerBlocked, "vanilla lets the chest below open; is MC-265635 fixed?");
			helper.succeed();
		});
	}

	/** MC-265635: a cat on the upper of two stacked chests only keeps the upper one shut. */
	public static void catBlocksOnlyItsChest(GameTestHelper helper) {
		catChest(helper, true);
	}

	/** Without the fix the lower chest is blocked too. */
	public static void catBlocksOnlyItsChestVanilla(GameTestHelper helper) {
		catChest(helper, false);
	}

	private static void dispenserPortal(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 8; x++) for (int z = 0; z < 8; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		// a 5x5 obsidian frame standing in the x-y plane at z = 3, with a 3x3 opening
		for (int x = 1; x <= 5; x++) for (int y = 1; y <= 5; y++)
			if (x == 1 || x == 5 || y == 1 || y == 5) helper.setBlock(new BlockPos(x, y, 3), Blocks.OBSIDIAN);
		BlockPos middle = new BlockPos(3, 3, 3), dispenser = new BlockPos(3, 3, 2);
		if (!fixed) LongStandingFixes.VANILLA_BLOCKS.add(helper.absolutePos(middle));
		helper.setBlock(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(net.minecraft.world.level.block.DispenserBlock.FACING, Direction.SOUTH));
		((net.minecraft.world.Container) be(helper, dispenser)).setItem(0, new ItemStack(Items.FLINT_AND_STEEL));
		helper.runAfterDelay(2, () -> helper.setBlock(dispenser.above(), Blocks.REDSTONE_BLOCK));
		helper.runAfterDelay(10, () -> {
			boolean lit = helper.getLevel().getBlockState(helper.absolutePos(middle)).is(Blocks.NETHER_PORTAL);
			LongStandingFixes.VANILLA_BLOCKS.remove(helper.absolutePos(middle));
			for (int x = 2; x <= 4; x++) for (int y = 2; y <= 4; y++) helper.setBlock(new BlockPos(x, y, 3), Blocks.AIR);
			if (fixed) helper.assertTrue(lit, "the dispenser did not light the portal through its middle");
			else helper.assertTrue(!lit, "vanilla's dispenser lit the portal through its middle; is MC-135198 fixed?");
			helper.succeed();
		});
	}

	/** MC-135198: a dispenser lights a 5x5 portal through the middle of the opening. */
	public static void dispenserLightsPortal(GameTestHelper helper) {
		dispenserPortal(helper, true);
	}

	/** Without the fix it can't. */
	public static void dispenserLightsPortalVanilla(GameTestHelper helper) {
		dispenserPortal(helper, false);
	}

	/**
	 * An item, a primed TNT and a minecart slide down the sides of a honey column; their fall speed is measured over ten
	 * ticks once they are sliding. 1.21.1 lets them slide at about 0.09 blocks a tick; since 1.21.2 it is about 0.16.
	 */
	private static void honey(GameTestHelper helper, boolean fixed) {
		var level = helper.getLevel();
		BlockPos column = new BlockPos(3, 1, 3);
		for (int x = 1; x < 6; x++) for (int z = 1; z < 6; z++) for (int y = 1; y <= 8; y++)
			helper.setBlock(new BlockPos(x, y, z), x == 3 && z == 3 && y <= 7 ? Blocks.HONEY_BLOCK : Blocks.AIR);
		var base = helper.absoluteVec(net.minecraft.world.phys.Vec3.atLowerCornerOf(column));
		double y = base.y + 6;
		var item = new net.minecraft.world.entity.item.ItemEntity(level, base.x + 0.9375 + 0.125 + 1.0E-4, y, base.z + 0.5, new ItemStack(Items.DIAMOND));
		item.setPickUpDelay(400);
		var tnt = new net.minecraft.world.entity.item.PrimedTnt(level, base.x + 0.0625 - 0.49 - 1.0E-4, y, base.z + 0.5, null);
		tnt.setFuse(400);
		//? if >=26 {
		/*var cart = net.minecraft.world.entity.EntityTypes.MINECART.create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
		*///?} else if >=1.21.11 {
		/*var cart = EntityType.MINECART.create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
		*///?} else
		var cart = EntityType.MINECART.create(level);
		cart.setPos(base.x + 0.5, y, base.z + 0.9375 + 0.49 + 1.0E-4);
		java.util.List<net.minecraft.world.entity.Entity> sliders = java.util.List.of(item, tnt, cart);
		for (var e : sliders) {
			e.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
			if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(e.getUUID());
			level.addFreshEntity(e);
		}
		double[] from = new double[3];
		helper.runAfterDelay(20, () -> {
			for (int i = 0; i < 3; i++) from[i] = sliders.get(i).getY();
		});
		helper.runAfterDelay(30, () -> {
			String[] names = {"item", "TNT", "minecart"};
			StringBuilder speeds = new StringBuilder();
			boolean allOld = true, allNew = true;
			for (int i = 0; i < 3; i++) {
				var e = sliders.get(i);
				double speed = (from[i] - e.getY()) / 10;
				speeds.append(names[i]).append(String.format(" %.4f ", speed));
				allOld &= speed > 0.07 && speed < 0.11;
				allNew &= speed > 0.13;
				LongStandingFixes.VANILLA_ENTITIES.remove(e.getUUID());
				e.discard();
			}
			System.out.printf("[long_standing_fixes] MC-278572 %s: %s(blocks a tick)%n", fixed ? "fixed" : "vanilla", speeds);
			//? if >=1.21.11 {
			/*boolean bug = !fixed;
			*///?} else
			boolean bug = false; // 1.21.1 never had it
			if (bug) helper.assertTrue(allNew, "without the fix the slide speeds were " + speeds + "; is MC-278572 fixed?");
			else helper.assertTrue(allOld, "slide speeds were " + speeds + ", expected about 0.09");
			helper.succeed();
		});
	}

	/** MC-278572: items, TNT and minecarts slide down honey at the 1.21.1 speed. */
	public static void honeySlideSpeed(GameTestHelper helper) {
		honey(helper, true);
	}

	/** Without the fix they slide almost twice as fast (on 1.21.1 there's nothing to fix). */
	public static void honeySlideSpeedVanilla(GameTestHelper helper) {
		honey(helper, false);
	}

	/**
	 * A wind charge hangs in the air next to an explosion that breaks no blocks; read its speed straight after the blast
	 * (moving it would let it fly into the next test's area and burst).
	 */
	private static void windChargeBlast(GameTestHelper helper, boolean fixed) {
		//? if >=1.21.11 {
		/*for (int x = 0; x < 7; x++) for (int z = 0; z < 5; z++) for (int y = 1; y < 5; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
		var level = helper.getLevel();
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(3.5, 2.5, 2.5));
		var charge = new net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge(level, at.x, at.y, at.z, net.minecraft.world.phys.Vec3.ZERO);
		charge.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(charge.getUUID());
		level.addFreshEntity(charge);
		helper.runAfterDelay(2, () -> {
			var before = charge.getDeltaMovement();
			level.explode(null, at.x - 1.5, at.y, at.z, 3.0F, net.minecraft.world.level.Level.ExplosionInteraction.NONE);
			double speed = charge.getDeltaMovement().subtract(before).length();
			LongStandingFixes.VANILLA_ENTITIES.remove(charge.getUUID());
			charge.discard();
			System.out.printf("[long_standing_fixes] MC-279003 %s: the blast gave the wind charge %.3f blocks a tick%n", fixed ? "fixed" : "vanilla", speed);
			if (fixed) helper.assertTrue(speed > 0.3, "the explosion gave the wind charge only " + speed + " blocks a tick");
			else helper.assertTrue(speed < 0.01, "the explosion gave the wind charge " + speed + " blocks a tick without the fix; is MC-279003 fixed?");
			helper.succeed();
		});
		*///?} else {
		// on 1.21.1 explosions still move wind charges; nothing to fix
		helper.succeed();
		//?}
	}

	/** MC-279003: an explosion pushes a wind charge away. */
	public static void explosionPushesWindCharge(GameTestHelper helper) {
		windChargeBlast(helper, true);
	}

	/** Without the fix the wind charge stays where it is. */
	public static void explosionPushesWindChargeVanilla(GameTestHelper helper) {
		windChargeBlast(helper, false);
	}

	/** A zombie hits a player who holds up a shield; see whether the zombie bounces off. */
	private static void shield(GameTestHelper helper, boolean fixed) {
		//? if <1.21.5 {
		for (int x = 0; x < 6; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var level = helper.getLevel();
		var player = realPlayer(helper, fixed ? "test-shield" : "test-shield-vanilla");
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(1.5, 1, 2.5));
		player.teleportTo(level, at.x, at.y, at.z, -90.0F, 0.0F);
		player.setYHeadRot(-90.0F);
		// the test server may hand out creative mode, where nothing hurts the player
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		// newly joined players can't be hurt for 60 ticks
		for (int i = 0; i < 61; i++) player.tick();
		player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, new ItemStack(Items.SHIELD));
		player.startUsingItem(net.minecraft.world.InteractionHand.OFF_HAND);
		// a shield only blocks after it has been up for 5 ticks
		for (int i = 0; i < 6; i++) player.doTick();
		helper.assertTrue(player.isBlocking(), "the player is not blocking with the shield");
		var zombie = helper.spawn(EntityType.ZOMBIE, new BlockPos(3, 1, 2));
		zombie.setNoAi(true);
		zombie.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(zombie.getUUID());
		float health = player.getHealth();
		// mob hits do nothing to players on peaceful, which the test server may run on; switch for this one hit
		var server = level.getServer();
		var difficulty = server.getWorldData().getDifficulty();
		server.setDifficulty(net.minecraft.world.Difficulty.NORMAL, true);
		// the fix is off by default; switch it on for this hit only
		boolean enabled = LongStandingFixes.shieldsRepelMobs;
		LongStandingFixes.shieldsRepelMobs = true;
		player.hurt(player.damageSources().mobAttack(zombie), 4.0F);
		LongStandingFixes.shieldsRepelMobs = enabled;
		server.setDifficulty(difficulty, true);
		var push = zombie.getDeltaMovement();
		LongStandingFixes.VANILLA_ENTITIES.remove(zombie.getUUID());
		level.getServer().getPlayerList().remove(player);
		zombie.discard();
		System.out.printf("[long_standing_fixes] MC-147694 %s: zombie pushed %.3f, %.3f; player health %.1f -> %.1f%n", fixed ? "fixed" : "vanilla", push.x, push.z, health, player.getHealth());
		helper.assertTrue(player.getHealth() == health, "the shield did not block the hit");
		// the zombie stands east of the player, so away from the shield is +x
		if (fixed) helper.assertTrue(push.x > 0.3, "the zombie was not knocked back: " + push);
		else helper.assertTrue(push.horizontalDistance() < 0.01, "the zombie was knocked back without the fix: " + push + "; is MC-147694 fixed?");
		helper.succeed();
		//?} else {
		/*// shields were reworked in 1.21.5; the fix is for 1.21.1 only
		helper.succeed();
		*///?}
	}

	/** MC-147694: a mob that hits a raised shield is knocked back. */
	public static void shieldRepelsMob(GameTestHelper helper) {
		shield(helper, true);
	}

	/** Without the fix the mob stays put. */
	public static void shieldRepelsMobVanilla(GameTestHelper helper) {
		shield(helper, false);
	}

	/** A trade nobody uses restocks 100 times; its demand should end at 0, not at -1200. */
	private static void demand(GameTestHelper helper, boolean fixed) {
		var offer = new net.minecraft.world.item.trading.MerchantOffer(new net.minecraft.world.item.trading.ItemCost(Items.EMERALD, 1), new ItemStack(Items.BREAD), 12, 1, 0.05F);
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(offer);
		// the fix is off by default; switch it on for this test only
		boolean enabled = LongStandingFixes.villagerDemand;
		LongStandingFixes.villagerDemand = true;
		for (int i = 0; i < 100; i++) offer.updateDemand();
		int idle = offer.getDemand();
		// then it sells out once; the next restock should raise the demand (and with it the price)
		for (int i = 0; i < 12; i++) offer.increaseUses();
		offer.updateDemand();
		int busy = offer.getDemand();
		LongStandingFixes.villagerDemand = enabled;
		LongStandingFixes.VANILLA_OBJECTS.remove(offer);
		System.out.printf("[long_standing_fixes] MC-163962 %s: demand %d after 100 idle restocks, %d after selling out%n", fixed ? "fixed" : "vanilla", idle, busy);
		if (fixed) helper.assertTrue(idle == 0 && busy == 12, "demand was " + idle + " after idle restocks and " + busy + " after selling out");
		else helper.assertTrue(idle == -1200 && busy < 0, "vanilla demand was " + idle + " and " + busy + "; is MC-163962 fixed?");
		helper.succeed();
	}

	/** MC-163962: an unused trade's demand stops at 0. */
	public static void villagerDemandStopsAtZero(GameTestHelper helper) {
		demand(helper, true);
	}

	/** Without the fix it keeps falling. */
	public static void villagerDemandStopsAtZeroVanilla(GameTestHelper helper) {
		demand(helper, false);
	}

	/** Light a creeper with an unbreakable flint and steel. */
	private static void unbreakableIgniter(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var player = realPlayer(helper, fixed ? "test-unbreakable" : "test-unbreakable-vanilla");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		//? if >=26 {
		/*net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(net.minecraft.world.entity.EntityTypes.CREEPER, new BlockPos(2, 1, 2));
		*///?} else
		net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(EntityType.CREEPER, new BlockPos(2, 1, 2));
		creeper.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(creeper.getUUID());
		ItemStack flint = new ItemStack(Items.FLINT_AND_STEEL);
		//? if >=1.21.11 {
		/*flint.set(net.minecraft.core.component.DataComponents.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
		*///?} else
		flint.set(net.minecraft.core.component.DataComponents.UNBREAKABLE, new net.minecraft.world.item.component.Unbreakable(true));
		player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, flint);
		click(player, creeper);
		boolean lit = creeper.isIgnited();
		ItemStack held = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
		LongStandingFixes.VANILLA_ENTITIES.remove(creeper.getUUID());
		creeper.discard();
		helper.getLevel().getServer().getPlayerList().remove(player);
		System.out.printf("[long_standing_fixes] MC-264285 %s: lit %s, flint and steel left: %s%n", fixed ? "fixed" : "vanilla", lit, held);
		helper.assertTrue(lit, "the creeper was not lit");
		if (fixed) helper.assertTrue(held.is(Items.FLINT_AND_STEEL) && held.getDamageValue() == 0, "the unbreakable flint and steel became " + held);
		else helper.assertTrue(held.isEmpty(), "vanilla kept the unbreakable flint and steel (" + held + "); is MC-264285 fixed?");
		helper.succeed();
	}

	/** MC-264285: an unbreakable flint and steel survives lighting a creeper. */
	public static void unbreakableFlintAndSteelKept(GameTestHelper helper) {
		unbreakableIgniter(helper, true);
	}

	/** Without the fix it is used up. */
	public static void unbreakableFlintAndSteelKeptVanilla(GameTestHelper helper) {
		unbreakableIgniter(helper, false);
	}

	/** Moves a test player within its level. */
	private static void place(net.minecraft.server.level.ServerPlayer player, double x, double y, double z, float yRot) {
		//? if >=1.21.11 {
		/*player.snapTo(x, y, z, yRot, 0.0F);
		*///?} else
		player.moveTo(x, y, z, yRot, 0.0F);
	}

	/** A creeper starts to swell next to a survival player, who then switches to creative. */
	private static void defuse(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 6; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			for (int y = 1; y < 4; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
		}
		var player = realPlayer(helper, fixed ? "test-defuse" : "test-defuse-vanilla");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(1.5, 1, 2.5));
		place(player, at.x, at.y, at.z, -90.0F);
		//? if >=26 {
		/*net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(net.minecraft.world.entity.EntityTypes.CREEPER, new BlockPos(3, 1, 2));
		*///?} else
		net.minecraft.world.entity.monster.Creeper creeper = helper.spawn(EntityType.CREEPER, new BlockPos(3, 1, 2));
		creeper.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(creeper.getUUID());
		creeper.setTarget(player);
		var goal = new net.minecraft.world.entity.ai.goal.SwellGoal(creeper);
		goal.start();
		goal.tick();
		int before = creeper.getSwellDir();
		player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
		goal.tick();
		int after = creeper.getSwellDir();
		LongStandingFixes.VANILLA_ENTITIES.remove(creeper.getUUID());
		creeper.discard();
		helper.getLevel().getServer().getPlayerList().remove(player);
		System.out.printf("[long_standing_fixes] MC-179072 %s: swelling %d in survival, %d after switching to creative%n", fixed ? "fixed" : "vanilla", before, after);
		helper.assertTrue(before == 1, "the creeper did not start to swell next to a survival player");
		if (fixed) helper.assertTrue(after == -1, "the creeper kept swelling after the player switched to creative");
		else helper.assertTrue(after == 1, "vanilla creeper stopped swelling (" + after + "); is MC-179072 fixed?");
		helper.succeed();
	}

	/** MC-179072: a creeper stops swelling when its player switches to creative. */
	public static void creeperDefusesForCreative(GameTestHelper helper) {
		defuse(helper, true);
	}

	/** Without the fix it keeps swelling. */
	public static void creeperDefusesForCreativeVanilla(GameTestHelper helper) {
		defuse(helper, false);
	}

	/** A spectator stands under open sky; ask where lightning aimed at their spot would strike. */
	private static void lightning(GameTestHelper helper, boolean fixed) {
		var player = realPlayer(helper, fixed ? "test-lightning" : "test-lightning-vanilla");
		player.setGameMode(net.minecraft.world.level.GameType.SPECTATOR);
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		var level = helper.getLevel();
		var spot = helper.absolutePos(new BlockPos(2, 1, 2));
		int top = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, spot.getX(), spot.getZ());
		// above everything, so the spectator sees the sky
		place(player, spot.getX() + 0.5, top + 3, spot.getZ() + 0.5, 0.0F);
		BlockPos aim = new BlockPos(spot.getX(), top, spot.getZ());
		BlockPos hit = ((io.github.traelmyx.longstandingfixes.mixin.LightningTargetInvoker) level).longstandingfixes$findLightningTargetAround(aim);
		boolean atPlayer = hit.equals(player.blockPosition());
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		level.getServer().getPlayerList().remove(player);
		System.out.printf("[long_standing_fixes] MC-262422 %s: lightning aimed at %s, spectator at %s%n", fixed ? "fixed" : "vanilla", hit, player.blockPosition());
		if (fixed) helper.assertTrue(!atPlayer, "lightning aimed at the spectator");
		else helper.assertTrue(atPlayer, "vanilla lightning did not aim at the spectator; is MC-262422 fixed?");
		helper.succeed();
	}

	/** MC-262422: thunderstorm lightning doesn't aim at spectators. */
	public static void lightningSkipsSpectators(GameTestHelper helper) {
		lightning(helper, true);
	}

	/** Without the fix it picks the spectator. */
	public static void lightningSkipsSpectatorsVanilla(GameTestHelper helper) {
		lightning(helper, false);
	}

	private static void setMobLoot(net.minecraft.server.level.ServerLevel level, boolean value) {
		//? if >=1.21.11 {
		/*level.getGameRules().set(net.minecraft.world.level.gamerules.GameRules.MOB_DROPS, value, level.getServer());
		*///?} else
		level.getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT).set(value, level.getServer());
	}

	/** A fox holding an emerald dies with doMobLoot off (switched off only for that moment). */
	private static void fox(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var level = helper.getLevel();
		//? if >=26 {
		/*var fox = helper.spawn(net.minecraft.world.entity.EntityTypes.FOX, new BlockPos(2, 1, 2));
		*///?} else
		var fox = helper.spawn(EntityType.FOX, new BlockPos(2, 1, 2));
		fox.setNoAi(true);
		fox.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.EMERALD));
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(fox.getUUID());
		boolean rule = LongStandingFixes.mobLoot(level);
		setMobLoot(level, false);
		//? if >=1.21.11 {
		/*fox.hurtServer(level, level.damageSources().genericKill(), 1000.0F);
		*///?} else
		fox.hurt(level.damageSources().genericKill(), 1000.0F);
		setMobLoot(level, rule);
		var box = new net.minecraft.world.phys.AABB(helper.absolutePos(new BlockPos(0, 0, 0))).inflate(5);
		long emeralds = level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, box, e -> e.getItem().is(Items.EMERALD)).size();
		LongStandingFixes.VANILLA_ENTITIES.remove(fox.getUUID());
		level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, box, e -> e.getItem().is(Items.EMERALD)).forEach(net.minecraft.world.entity.Entity::discard);
		System.out.printf("[long_standing_fixes] MC-153010 %s: fox dead %s, dropped %d emeralds with doMobLoot off%n", fixed ? "fixed" : "vanilla", fox.isDeadOrDying(), emeralds);
		helper.assertTrue(fox.isDeadOrDying(), "the fox did not die");
		if (fixed) helper.assertTrue(emeralds == 0, "the fox dropped its emerald with doMobLoot off");
		else helper.assertTrue(emeralds == 1, "vanilla fox dropped " + emeralds + " emeralds; is MC-153010 fixed?");
		helper.succeed();
	}

	/** MC-153010: with doMobLoot off a fox drops nothing. */
	public static void foxFollowsDoMobLoot(GameTestHelper helper) {
		fox(helper, true);
	}

	/** Without the fix it drops what it carries. */
	public static void foxFollowsDoMobLootVanilla(GameTestHelper helper) {
		fox(helper, false);
	}

	private static void setMobGriefing(net.minecraft.server.level.ServerLevel level, boolean value) {
		//? if >=1.21.11 {
		/*level.getGameRules().set(net.minecraft.world.level.gamerules.GameRules.MOB_GRIEFING, value, level.getServer());
		*///?} else
		level.getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING).set(value, level.getServer());
	}

	/** A burning zombie stands in a full water cauldron while mobGriefing is off (only for that moment). */
	private static void cauldron(GameTestHelper helper, boolean fixed) {
		var level = helper.getLevel();
		BlockPos at = new BlockPos(2, 1, 2);
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		helper.setBlock(at, Blocks.WATER_CAULDRON.defaultBlockState().setValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL, 3));
		//? if >=26 {
		/*var zombie = helper.spawn(net.minecraft.world.entity.EntityTypes.ZOMBIE, at);
		*///?} else
		var zombie = helper.spawn(EntityType.ZOMBIE, at);
		zombie.setNoAi(true);
		var pos = helper.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 1.3, 2.5));
		zombie.setPos(pos.x, pos.y, pos.z);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(zombie.getUUID());
		zombie.igniteForSeconds(5);
		boolean rule = LongStandingFixes.mobGriefing(level);
		setMobGriefing(level, false);
		//? if >=1.21.11 {
		/*zombie.applyEffectsFromBlocks(zombie.position(), zombie.position());
		*///?} else
		zombie.move(net.minecraft.world.entity.MoverType.SELF, new net.minecraft.world.phys.Vec3(0, -0.01, 0));
		setMobGriefing(level, rule);
		int left = helper.getBlockState(at).hasProperty(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL) ? helper.getBlockState(at).getValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL) : 0;
		LongStandingFixes.VANILLA_ENTITIES.remove(zombie.getUUID());
		boolean burning = zombie.isOnFire();
		zombie.discard();
		System.out.printf("[long_standing_fixes] MC-248588 %s: cauldron level %d of 3 left, zombie still burning %s%n", fixed ? "fixed" : "vanilla", left, burning);
		if (fixed) helper.assertTrue(left == 3, "the burning zombie used up water with mobGriefing off: " + left + " of 3 left");
		else helper.assertTrue(left == 2, "vanilla left " + left + " of 3 levels; is MC-248588 fixed?");
		helper.succeed();
	}

	/** MC-248588: with mobGriefing off a burning mob leaves a cauldron's water alone. */
	public static void cauldronFollowsMobGriefing(GameTestHelper helper) {
		cauldron(helper, true);
	}

	/** Without the fix it uses up a level. */
	public static void cauldronFollowsMobGriefingVanilla(GameTestHelper helper) {
		cauldron(helper, false);
	}

	/** One player stabs another with a Knockback II spear; read the speed the server sends the victim's game. */
	private static void spear(GameTestHelper helper, boolean fixed) {
		//? if >=1.21.11 {
		/*for (int x = 0; x < 7; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			for (int y = 1; y < 4; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
		}
		var level = helper.getLevel();
		var attacker = realPlayer(helper, fixed ? "test-spear" : "test-spear-vanilla");
		var victim = realPlayer(helper, fixed ? "test-stabbed" : "test-stabbed-vanilla");
		attacker.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		victim.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		var a = helper.absoluteVec(new net.minecraft.world.phys.Vec3(1.5, 1, 2.5));
		place(attacker, a.x, a.y, a.z, -90.0F);
		place(victim, a.x + 2, a.y, a.z, 90.0F);
		// newly joined players can't be hurt for 60 ticks
		for (int i = 0; i < 61; i++) victim.tick();
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(victim);
		ItemStack spear = new ItemStack(Items.IRON_SPEAR);
		var knockback = level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(net.minecraft.world.item.enchantment.Enchantments.KNOCKBACK);
		spear.enchant(knockback, 2);
		attacker.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, spear);
		var channel = CHANNELS.get(victim);
		channel.runPendingTasks();
		channel.outboundMessages().clear();
		boolean hit = attacker.stabAttack(net.minecraft.world.entity.EquipmentSlot.MAINHAND, victim, 4.0F, true, true, false);
		channel.runPendingTasks();
		double sent = -1;
		int packets = 0;
		for (Object message : channel.outboundMessages())
			if (message instanceof net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket motion && motionId(motion) == victim.getId()) {
				packets++;
				sent = motionOf(motion).horizontalDistance();
			}
		LongStandingFixes.VANILLA_OBJECTS.remove(victim);
		level.getServer().getPlayerList().remove(attacker);
		level.getServer().getPlayerList().remove(victim);
		CHANNELS.remove(attacker);
		CHANNELS.remove(victim);
		System.out.printf("[long_standing_fixes] MC-310859 %s: hit %s, %d speed packets, last horizontal speed sent %.3f%n", fixed ? "fixed" : "vanilla", hit, packets, sent);
		helper.assertTrue(hit && packets > 0, "the stab did not reach the victim (hit " + hit + ", " + packets + " speed packets)");
		//? if >=26 {
		boolean bug = !fixed;
		//?} else
		/^boolean bug = false; // 1.21.11 pushes once with the sum^/
		if (!bug) helper.assertTrue(sent > 0.6, "the victim's game got a speed of only " + sent + "; Knockback II should add to the 0.4 push");
		else helper.assertTrue(Math.abs(sent - 0.4) < 0.05, "vanilla sent " + sent + ", expected the bare 0.4 push; is MC-310859 fixed?");
		helper.succeed();
		*///?} else {
		// spears arrived in 1.21.11
		helper.succeed();
		//?}
	}

	//? if >=1.21.11 {
	/*private static int motionId(net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket packet) {
		//? if >=26 {
		return packet.id();
		//?} else
		/^return packet.getId();^/
	}

	private static net.minecraft.world.phys.Vec3 motionOf(net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket packet) {
		//? if >=26 {
		return packet.movement();
		//?} else
		/^return packet.getMovement();^/
	}
	*///?}

	/** MC-310859: a Knockback spear knocks a player back further than a plain push. */
	public static void spearKnockbackOnPlayers(GameTestHelper helper) {
		spear(helper, true);
	}

	/** Without the fix the player only gets the plain 0.4 push. */
	public static void spearKnockbackOnPlayersVanilla(GameTestHelper helper) {
		spear(helper, false);
	}

	/**
	 * A wither skull kills a player carrying 64 stone and explodes, in that order and in the same tick, the way
	 * WitherSkull.onHit does it. A stack of dirt that was already lying there should still be blown up.
	 */
	private static void deathDrops(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.OBSIDIAN);
			for (int y = 1; y < 4; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
		}
		var level = helper.getLevel();
		var player = realPlayer(helper, fixed ? "test-skull" : "test-skull-vanilla");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 1, 2.5));
		place(player, at.x, at.y, at.z, 0.0F);
		for (int i = 0; i < 61; i++) player.tick();
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		player.getInventory().add(new ItemStack(Items.STONE, 64));
		var dirt = new net.minecraft.world.entity.item.ItemEntity(level, at.x + 0.5, at.y, at.z, new ItemStack(Items.DIRT, 64));
		dirt.setPickUpDelay(400);
		level.addFreshEntity(dirt);
		//? if >=26 {
		/*var skull = new net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull(net.minecraft.world.entity.EntityTypes.WITHER_SKULL, level);
		*///?} else if >=1.21.11 {
		/*var skull = new net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull(EntityType.WITHER_SKULL, level);
		*///?} else
		var skull = new net.minecraft.world.entity.projectile.WitherSkull(EntityType.WITHER_SKULL, level);
		skull.setPos(at.x, at.y + 1, at.z);
		//? if >=1.21.11 {
		/*player.hurtServer(level, player.damageSources().witherSkull(skull, null), 1000.0F);
		*///?} else
		player.hurt(player.damageSources().witherSkull(skull, null), 1000.0F);
		boolean died = player.isDeadOrDying();
		level.explode(skull, at.x, at.y + 1, at.z, 1.0F, false, net.minecraft.world.level.Level.ExplosionInteraction.MOB);
		var box = new net.minecraft.world.phys.AABB(at, at).inflate(4);
		int stone = 0, dirtLeft = 0;
		for (var item : level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class, box, e -> e.isAlive())) {
			if (item.getItem().is(Items.STONE)) stone += item.getItem().getCount();
			if (item.getItem().is(Items.DIRT)) dirtLeft += item.getItem().getCount();
			item.discard();
		}
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		level.getServer().getPlayerList().remove(player);
		CHANNELS.remove(player);
		System.out.printf("[long_standing_fixes] MC-164171 %s: player died %s, %d of 64 stone left, %d of 64 old dirt left%n", fixed ? "fixed" : "vanilla", died, stone, dirtLeft);
		helper.assertTrue(died, "the wither skull did not kill the player");
		helper.assertTrue(dirtLeft == 0, "the explosion did not destroy the item that was already on the ground");
		if (fixed) helper.assertTrue(stone == 64, "only " + stone + " of the 64 dropped stone survived the blast");
		else helper.assertTrue(stone == 0, "vanilla kept " + stone + " dropped stone; is MC-164171 fixed?");
		helper.succeed();
	}

	/** MC-164171: the blast of the skull that killed a player leaves the player's drops alone. */
	public static void killingBlastSparesDrops(GameTestHelper helper) {
		deathDrops(helper, true);
	}

	/** Without the fix the drops are destroyed. */
	public static void killingBlastSparesDropsVanilla(GameTestHelper helper) {
		deathDrops(helper, false);
	}

	/**
	 * A zombie with a +10 max health helmet and 30 health is saved and loaded again (a relog or chunk reload); after two
	 * ticks it should still have 30 health, not 20.
	 */
	private static void bonusHealth(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		var level = helper.getLevel();
		//? if >=26 {
		/*var type = net.minecraft.world.entity.EntityTypes.ZOMBIE;
		*///?} else
		var type = EntityType.ZOMBIE;
		var zombie = helper.spawn(type, new BlockPos(2, 1, 2));
		zombie.setNoAi(true);
		zombie.setPersistenceRequired();
		ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
		//? if >=1.21.11 {
		/*var id = net.minecraft.resources.Identifier.fromNamespaceAndPath(LongStandingFixes.MOD_ID, "test_health");
		*///?} else
		var id = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(LongStandingFixes.MOD_ID, "test_health");
		helmet.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, net.minecraft.world.item.component.ItemAttributeModifiers.builder()
			.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, new net.minecraft.world.entity.ai.attributes.AttributeModifier(id, 10.0,
				net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.HEAD).build());
		zombie.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, helmet);
		helper.runAfterDelay(2, () -> {
			zombie.setHealth(30.0F);
			float before = zombie.getHealth();
			//? if >=1.21.11 {
			/*var out = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess());
			zombie.saveWithoutId(out);
			var tag = out.buildResult();
			*///?} else {
			var tag = new net.minecraft.nbt.CompoundTag();
			zombie.saveWithoutId(tag);
			//?}
			tag.remove("UUID");
			var loaded = type.create(level
				//? if >=1.21.11
				/*, net.minecraft.world.entity.EntitySpawnReason.LOAD*/
			);
			if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(loaded);
			//? if >=1.21.11 {
			/*loaded.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), tag));
			*///?} else
			loaded.load(tag);
			zombie.discard();
			level.addFreshEntity(loaded);
			helper.runAfterDelay(2, () -> {
				float after = loaded.getHealth(), max = loaded.getMaxHealth();
				LongStandingFixes.VANILLA_OBJECTS.remove(loaded);
				loaded.discard();
				System.out.printf("[long_standing_fixes] MC-17876 %s: health %.1f before saving, %.1f of %.1f after loading%n", fixed ? "fixed" : "vanilla", before, after, max);
				helper.assertTrue(before == 30.0F && max == 30.0F, "the helmet did not raise max health to 30 (health " + before + ", max " + max + ")");
				if (fixed) helper.assertTrue(after == 30.0F, "the zombie came back with " + after + " health instead of 30");
				else helper.assertTrue(after == 20.0F, "vanilla zombie came back with " + after + " health; is MC-17876 fixed?");
				helper.succeed();
			});
		});
	}

	/** MC-17876: bonus health from gear survives saving and loading. */
	public static void bonusHealthSurvivesReload(GameTestHelper helper) {
		bonusHealth(helper, true);
	}

	/** Without the fix it is capped at 20. */
	public static void bonusHealthSurvivesReloadVanilla(GameTestHelper helper) {
		bonusHealth(helper, false);
	}

	private static void pearlCooldown(net.minecraft.server.level.ServerPlayer player, int ticks) {
		//? if >=1.21.11 {
		/*player.getCooldowns().addCooldown(new ItemStack(Items.ENDER_PEARL), ticks);
		*///?} else
		player.getCooldowns().addCooldown(Items.ENDER_PEARL, ticks);
	}

	private static boolean pearlOnCooldown(net.minecraft.server.level.ServerPlayer player) {
		//? if >=1.21.11 {
		/*return player.getCooldowns().isOnCooldown(new ItemStack(Items.ENDER_PEARL));
		*///?} else
		return player.getCooldowns().isOnCooldown(Items.ENDER_PEARL);
	}

	/** A player with an ender pearl cooldown is saved and their data loaded into a freshly joined player (a relog). */
	private static void cooldownRelog(GameTestHelper helper, boolean fixed) {
		var level = helper.getLevel();
		var before = realPlayer(helper, fixed ? "test-pearl" : "test-pearl-vanilla");
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(before);
		pearlCooldown(before, 200);
		//? if >=1.21.11 {
		/*var out = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess());
		before.saveWithoutId(out);
		var tag = out.buildResult();
		*///?} else {
		var tag = new net.minecraft.nbt.CompoundTag();
		before.saveWithoutId(tag);
		//?}
		tag.remove("UUID");
		LongStandingFixes.VANILLA_OBJECTS.remove(before);
		level.getServer().getPlayerList().remove(before);
		CHANNELS.remove(before);
		var after = realPlayer(helper, fixed ? "test-pearl-again" : "test-pearl-again-vanilla");
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(after);
		//? if >=1.21.11 {
		/*after.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, level.registryAccess(), tag));
		*///?} else
		after.load(tag);
		after.tick();
		boolean cooling = pearlOnCooldown(after);
		LongStandingFixes.VANILLA_OBJECTS.remove(after);
		level.getServer().getPlayerList().remove(after);
		CHANNELS.remove(after);
		System.out.printf("[long_standing_fixes] MC-111801 %s: ender pearl still on cooldown after the relog: %s%n", fixed ? "fixed" : "vanilla", cooling);
		if (fixed) helper.assertTrue(cooling, "the ender pearl cooldown was lost on relog");
		else helper.assertTrue(!cooling, "vanilla kept the cooldown over a relog; is MC-111801 fixed?");
		helper.succeed();
	}

	/** MC-111801: an item cooldown survives logging out and back in. */
	public static void cooldownSurvivesRelog(GameTestHelper helper) {
		cooldownRelog(helper, true);
	}

	/** Without the fix it is gone. */
	public static void cooldownSurvivesRelogVanilla(GameTestHelper helper) {
		cooldownRelog(helper, false);
	}

	/** A player with an ender pearl cooldown goes to the Nether; count the cooldown packets their game gets. */
	private static void cooldownDimension(GameTestHelper helper, boolean fixed) {
		var server = helper.getLevel().getServer();
		var player = realPlayer(helper, fixed ? "test-traveller" : "test-traveller-vanilla");
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		player.tick();
		pearlCooldown(player, 200);
		var nether = server.getLevel(net.minecraft.world.level.Level.NETHER);
		//? if >=1.21.11 {
		/*player.teleportTo(nether, 0.5, 64, 0.5, java.util.Set.of(), 0.0F, 0.0F, true);
		*///?} else
		player.teleportTo(nether, 0.5, 64, 0.5, 0.0F, 0.0F);
		var channel = CHANNELS.get(player);
		channel.runPendingTasks();
		channel.outboundMessages().clear();
		player.tick();
		channel.runPendingTasks();
		long resent = channel.outboundMessages().stream().filter(m -> m instanceof net.minecraft.network.protocol.game.ClientboundCooldownPacket).count();
		boolean cooling = pearlOnCooldown(player);
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		server.getPlayerList().remove(player);
		CHANNELS.remove(player);
		System.out.printf("[long_standing_fixes] MC-253476 %s: server still has the cooldown %s, cooldown packets sent after the trip: %d%n", fixed ? "fixed" : "vanilla", cooling, resent);
		helper.assertTrue(cooling, "the server lost the cooldown on the way to the Nether");
		if (fixed) helper.assertTrue(resent >= 1, "the player's game was not told about the cooldown after changing dimension");
		else helper.assertTrue(resent == 0, "vanilla re-sent " + resent + " cooldowns; is MC-253476 fixed?");
		helper.succeed();
	}

	/** MC-253476: after changing dimension the player's game is told about running cooldowns again. */
	public static void cooldownSurvivesDimensionChange(GameTestHelper helper) {
		cooldownDimension(helper, true);
	}

	/** Without the fix nothing is sent and the game forgets them. */
	public static void cooldownSurvivesDimensionChangeVanilla(GameTestHelper helper) {
		cooldownDimension(helper, false);
	}

	/**
	 * The server lags: two real seconds pass between two of its ticks. A player who had chatted the counter up to 190 (just
	 * under the kick at 200) should cool down by the 40 steps of those two seconds, not by 1.
	 */
	private static void spam(GameTestHelper helper, boolean fixed) {
		var player = realPlayer(helper, fixed ? "test-chatter" : "test-chatter-vanilla");
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		var counters = (LongStandingFixes.SpamCounters) player.connection;
		long[] now = {1_000_000_000L};
		var clock = LongStandingFixes.CLOCK;
		LongStandingFixes.CLOCK = () -> now[0];
		int left;
		try {
			player.connection.tick();
			counters.longstandingfixes$setChatSpam(190);
			now[0] += 2_000_000_000L;
			player.connection.tick();
			left = counters.longstandingfixes$chatSpam();
		} finally {
			LongStandingFixes.CLOCK = clock;
		}
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		helper.getLevel().getServer().getPlayerList().remove(player);
		CHANNELS.remove(player);
		System.out.printf("[long_standing_fixes] MC-276819 %s: spam counter 190 -> %d over one 2-second tick%n", fixed ? "fixed" : "vanilla", left);
		if (fixed) helper.assertTrue(left == 150, "the spam counter only cooled down to " + left + ", expected 150");
		else helper.assertTrue(left == 189, "vanilla cooled down to " + left + ", expected 189; is MC-276819 fixed?");
		helper.succeed();
	}

	/** MC-276819: spam counters cool down by real time on a lagging server. */
	public static void spamLimitInRealTime(GameTestHelper helper) {
		spam(helper, true);
	}

	/** Without the fix they cool down by one step per tick, however long the tick took. */
	public static void spamLimitInRealTimeVanilla(GameTestHelper helper) {
		spam(helper, false);
	}

	/** A snowball hits an end crystal (60 blocks above the test, so a vanilla blast can't reach the other tests). */
	private static void crystal(GameTestHelper helper, boolean fixed) {
		var level = helper.getLevel();
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 60, 2.5));
		//? if >=26 {
		/*var crystal = new net.minecraft.world.entity.boss.enderdragon.EndCrystal(net.minecraft.world.entity.EntityTypes.END_CRYSTAL, level);
		var snowball = new net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball(level, at.x, at.y + 2, at.z, new ItemStack(Items.SNOWBALL));
		*///?} else if >=1.21.11 {
		/*var crystal = new net.minecraft.world.entity.boss.enderdragon.EndCrystal(EntityType.END_CRYSTAL, level);
		var snowball = new net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball(level, at.x, at.y + 2, at.z, new ItemStack(Items.SNOWBALL));
		*///?} else {
		var crystal = new net.minecraft.world.entity.boss.enderdragon.EndCrystal(EntityType.END_CRYSTAL, level);
		var snowball = new net.minecraft.world.entity.projectile.Snowball(level, at.x, at.y + 2, at.z);
		//?}
		crystal.setPos(at.x, at.y, at.z);
		crystal.setShowBottom(false);
		level.addFreshEntity(crystal);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(crystal.getUUID());
		var source = level.damageSources().thrown(snowball, null);
		// the fix is off by default; switch it on for this hit only
		boolean enabled = LongStandingFixes.crystalsIgnoreHarmlessHits;
		LongStandingFixes.crystalsIgnoreHarmlessHits = true;
		//? if >=1.21.11 {
		/*crystal.hurtServer(level, source, 0.0F);
		*///?} else
		crystal.hurt(source, 0.0F);
		LongStandingFixes.crystalsIgnoreHarmlessHits = enabled;
		boolean intact = crystal.isAlive();
		LongStandingFixes.VANILLA_ENTITIES.remove(crystal.getUUID());
		crystal.discard();
		System.out.printf("[long_standing_fixes] MC-101851 %s: end crystal still there after a snowball hit: %s%n", fixed ? "fixed" : "vanilla", intact);
		if (fixed) helper.assertTrue(intact, "a snowball blew up the end crystal");
		else helper.assertTrue(!intact, "vanilla crystal survived a snowball; is MC-101851 fixed?");
		helper.succeed();
	}

	/** MC-101851: a harmless snowball hit leaves an end crystal alone. */
	public static void crystalIgnoresSnowball(GameTestHelper helper) {
		crystal(helper, true);
	}

	/** Without the fix the crystal explodes. */
	public static void crystalIgnoresSnowballVanilla(GameTestHelper helper) {
		crystal(helper, false);
	}

	/**
	 * A wandering trader stands 5 blocks from a (frozen) ravager. Within 60 ticks it should start running away; it needs a
	 * moment, since fleeing only starts once it finds a spot to run to.
	 */
	private static void traderRavager(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 12; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			for (int y = 1; y < 4; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
		}
		//? if >=26 {
		/*var trader = helper.spawn(net.minecraft.world.entity.EntityTypes.WANDERING_TRADER, new BlockPos(6, 1, 2));
		var ravager = helper.spawn(net.minecraft.world.entity.EntityTypes.RAVAGER, new BlockPos(1, 1, 2));
		*///?} else {
		var trader = helper.spawn(EntityType.WANDERING_TRADER, new BlockPos(6, 1, 2));
		var ravager = helper.spawn(EntityType.RAVAGER, new BlockPos(1, 1, 2));
		//?}
		ravager.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(trader.getUUID());
		boolean[] fled = {false};
		for (int t = 1; t <= 60; t++) helper.runAfterDelay(t, () -> {
			fled[0] |= ((io.github.traelmyx.longstandingfixes.mixin.MobGoalsAccessor) trader).longstandingfixes$goals().getAvailableGoals().stream()
				.anyMatch(g -> g.isRunning() && g.getGoal() instanceof net.minecraft.world.entity.ai.goal.AvoidEntityGoal);
		});
		helper.runAfterDelay(61, () -> {
			LongStandingFixes.VANILLA_ENTITIES.remove(trader.getUUID());
			trader.discard();
			ravager.discard();
			System.out.printf("[long_standing_fixes] MC-262810 %s: wandering trader ran from the ravager within 60 ticks: %s%n", fixed ? "fixed" : "vanilla", fled[0]);
			if (fixed) helper.assertTrue(fled[0], "the wandering trader did not run from the ravager");
			else helper.assertTrue(!fled[0], "vanilla trader ran from the ravager; is MC-262810 fixed?");
			helper.succeed();
		});
	}

	/** MC-262810: wandering traders run from ravagers. */
	public static void traderFleesRavager(GameTestHelper helper) {
		traderRavager(helper, true);
	}

	/** Without the fix they don't. */
	public static void traderFleesRavagerVanilla(GameTestHelper helper) {
		traderRavager(helper, false);
	}

	/** Give a shulker Levitation. */
	private static void shulker(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
		//? if >=26 {
		/*var shulker = helper.spawn(net.minecraft.world.entity.EntityTypes.SHULKER, new BlockPos(2, 1, 2));
		*///?} else
		var shulker = helper.spawn(EntityType.SHULKER, new BlockPos(2, 1, 2));
		shulker.setNoAi(true);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(shulker.getUUID());
		shulker.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION, 200));
		boolean levitating = shulker.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
		LongStandingFixes.VANILLA_ENTITIES.remove(shulker.getUUID());
		shulker.discard();
		System.out.printf("[long_standing_fixes] MC-205843 %s: shulker has Levitation: %s%n", fixed ? "fixed" : "vanilla", levitating);
		if (fixed) helper.assertTrue(!levitating, "the shulker took Levitation");
		else helper.assertTrue(levitating, "vanilla shulker refused Levitation; is MC-205843 fixed?");
		helper.succeed();
	}

	/** MC-205843: shulkers can't get Levitation. */
	public static void shulkerRefusesLevitation(GameTestHelper helper) {
		shulker(helper, true);
	}

	/** Without the fix they take it. */
	public static void shulkerRefusesLevitationVanilla(GameTestHelper helper) {
		shulker(helper, false);
	}

	/** An iron golem and a zombie on the same team stand in one spot for 200 ticks, bumping into each other. */
	private static void golemTeam(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			for (int y = 1; y < 4; y++) helper.setBlock(new BlockPos(x, y, z), x == 0 || x == 4 || z == 0 || z == 4 ? Blocks.GLASS : Blocks.AIR);
		}
		//? if >=26 {
		/*var golem = helper.spawn(net.minecraft.world.entity.EntityTypes.IRON_GOLEM, new BlockPos(2, 1, 2));
		var zombie = helper.spawn(net.minecraft.world.entity.EntityTypes.ZOMBIE, new BlockPos(2, 1, 2));
		*///?} else {
		var golem = helper.spawn(EntityType.IRON_GOLEM, new BlockPos(2, 1, 2));
		var zombie = helper.spawn(EntityType.ZOMBIE, new BlockPos(2, 1, 2));
		//?}
		zombie.setNoAi(true);
		var scoreboard = helper.getLevel().getScoreboard();
		String name = "lsf_" + (fixed ? "team" : "team_v");
		var team = scoreboard.getPlayerTeam(name);
		if (team == null) team = scoreboard.addPlayerTeam(name);
		scoreboard.addPlayerToTeam(golem.getScoreboardName(), team);
		scoreboard.addPlayerToTeam(zombie.getScoreboardName(), team);
		if (!fixed) LongStandingFixes.VANILLA_ENTITIES.add(golem.getUUID());
		boolean[] attacked = {false};
		for (int t = 1; t <= 200; t++) helper.runAfterDelay(t, () -> {
			attacked[0] |= golem.getTarget() == zombie;
			// keep them overlapping
			zombie.setPos(golem.getX(), golem.getY(), golem.getZ());
		});
		helper.runAfterDelay(201, () -> {
			LongStandingFixes.VANILLA_ENTITIES.remove(golem.getUUID());
			golem.discard();
			zombie.discard();
			System.out.printf("[long_standing_fixes] MC-95782 %s: golem went for its teammate within 200 ticks: %s%n", fixed ? "fixed" : "vanilla", attacked[0]);
			if (fixed) helper.assertTrue(!attacked[0], "the iron golem attacked a zombie on its own team");
			else helper.assertTrue(attacked[0], "vanilla golem left its teammate alone; is MC-95782 fixed?");
			helper.succeed();
		});
	}

	/** MC-95782: a golem bumping into a hostile teammate doesn't attack it. */
	public static void golemSparesTeammate(GameTestHelper helper) {
		golemTeam(helper, true);
	}

	/** Without the fix it attacks. */
	public static void golemSparesTeammateVanilla(GameTestHelper helper) {
		golemTeam(helper, false);
	}

	/**
	 * Asks the spawn finder for a spot above a far-away, unloaded flat-world chunk while the default game mode is
	 * Adventure, the way a respawn does when nobody is near the world spawn.
	 */
	private static void adventureSpawn(GameTestHelper helper, boolean fixed) {
		//? if >=1.21.11 {
		/*var level = helper.getLevel();
		int far = fixed ? 20008 : -20008;
		BlockPos suggestion = new BlockPos(far, helper.absolutePos(new BlockPos(0, 1, 0)).getY(), far);
		helper.assertTrue(!level.hasChunk(suggestion.getX() >> 4, suggestion.getZ() >> 4), "the chunk at " + suggestion + " is already loaded");
		var worldData = level.getServer().getWorldData();
		var before = worldData.getGameType();
		if (!fixed) LongStandingFixes.VANILLA_BLOCKS.add(suggestion);
		net.minecraft.world.phys.Vec3 spawn;
		try {
			worldData.setGameType(net.minecraft.world.level.GameType.ADVENTURE);
			var future = net.minecraft.server.level.PlayerSpawnFinder.findSpawn(level, suggestion);
			helper.assertTrue(future.isDone(), "the Adventure spawn search did not finish straight away");
			spawn = future.join();
		} finally {
			worldData.setGameType(before);
			LongStandingFixes.VANILLA_BLOCKS.remove(suggestion);
		}
		BlockPos feet = BlockPos.containing(spawn);
		System.out.printf("[long_standing_fixes] MC-302519 %s: spawn suggested at y %d, found y %.1f (world bottom %d)%n", fixed ? "fixed" : "vanilla", suggestion.getY(), spawn.y, level.getMinY());
		if (fixed) {
			helper.assertTrue(feet.getY() > level.getMinY() + 1, "the player would spawn at y " + spawn.y + ", next to the bedrock");
			helper.assertTrue(!level.getBlockState(feet.below()).isAir() && level.getBlockState(feet).isAir() && level.getBlockState(feet.above()).isAir(),
				"y " + spawn.y + " is not standing room on the surface");
		} else {
			helper.assertTrue(feet.getY() == level.getMinY() + 1, "vanilla found y " + spawn.y + ", expected the bottom of the world; is MC-302519 fixed?");
		}
		helper.succeed();
		*///?} else {
		// 1.21.1 keeps the spawn chunks loaded and has no spawn finder
		helper.succeed();
		//?}
	}

	/** MC-302519: with Adventure as the default game mode, the spawn is found on the surface. */
	public static void adventureSpawnOnSurface(GameTestHelper helper) {
		adventureSpawn(helper, true);
	}

	/** Without the fix it is at the bottom of the world. */
	public static void adventureSpawnOnSurfaceVanilla(GameTestHelper helper) {
		adventureSpawn(helper, false);
	}

	/** A survival player glides into a pool and stays under water for 100 ticks; count what the elytra loses. */
	private static void elytraWater(GameTestHelper helper, boolean fixed) {
		for (int x = 0; x < 5; x++) for (int z = 0; z < 5; z++) {
			helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
			boolean wall = x == 0 || z == 0 || x == 4 || z == 4;
			for (int y = 1; y < 6; y++) helper.setBlock(new BlockPos(x, y, z), wall ? Blocks.GLASS : Blocks.WATER);
		}
		var player = realPlayer(helper, fixed ? "test-glider" : "test-glider-vanilla");
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		ItemStack elytra = new ItemStack(Items.ELYTRA);
		player.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, elytra);
		var at = helper.absoluteVec(new net.minecraft.world.phys.Vec3(2.5, 2.5, 2.5));
		place(player, at.x, at.y, at.z, 0.0F);
		if (!fixed) LongStandingFixes.VANILLA_OBJECTS.add(player);
		player.startFallFlying();
		boolean wasGliding = player.isFallFlying();
		for (int i = 0; i < 100; i++) {
			// hold the player mid-pool, the way swimming keeps them off the bottom
			place(player, at.x, at.y, at.z, 0.0F);
			player.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
			player.doTick();
		}
		boolean inWater = player.isInWater();
		boolean stillGliding = player.isFallFlying();
		int worn = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST).getDamageValue();
		LongStandingFixes.VANILLA_OBJECTS.remove(player);
		helper.getLevel().getServer().getPlayerList().remove(player);
		CHANNELS.remove(player);
		System.out.printf("[long_standing_fixes] MC-298489 %s: in water %s, gliding %s -> %s, elytra lost %d durability in 100 ticks%n", fixed ? "fixed" : "vanilla", inWater, wasGliding, stillGliding, worn);
		helper.assertTrue(wasGliding && inWater, "the setup failed: gliding " + wasGliding + ", in water " + inWater);
		if (fixed) helper.assertTrue(worn == 0 && !stillGliding, "the elytra lost " + worn + " durability under water (still gliding: " + stillGliding + ")");
		else helper.assertTrue(worn > 0, "vanilla kept the elytra intact under water; is MC-298489 fixed?");
		helper.succeed();
	}

	/** MC-298489: an elytra doesn't wear out while its wearer is in water. */
	public static void elytraStopsInWater(GameTestHelper helper) {
		elytraWater(helper, true);
	}

	/** Without the fix it keeps losing durability. */
	public static void elytraStopsInWaterVanilla(GameTestHelper helper) {
		elytraWater(helper, false);
	}
}
