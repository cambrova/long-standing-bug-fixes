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
}
