package io.github.traelmyx.longstandingfixes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Small fixes for confirmed, still-open bugs from Mojang's bug tracker. Each can be turned off on its own. */
public final class LongStandingFixes {
	public static final String MOD_ID = "long_standing_fixes";
	public static final Logger LOGGER = LoggerFactory.getLogger("Long-Standing Bug Fixes");

	/** MC-166272: comparators read how many items are cooking on a campfire. */
	public static boolean campfireComparator = true;
	/** MC-159069: baby villagers walk up to an iron golem offering a poppy and take it. */
	public static boolean golemPoppy = true;
	/** MC-217397: skeletons that are on fire shoot burning arrows again. */
	public static boolean burningSkeletons = true;
	/** MC-280273 (1.21.5 and newer): wind charges push dropped items again. */
	public static boolean windChargeItems = true;
	/** MC-219854: spectators don't take void damage. */
	public static boolean spectatorVoid = true;
	/** MC-168976 (1.21.1): a broken entity custom name is logged once, not on every spawner attempt. */
	public static boolean spawnerLogSpam = true;
	/** MC-186909: clicking a creeper that is already lit doesn't use up more flint and steel or fire charges. */
	public static boolean creeperIgniters = true;
	/** MC-261789: falling blocks that land fast below Y=0 turn into blocks instead of breaking into items. */
	public static boolean fallingBlockPrecision = true;
	/** MC-269636: taking the wax off a lit copper bulb doesn't award "Lighten Up". */
	public static boolean lightenUp = true;
	/** MC-210491: batting a fireball away is a vibration sculk sensors pick up, like hitting any other entity. */
	public static boolean sculkBattedProjectiles = true;

	/** MC-265635: a cat sitting on a chest no longer keeps the chest below that one shut. */
	public static boolean catOnChestAbove = true;
	/** MC-135198: dispensers with flint and steel light a portal through any empty spot inside the frame. */
	public static boolean dispenserLightsPortals = true;
	/** MC-278572 (1.21.2 and newer): items, TNT, minecarts and boats slide down honey at the old speed again. */
	public static boolean honeySlide = true;
	/** MC-279003 (1.21.4 and newer): explosions push wind charges again. */
	public static boolean explosionsPushWindCharges = true;
	/** MC-147694 (1.21.1): a mob that hits a raised shield is knocked back. OFF by default: a combat change. */
	public static boolean shieldsRepelMobs = false;
	/** MC-163962: A villager trade's demand never drops below 0, so prices still go up when you trade a lot. OFF by default: trading halls rely on cheap trades. */
	public static boolean villagerDemand = false;
	/** MC-264285: Lighting a creeper doesn't use up an unbreakable flint and steel. */
	public static boolean unbreakableIgniters = true;
	/** MC-179072: A swelling creeper calms down when its player switches to creative or spectator. */
	public static boolean creepersDefuse = true;
	/** MC-262422: Thunderstorm lightning doesn't aim at spectators. */
	public static boolean lightningSkipsSpectators = true;
	/** MC-153010: With doMobLoot off, foxes don't drop what they carry. */
	public static boolean foxMobLoot = true;
	/** MC-248588: with mobGriefing off, burning mobs don't use up water or powder snow in cauldrons. */
	public static boolean cauldronMobGriefing = true;
	/** MC-310859 (1.21.11 and newer): spears with Knockback knock players back too. */
	public static boolean spearKnockbackPlayers = true;
	/** MC-164171 / MC-109083: the wither skull or ghast fireball that kills a player doesn't blow up the items they drop. */
	public static boolean keepDeathDrops = true;
	/** MC-17876: health above 20 from health-boosting gear survives a relog or chunk reload. */
	public static boolean keepBonusHealth = true;
	/** MC-111801 / MC-253476: item cooldowns survive logging out and changing dimension. */
	public static boolean keepCooldowns = true;
	/** MC-276819: spam counters cool down in real time, so a lagging server doesn't kick players for normal chat. */
	public static boolean spamRealTime = true;
	/** MC-101851: snowballs, eggs and ender pearls no longer blow up end crystals. OFF by default: players use it as a tactic. */
	public static boolean crystalsIgnoreHarmlessHits = false;
	/** MC-262810: Wandering traders run from ravagers too. */
	public static boolean tradersFleeRavagers = true;
	/** MC-205843: Shulkers can no longer get Levitation. */
	public static boolean shulkersNoLevitation = true;
	/** MC-95782: iron golems don't attack hostile mobs on their own team when they bump into them. */
	public static boolean golemsSpareTeammates = true;
	/** MC-302519 (1.21.11 and newer): with Adventure as the default game mode, players respawn on the surface, not in the bedrock. */
	public static boolean adventureSpawnOnSurface = true;
	/** MC-298489: gliding ends in water, so the elytra stops losing durability while you sink or swim. */
	public static boolean elytraStopsInWater = true;
	/** MC-308361 (26.2 and newer): the small cubes a bucketed sulfur cube splits into don't despawn either. */
	public static boolean bucketedCubesSplitPersistent = true;
	/** MC-262182: mobs that fight in melee still hit a target in reach while another mob rides them. */
	public static boolean riddenMobsAttack = true;
	/** MC-99601: typing /whitelist add, /op or /ban for a name the server hasn't seen doesn't freeze the server during the lookup. */
	public static boolean namesLookedUpInBackground = true;

