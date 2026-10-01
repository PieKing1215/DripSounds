package me.pieking1215.dripsounds;

//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;

//? if >=1.20.5 {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else
//import net.neoforged.neoforge.client.ConfigScreenHandler;

@Mod(DripSounds.MOD_ID)
public class DripSoundsNeoForge {
    public DripSoundsNeoForge() {
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(this::imProcess);
        ModLoadingContext.get().getActiveContainer().getEventBus().addListener(this::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (/*? if >= 1.21.9 {*/FMLEnvironment.getDist()/*?} else {*//*FMLEnvironment.dist*//*?}*/ == Dist.CLIENT) {
            DripSounds.finishInit();
        }
    }

    void clientSetup(final FMLClientSetupEvent event) {
        //? if >=1.20.5 {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mc, screen) -> DripSoundsConfig.setupCloth(screen));
        //?} else
        //ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));
    }
}
//?}