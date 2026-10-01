package me.pieking1215.dripsounds;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//? if <= 1.18
//import net.minecraft.network.chat.TextComponent;
//? if <= 1.18
//import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric
//import net.fabricmc.loader.api.FabricLoader;
//? if neoforge {
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
//?} elif forge {
/*import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
*///?}

import java.io.File;

public class DripSounds {
    public static final String MOD_ID = /*$ modid*/ "waterdripsound";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String VERSION = /*$ mod_version*/ "0.6.0";
    public static final String MINECRAFT = /*$ minecraft*/ "26.3";

    public static void finishInit(){
        DripSoundsConfig.load();
    }

    // loader compatibility layer

    public static boolean hasMod(String modid) {
        //? if fabric
        //return FabricLoader.getInstance().isModLoaded(modid);
        //? if neoforge || forge
        return ModList.get().isLoaded(modid);
    }

    public static File configDir() {
        //? if fabric
        //return FabricLoader.getInstance().getConfigDir().toFile();
        //? if neoforge || forge
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    // utility

    public static MutableComponent translatableComponent(String key) {
        //? if >=1.19 {
        return Component.translatable(key);
        //?} else
        //return new TranslatableComponent(key);
    }
    public static MutableComponent literalComponent(String text) {
        //? if >=1.19 {
        return Component.literal(text);
        //?} else
        //return new TextComponent(text);
    }

    public static Identifier parseResource(String path){
        //? if >=1.21 {
        return Identifier.parse(path);
        //?} else
        //return new Identifier(path);
    }
}
