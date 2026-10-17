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
		props.setProperty("MC-166272.campfireComparator", Boolean.toString(campfireComparator));
		props.setProperty("MC-159069.babyVillagersTakePoppies", Boolean.toString(golemPoppy));
		props.setProperty("MC-217397.burningSkeletonsShootBurningArrows", Boolean.toString(burningSkeletons));
		props.setProperty("MC-280273.windChargesPushItems", Boolean.toString(windChargeItems));
		props.setProperty("MC-219854.spectatorsIgnoreTheVoid", Boolean.toString(spectatorVoid));
		props.setProperty("MC-168976.logBrokenNamesOnce", Boolean.toString(spawnerLogSpam));
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
