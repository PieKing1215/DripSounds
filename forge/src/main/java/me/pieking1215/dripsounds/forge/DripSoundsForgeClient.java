package me.pieking1215.dripsounds.forge;

import me.pieking1215.dripsounds.DripSounds;
import me.pieking1215.dripsounds.DripSoundsConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//? if >=1.17 {
import net.minecraftforge.fml.IExtensionPoint;
//?} else
/*import net.minecraftforge.fml.ExtensionPoint;*/

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLPaths;

//? if >=1.20.5 {
import net.minecraftforge.client.gui.IConfigScreenFactory;
//?} else if >=1.19 {
/*import net.minecraftforge.client.ConfigScreenHandler;*/
//?} else if >=1.18 {
/*import net.minecraftforge.client.ConfigGuiHandler;*/
//?} else if >=1.17
/*import net.minecraftforge.fmlclient.ConfigGuiHandler;*/

//? if >=1.18 {
import net.minecraftforge.client.event.ScreenEvent;
//?} else
/*import net.minecraftforge.client.event.GuiScreenEvent;*/

import net.minecraftforge.client.settings.KeyConflictContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.lang.reflect.Field;
import java.security.CodeSource;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DripSoundsForgeClient {

    static void finishInit() {
        DripSounds.instance().finishInit();
    }

    static void clientSetup(final FMLClientSetupEvent event) {
        //? if >= 1.17 {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        //?} else {
        /*ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> "", (a, b) -> true));
        *///?}
        DripSounds.setInstance(new DripSounds() {
            @Override
            public boolean hasMod(String modid) {
                return ModList.get().isLoaded(modid);
            }

            @Override
            public File configDir() {
                return FMLPaths.CONFIGDIR.get().toFile();
            }
        });

        //? if >=1.20.5 {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mc, screen) -> DripSoundsConfig.setupCloth(screen));
        //?} else if >= 1.19 {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));
        *///?} else if >= 1.17 {
        /*ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));
        *///?} else
        /*ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> DripSoundsConfig.setupCloth(screen));*/
    }
}
