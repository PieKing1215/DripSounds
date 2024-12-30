package me.pieking1215.dripsounds.neoforge;

import me.pieking1215.dripsounds.DripSounds;
import me.pieking1215.dripsounds.DripSoundsConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLPaths;
//? if >=1.20.5 {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else
/*import net.neoforged.neoforge.client.ConfigScreenHandler;*/
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.security.CodeSource;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DripSoundsNeoForgeClient {

    static void finishInit() {
        DripSounds.instance().finishInit();
    }

    static void clientSetup(final FMLClientSetupEvent event) {
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
        //?} else
        /*ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> DripSoundsConfig.setupCloth(screen)));*/
    }
}
