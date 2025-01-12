package me.pieking1215.dripsounds;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

//? if <1.19
/*import net.minecraft.network.chat.TextComponent;*/
//? if <1.19
/*import net.minecraft.network.chat.TranslatableComponent;*/

import net.minecraft.resources.ResourceLocation;

import java.io.File;

public abstract class DripSounds {
    public static final String MOD_ID = "waterdripsound";

    private static DripSounds instance;

    public static DripSounds instance() {
        if (instance == null) {
            instance = new DripSoundsNoOp();
        }

        return instance;
    }

    public static void setInstance(DripSounds newInstance) {
        instance = newInstance;
    }

    // loader compatibility layer

    public abstract boolean hasMod(String modid);
    public abstract File configDir();

    // utility

    public MutableComponent translatableComponent(String key) {
        //? if >=1.19 {
        return Component.translatable(key);
        //?} else
        /*return new TranslatableComponent(key);*/
    }
    public MutableComponent literalComponent(String text) {
        //? if >=1.19 {
        return Component.literal(text);
         //?} else
        /*return new TextComponent(text);*/
    }

    public ResourceLocation parseResource(String path){
        //? if >=1.21 {
        return ResourceLocation.parse(path);
        //?} else
        /*return new ResourceLocation(path);*/
    }

    // implementation

    public DripSounds() {

    }

    public void finishInit(){
        DripSoundsConfig.load();
    }

}