	/** The command a player or the console is running on this thread, so it can run again after a name lookup (MC-99601). */
	public record RunningCommand(net.minecraft.commands.CommandSourceStack source, String command) {
	}

	public static final ThreadLocal<RunningCommand> RUNNING_COMMAND = new ThreadLocal<>();
	/** Names whose background lookup found nothing; the command that asked runs once more and fails as usual. */
	public static final java.util.Set<String> FAILED_LOOKUPS = java.util.concurrent.ConcurrentHashMap.newKeySet();
	/** Where player names are looked up off the main thread. */
	public static final java.util.concurrent.ExecutorService LOOKUPS = java.util.concurrent.Executors.newFixedThreadPool(2, task -> {
		Thread thread = new Thread(task, "Long-Standing Bug Fixes name lookup");
		thread.setDaemon(true);
		return thread;
	});

	/** The server's player-name cache, asked whether it already knows a name. */
	public interface ProfileCachePeek {
		boolean longstandingfixes$isCached(String name);
	}

	/** Lower-case names the game tests make slow to look up, standing in for a slow answer from Mojang. */
	public static final java.util.Set<String> SLOW_TEST_NAMES = java.util.concurrent.ConcurrentHashMap.newKeySet();

	/** Waits a second if the game tests asked for this name to be slow; true if it was one of theirs. */
	public static boolean slowTestLookup(String name) {
		if (!SLOW_TEST_NAMES.contains(name.toLowerCase(java.util.Locale.ROOT))) return false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return true;
	}

	/** The clock the spam counters cool down by; the game tests swap it for one they can wind forward. */
	public static java.util.function.LongSupplier CLOCK = System::nanoTime;

	/** A connection's chat spam counter, for the game tests (MC-276819). */
	public interface SpamCounters {
		int longstandingfixes$chatSpam();

		void longstandingfixes$setChatSpam(int count);
	}

	/** A player's item cooldowns, written as text and read back (MC-111801). */
	public interface Cooldowns {
		String longstandingfixes$save();

		void longstandingfixes$load(String saved);

		void longstandingfixes$resend();
	}

	/** An item that knows whether a death dropped it this very tick (MC-164171). */
	public interface DeathDrop {
		boolean longstandingfixes$freshDeathDrop();
	}

	/** How many entities are dying on this thread right now (MC-164171). */
	public static final ThreadLocal<Integer> DYING = ThreadLocal.withInitial(() -> 0);

	/** Set while the item-used-on-block trigger runs for a waxed copper bulb (MC-269636). */
	public static final ThreadLocal<Boolean> UNWAXING_BULB = new ThreadLocal<>();

