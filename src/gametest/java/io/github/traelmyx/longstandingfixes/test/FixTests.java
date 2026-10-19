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

	/** A plain server player in the player list; the helper's mock player always says it isn't a spectator. */
	private static net.minecraft.server.level.ServerPlayer realPlayer(GameTestHelper helper, String name) {
		var server = helper.getLevel().getServer();
		var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), name), false);
		var player = new net.minecraft.server.level.ServerPlayer(server, helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
		var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
		new io.netty.channel.embedded.EmbeddedChannel(connection);
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
		// let the sensor settle after everything above moved and appeared
		helper.runAfterDelay(60, () -> {
			helper.assertTrue(!sensorActive(helper, sensor), "the sculk sensor was still active before the hit");
			player.attack(fireball);
			// gone before it can fly into anything and explode, which sensors would hear anyway
			fireball.discard();
			helper.runAfterDelay(10, () -> {
				boolean active = sensorActive(helper, sensor);
				LongStandingFixes.VANILLA_ENTITIES.remove(fireball.getUUID());
				helper.getLevel().getServer().getPlayerList().remove(player);
				if (fixed) helper.assertTrue(active, "the sculk sensor did not hear the fireball being hit");
				else helper.assertTrue(!active, "vanilla's sculk sensor heard the fireball being hit; is MC-210491 fixed?");
				helper.succeed();
			});
		});
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
}
