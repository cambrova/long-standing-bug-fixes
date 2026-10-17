package io.github.traelmyx.longstandingfixes.neoforge;

//? if neoforge {
import io.github.traelmyx.longstandingfixes.LongStandingFixes;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(LongStandingFixes.MOD_ID)
public class LongStandingFixesNeoForge {
	public LongStandingFixesNeoForge() {
		LongStandingFixes.loadConfig(FMLPaths.CONFIGDIR.get());
	}
}
//?}