	private static final java.util.Set<String> WARNED_NAMES = java.util.concurrent.ConcurrentHashMap.newKeySet();
	/** Names the game tests keep on vanilla behaviour (logged every time). */
	public static final java.util.Set<String> VANILLA_NAMES = java.util.concurrent.ConcurrentHashMap.newKeySet();
	/** Warnings that went through, for the tests. */
	public static final java.util.concurrent.atomic.AtomicInteger customNameWarnings = new java.util.concurrent.atomic.AtomicInteger();

	/** True the first time a broken name is seen (always for names kept on vanilla); bounded so it can't grow forever. */
	public static boolean firstWarning(String name) {
		if (VANILLA_NAMES.contains(name)) {
			customNameWarnings.incrementAndGet();
			return true;
		}
		if (WARNED_NAMES.size() > 1000) WARNED_NAMES.clear();
		boolean first = WARNED_NAMES.add(name);
		if (first) customNameWarnings.incrementAndGet();
		return first;
	}

	/** Blocks and entities the game tests keep on vanilla behaviour, to show each bug still happens without its fix. */
	public static final java.util.Set<net.minecraft.core.BlockPos> VANILLA_BLOCKS = java.util.concurrent.ConcurrentHashMap.newKeySet();
	public static final java.util.Set<java.util.UUID> VANILLA_ENTITIES = java.util.concurrent.ConcurrentHashMap.newKeySet();
	/** Same, by object: game-test mock players all share one UUID. */
	public static final java.util.Set<Object> VANILLA_OBJECTS = java.util.Collections.synchronizedSet(java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>()));

	private LongStandingFixes() {
	}

	public static void loadConfig(Path configDir) {
		Path file = configDir.resolve(MOD_ID + ".properties");
		Properties props = new Properties();
		if (Files.exists(file)) {
			try (Reader reader = Files.newBufferedReader(file)) {
				props.load(reader);
			} catch (IOException e) {
				LOGGER.warn("Could not read {}, using defaults", file, e);
			}
		}
		campfireComparator = Boolean.parseBoolean(props.getProperty("MC-166272.campfireComparator", "true"));
		golemPoppy = Boolean.parseBoolean(props.getProperty("MC-159069.babyVillagersTakePoppies", "true"));
		burningSkeletons = Boolean.parseBoolean(props.getProperty("MC-217397.burningSkeletonsShootBurningArrows", "true"));
		windChargeItems = Boolean.parseBoolean(props.getProperty("MC-280273.windChargesPushItems", "true"));
		spectatorVoid = Boolean.parseBoolean(props.getProperty("MC-219854.spectatorsIgnoreTheVoid", "true"));
		spawnerLogSpam = Boolean.parseBoolean(props.getProperty("MC-168976.logBrokenNamesOnce", "true"));
		creeperIgniters = Boolean.parseBoolean(props.getProperty("MC-186909.lightCreepersOnce", "true"));
		fallingBlockPrecision = Boolean.parseBoolean(props.getProperty("MC-261789.fallingBlocksLandBelowZero", "true"));
		lightenUp = Boolean.parseBoolean(props.getProperty("MC-269636.noLightenUpForWax", "true"));
		sculkBattedProjectiles = Boolean.parseBoolean(props.getProperty("MC-210491.sculkHearsBattedFireballs", "true"));
		catOnChestAbove = Boolean.parseBoolean(props.getProperty("MC-265635.catsOnlyBlockTheirChest", "true"));
		dispenserLightsPortals = Boolean.parseBoolean(props.getProperty("MC-135198.dispensersLightPortals", "true"));
		honeySlide = Boolean.parseBoolean(props.getProperty("MC-278572.honeySlideSpeed", "true"));
		explosionsPushWindCharges = Boolean.parseBoolean(props.getProperty("MC-279003.explosionsPushWindCharges", "true"));
		shieldsRepelMobs = Boolean.parseBoolean(props.getProperty("MC-147694.shieldsRepelMobs", "false"));
		villagerDemand = Boolean.parseBoolean(props.getProperty("MC-163962.villagerDemandStopsAtZero", "false"));
		unbreakableIgniters = Boolean.parseBoolean(props.getProperty("MC-264285.keepUnbreakableFlintAndSteel", "true"));
		creepersDefuse = Boolean.parseBoolean(props.getProperty("MC-179072.creepersDefuseForCreative", "true"));
		lightningSkipsSpectators = Boolean.parseBoolean(props.getProperty("MC-262422.lightningSkipsSpectators", "true"));
		foxMobLoot = Boolean.parseBoolean(props.getProperty("MC-153010.foxesFollowDoMobLoot", "true"));
		cauldronMobGriefing = Boolean.parseBoolean(props.getProperty("MC-248588.cauldronsFollowMobGriefing", "true"));
		spearKnockbackPlayers = Boolean.parseBoolean(props.getProperty("MC-310859.spearKnockbackOnPlayers", "true"));
		keepDeathDrops = Boolean.parseBoolean(props.getProperty("MC-164171.killingBlastSparesDrops", "true"));
		keepBonusHealth = Boolean.parseBoolean(props.getProperty("MC-17876.keepBonusHealthOnLoad", "true"));
		keepCooldowns = Boolean.parseBoolean(props.getProperty("MC-111801.keepItemCooldowns", "true"));
		spamRealTime = Boolean.parseBoolean(props.getProperty("MC-276819.spamLimitInRealTime", "true"));
		crystalsIgnoreHarmlessHits = Boolean.parseBoolean(props.getProperty("MC-101851.crystalsIgnoreHarmlessHits", "false"));
		tradersFleeRavagers = Boolean.parseBoolean(props.getProperty("MC-262810.tradersFleeRavagers", "true"));
		shulkersNoLevitation = Boolean.parseBoolean(props.getProperty("MC-205843.shulkersCantLevitate", "true"));
		golemsSpareTeammates = Boolean.parseBoolean(props.getProperty("MC-95782.golemsSpareTeammates", "true"));
		adventureSpawnOnSurface = Boolean.parseBoolean(props.getProperty("MC-302519.adventureSpawnOnSurface", "true"));
		elytraStopsInWater = Boolean.parseBoolean(props.getProperty("MC-298489.elytraStopsInWater", "true"));
		bucketedCubesSplitPersistent = Boolean.parseBoolean(props.getProperty("MC-308361.bucketedSulfurCubesSplitPersistent", "true"));
		riddenMobsAttack = Boolean.parseBoolean(props.getProperty("MC-262182.riddenMobsAttack", "true"));
		namesLookedUpInBackground = Boolean.parseBoolean(props.getProperty("MC-99601.lookUpNamesInBackground", "true"));
		props.setProperty("MC-166272.campfireComparator", Boolean.toString(campfireComparator));
		props.setProperty("MC-159069.babyVillagersTakePoppies", Boolean.toString(golemPoppy));
		props.setProperty("MC-217397.burningSkeletonsShootBurningArrows", Boolean.toString(burningSkeletons));
		props.setProperty("MC-280273.windChargesPushItems", Boolean.toString(windChargeItems));
		props.setProperty("MC-219854.spectatorsIgnoreTheVoid", Boolean.toString(spectatorVoid));
		props.setProperty("MC-168976.logBrokenNamesOnce", Boolean.toString(spawnerLogSpam));
		props.setProperty("MC-186909.lightCreepersOnce", Boolean.toString(creeperIgniters));
		props.setProperty("MC-261789.fallingBlocksLandBelowZero", Boolean.toString(fallingBlockPrecision));
		props.setProperty("MC-269636.noLightenUpForWax", Boolean.toString(lightenUp));
		props.setProperty("MC-210491.sculkHearsBattedFireballs", Boolean.toString(sculkBattedProjectiles));
		props.setProperty("MC-265635.catsOnlyBlockTheirChest", Boolean.toString(catOnChestAbove));
		props.setProperty("MC-135198.dispensersLightPortals", Boolean.toString(dispenserLightsPortals));
		props.setProperty("MC-278572.honeySlideSpeed", Boolean.toString(honeySlide));
		props.setProperty("MC-279003.explosionsPushWindCharges", Boolean.toString(explosionsPushWindCharges));
		props.setProperty("MC-147694.shieldsRepelMobs", Boolean.toString(shieldsRepelMobs));
		props.setProperty("MC-163962.villagerDemandStopsAtZero", Boolean.toString(villagerDemand));
		props.setProperty("MC-264285.keepUnbreakableFlintAndSteel", Boolean.toString(unbreakableIgniters));
		props.setProperty("MC-179072.creepersDefuseForCreative", Boolean.toString(creepersDefuse));
		props.setProperty("MC-262422.lightningSkipsSpectators", Boolean.toString(lightningSkipsSpectators));
		props.setProperty("MC-153010.foxesFollowDoMobLoot", Boolean.toString(foxMobLoot));
		props.setProperty("MC-248588.cauldronsFollowMobGriefing", Boolean.toString(cauldronMobGriefing));
		props.setProperty("MC-310859.spearKnockbackOnPlayers", Boolean.toString(spearKnockbackPlayers));
		props.setProperty("MC-164171.killingBlastSparesDrops", Boolean.toString(keepDeathDrops));
		props.setProperty("MC-17876.keepBonusHealthOnLoad", Boolean.toString(keepBonusHealth));
		props.setProperty("MC-111801.keepItemCooldowns", Boolean.toString(keepCooldowns));
		props.setProperty("MC-276819.spamLimitInRealTime", Boolean.toString(spamRealTime));
		props.setProperty("MC-101851.crystalsIgnoreHarmlessHits", Boolean.toString(crystalsIgnoreHarmlessHits));
		props.setProperty("MC-262810.tradersFleeRavagers", Boolean.toString(tradersFleeRavagers));
		props.setProperty("MC-205843.shulkersCantLevitate", Boolean.toString(shulkersNoLevitation));
		props.setProperty("MC-95782.golemsSpareTeammates", Boolean.toString(golemsSpareTeammates));
		props.setProperty("MC-302519.adventureSpawnOnSurface", Boolean.toString(adventureSpawnOnSurface));
		props.setProperty("MC-298489.elytraStopsInWater", Boolean.toString(elytraStopsInWater));
		props.setProperty("MC-308361.bucketedSulfurCubesSplitPersistent", Boolean.toString(bucketedCubesSplitPersistent));
		props.setProperty("MC-262182.riddenMobsAttack", Boolean.toString(riddenMobsAttack));
		props.setProperty("MC-99601.lookUpNamesInBackground", Boolean.toString(namesLookedUpInBackground));
		try {
			Files.createDirectories(configDir);
			try (Writer writer = Files.newBufferedWriter(file)) {
				props.store(writer, "Long-Standing Bug Fixes: set a fix to false to get the vanilla behaviour back. Changes apply after a restart.");
			}
		} catch (IOException e) {
			LOGGER.warn("Could not write {}", file, e);
		}
	}

	/** The signal a container of 4 one-item slots gives: 0 when empty, 4, 8, 11 or 15. */
	public static int campfireSignal(int filledSlots) {
		return filledSlots == 0 ? 0 : 1 + (int) Math.floor(filledSlots / 4.0 * 14.0);
	}

	/** The mobGriefing game rule. */
	public static boolean mobGriefing(net.minecraft.server.level.ServerLevel level) {
		//? if >=1.21.11 {
		/*return level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.MOB_GRIEFING);
		*///?} else
		return level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING);
	}

	/** The doMobLoot game rule. */
	public static boolean mobLoot(net.minecraft.server.level.ServerLevel level) {
		//? if >=1.21.11 {
		/*return level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.MOB_DROPS);
		*///?} else
		return level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT);
	}
}
