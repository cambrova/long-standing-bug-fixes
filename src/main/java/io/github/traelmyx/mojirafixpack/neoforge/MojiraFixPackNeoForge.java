package io.github.traelmyx.mojirafixpack.neoforge;

//? if neoforge {
import io.github.traelmyx.mojirafixpack.MojiraFixPack;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(MojiraFixPack.MOD_ID)
public class MojiraFixPackNeoForge {
	public MojiraFixPackNeoForge() {
		MojiraFixPack.loadConfig(FMLPaths.CONFIGDIR.get());
	}
}
//?}
