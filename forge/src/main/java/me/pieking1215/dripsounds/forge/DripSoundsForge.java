package me.pieking1215.dripsounds.forge;

import me.pieking1215.dripsounds.DripSounds;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DripSounds.MOD_ID)
public class DripSoundsForge {
    public DripSoundsForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imProcess);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DripSoundsForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            DripSoundsForgeClient.finishInit();
        }
    }
}
