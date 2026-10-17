package io.github.traelmyx.mojirafixpack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Small fixes for confirmed, still-open bugs from Mojang's bug tracker. Each can be turned off on its own. */
public final class MojiraFixPack {
	public static final String MOD_ID = "mojira_fix_pack";
	public static final Logger LOGGER = LoggerFactory.getLogger("Mojira Fix Pack");

	/** MC-166272: comparators read how many items are cooking on a campfire. */
	public static boolean campfireComparator = true;
	/** MC-159069: baby villagers walk up to an iron golem offering a poppy and take it. */
	public static boolean golemPoppy = true;
	/** MC-217397: skeletons that are on fire shoot burning arrows again. */
	public static boolean burningSkeletons = true;

	/** Blocks and entities the game tests keep on vanilla behaviour, to show each bug still happens without its fix. */
	public static final java.util.Set<net.minecraft.core.BlockPos> VANILLA_BLOCKS = java.util.concurrent.ConcurrentHashMap.newKeySet();
	public static final java.util.Set<java.util.UUID> VANILLA_ENTITIES = java.util.concurrent.ConcurrentHashMap.newKeySet();

	private MojiraFixPack() {
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
		props.setProperty("MC-166272.campfireComparator", Boolean.toString(campfireComparator));
		props.setProperty("MC-159069.babyVillagersTakePoppies", Boolean.toString(golemPoppy));
		props.setProperty("MC-217397.burningSkeletonsShootBurningArrows", Boolean.toString(burningSkeletons));
		try {
			Files.createDirectories(configDir);
			try (Writer writer = Files.newBufferedWriter(file)) {
				props.store(writer, "Mojira Fix Pack: set a fix to false to get the vanilla behaviour back. Changes apply after a restart.");
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
