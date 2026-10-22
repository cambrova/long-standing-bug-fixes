package io.github.cambrova.spamkickfix.neoforge;

//? if neoforge {
import io.github.cambrova.spamkickfix.SpamKickFix;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(SpamKickFix.MOD_ID)
public class SpamKickFixNeoForge {
	public SpamKickFixNeoForge() {
		SpamKickFix.loadConfig(FMLPaths.CONFIGDIR.get());
	}
}
//?}
