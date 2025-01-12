package me.pieking1215.dripsounds.fabric;

import me.pieking1215.dripsounds.DripSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class DripSoundsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DripSounds.setInstance(new DripSounds() {
            @Override
            public boolean hasMod(String modid) {
                return FabricLoader.getInstance().isModLoaded(modid);
            }

            @Override
            public File configDir() {
                return FabricLoader.getInstance().getConfigDir().toFile();
            }
        });

        DripSounds.instance().finishInit();
    }
}
