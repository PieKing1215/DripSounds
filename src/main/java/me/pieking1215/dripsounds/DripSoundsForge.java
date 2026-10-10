package me.pieking1215.dripsounds;

//? if forge {
/*import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

//? if >=1.20.5 {
import net.minecraftforge.client.gui.IConfigScreenFactory;
//?} else if >=1.19 {
/^import net.minecraftforge.client.ConfigScreenHandler;
^///?} else if >=1.18 {
/^import net.minecraftforge.client.ConfigGuiHandler;
^///?} else if >=1.17
/^import net.minecraftforge.fmlclient.ConfigGuiHandler;^/

//? if >=1.18 {
import net.minecraftforge.client.event.ScreenEvent;
//?} else
/^import net.minecraftforge.client.event.GuiScreenEvent;^/

@Mod(DripSounds.MOD_ID)
public class DripSoundsForge {
    public DripSoundsForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imProcess);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void imProcess(InterModProcessEvent evt) {
        if (FMLEnvironment.dist.isClient()) {
            DripSounds.finishInit();
        }
    }

    void clientSetup(final FMLClientSetupEvent event) {
        //? if >= 1.17 {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        //?} else {
        /^ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> "", (a, b) -> true));
        ^///?}

        //? if >=1.20.5 {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mc, screen) -> DripSoundsConfig.setupCloth(screen));
        //?} else if >= 1.19 {
        /^ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));
         ^///?} else if >= 1.17 {
        /^ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));
         ^///?} else
        /^ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> DripSoundsConfig.setupCloth(screen));^/
    }
}
*///?}