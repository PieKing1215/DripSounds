package me.pieking1215.dripsounds.neoforge;

import me.pieking1215.dripsounds.DripSounds;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(DripSounds.MOD_ID)
public class DripSoundsNeoForge {
    public DripSoundsNeoForge() {
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(this::imProcess);
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(DripSoundsNeoForgeClient::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            DripSoundsNeoForgeClient.finishInit();
        }
    }
}
