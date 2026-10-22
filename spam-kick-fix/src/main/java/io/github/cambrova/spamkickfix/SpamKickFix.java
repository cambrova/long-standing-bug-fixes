package io.github.cambrova.spamkickfix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Stops "Kicked for spamming" on lagging servers, and on 1.21.1 / 1.21.11 gives commands their own limit and makes both
 * limits configurable, the way 26.2 does with server.properties.
 */
public final class SpamKickFix {
	public static final String MOD_ID = "spam_kick_fix";
	public static final Logger LOGGER = LoggerFactory.getLogger("Spam Kick Fix");

	/** The limits cool down by the real time that passed, not by server ticks (MC-276819). */
	public static boolean realTimeCooldown = true;
	/** 1.21.1 / 1.21.11: how many seconds' worth of chat a player may send at once; 0 never kicks. Vanilla is 10. */
	public static int chatLimitSeconds = 10;
	/** 1.21.1 / 1.21.11: the same for commands, counted apart from chat like in 26.2; 0 never kicks. */
	public static int commandLimitSeconds = 10;

	/** The clock the limits cool down by; the game tests swap it for one they can wind forward. */
	public static java.util.function.LongSupplier CLOCK = System::nanoTime;

	/** Connections the game tests keep on vanilla behaviour, to show each problem still happens without the mod. */
	public static final java.util.Set<Object> VANILLA_OBJECTS = java.util.Collections.synchronizedSet(java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>()));

	/** A connection's spam counters, for the game tests. */
	public interface SpamCounters {
		int spamkickfix$chatSpam();

		void spamkickfix$setChatSpam(int count);

		int spamkickfix$commandSpam();
	}

	private static Boolean longStandingFixesCools;

	/**
	 * Long-Standing Bug Fixes before 1.12.0 runs the same real-time cool-down; letting both run would cool down twice.
	 * From 1.12.0 it leaves the job to this mod when both are installed.
	 */
	public static boolean otherModCools() {
		if (longStandingFixesCools == null) {
			boolean cools;
			try {
				Class<?> lsbf = Class.forName("io.github.traelmyx.longstandingfixes.LongStandingFixes");
				cools = !hasField(lsbf, "SPAM_KICK_FIX_AWARE");
			} catch (ClassNotFoundException e) {
				cools = false;
			}
			if (cools) LOGGER.info("Long-Standing Bug Fixes already cools the spam limits down in real time; leaving that to it");
			longStandingFixesCools = cools;
		}
		return longStandingFixesCools;
	}

	private static boolean hasField(Class<?> type, String name) {
		try {
			type.getField(name);
			return true;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}

	private SpamKickFix() {
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
		realTimeCooldown = Boolean.parseBoolean(props.getProperty("real-time-cooldown", "true"));
		props.setProperty("real-time-cooldown", Boolean.toString(realTimeCooldown));
		String comment = "Spam Kick Fix. Changes apply after a restart. real-time-cooldown: the chat and command limits cool down by real time, so a lagging server doesn't kick players for normal chat.";
		//? if <26 {
		chatLimitSeconds = seconds(props, "chat-spam-threshold-seconds", chatLimitSeconds);
		commandLimitSeconds = seconds(props, "command-spam-threshold-seconds", commandLimitSeconds);
		props.setProperty("chat-spam-threshold-seconds", Integer.toString(chatLimitSeconds));
		props.setProperty("command-spam-threshold-seconds", Integer.toString(commandLimitSeconds));
		comment += " The thresholds work like 26.2's server.properties settings of the same name: how many seconds' worth of messages (one a second) a player may send in a burst, 0 = never kick. Vanilla is 10. Ops are never kicked.";
		//?} else
		/*comment += " On this version the limits themselves are in server.properties (chat-spam-threshold-seconds, command-spam-threshold-seconds; 0 = never kick).";*/
		try {
			Files.createDirectories(configDir);
			try (Writer writer = Files.newBufferedWriter(file)) {
				props.store(writer, comment);
			}
		} catch (IOException e) {
			LOGGER.warn("Could not write {}", file, e);
		}
	}

	private static int seconds(Properties props, String key, int fallback) {
		try {
			return Math.max(0, Integer.parseInt(props.getProperty(key, Integer.toString(fallback)).trim()));
		} catch (NumberFormatException e) {
			LOGGER.warn("{} must be a whole number of seconds, using {}", key, fallback);
			return fallback;
		}
	}
}
