package me.pieking1215.waterdripsound;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WaterDripSoundConfig {
    public static final General GENERAL = new General();

    public static File configFile;

    public static class General {
        public final AtomicBoolean enabled = new AtomicBoolean(true);
        public final AtomicDouble volume = new AtomicDouble(0.3);
        public final AtomicInteger dripChance = new AtomicInteger(10);
        public final AtomicBoolean useDripstoneSounds = new AtomicBoolean(true);
        public final AtomicReference<SoundCategory> soundCategory = new AtomicReference<>(SoundCategory.AMBIENT);
    }

    public static void doneLoading(){
        try {
            File f = new File(FabricLoader.getInstance().getConfigDir().toFile(), "waterdripsound.json");
            f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();
            configFile = f;

            JsonReader jr = new JsonReader(new FileReader(f));
            JsonElement jp = JsonParser.parseReader(jr);
            if (jp.isJsonObject()) {
                JsonObject obj = jp.getAsJsonObject();
                if(obj.has("enabled")) GENERAL.enabled.set(obj.get("enabled").getAsBoolean());
                if(obj.has("volume")) GENERAL.volume.set(obj.get("volume").getAsDouble());
                if(obj.has("dripChance")) GENERAL.dripChance.set(obj.get("dripChance").getAsInt());
                if(obj.has("useDripstoneSounds")) GENERAL.useDripstoneSounds.set(obj.get("useDripstoneSounds").getAsBoolean());
                if(obj.has("soundCategory")) GENERAL.soundCategory.set(
                    Arrays.stream(SoundCategory.values())
                        .filter(c -> c.getName().equals(obj.get("soundCategory").getAsString()))
                        .findFirst()
                        .orElse(SoundCategory.AMBIENT));
            }
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Screen registerClothConfig(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("config.waterdripsound.general"));
        builder.setDefaultBackgroundTexture(Identifier.ofVanilla("textures/block/mossy_stone_bricks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("key.waterdripsound.category"));
        general.addEntry(eb.startBooleanToggle(Text.translatable("config.waterdripsound.enable"), GENERAL.enabled.get()).setDefaultValue(true).setSaveConsumer(GENERAL.enabled::set).build());
        general.addEntry(eb.startIntSlider(Text.translatable("config.waterdripsound.volume"), (int)(GENERAL.volume.get() * 100), 0, 100).setDefaultValue(30).setTextGetter(integer -> Text.literal("Volume: " + integer + "%")).setSaveConsumer(integer -> GENERAL.volume.set(integer / 100.0)).build());
        general.addEntry(eb.startIntSlider(Text.translatable("config.waterdripsound.dripChance"), GENERAL.dripChance.get(), 1, 100).setDefaultValue(10).setTextGetter(integer -> Text.literal("One in " + integer)).setSaveConsumer(GENERAL.dripChance::set).build());
        general.addEntry(eb.startBooleanToggle(Text.translatable("config.waterdripsound.useDripstoneSounds"), GENERAL.useDripstoneSounds.get()).setDefaultValue(true).setTooltip(Text.translatable("tooltip.config.waterdripsound.useDripstoneSounds")).setSaveConsumer(GENERAL.useDripstoneSounds::set).build());
        general.addEntry(eb.startEnumSelector(Text.translatable("config.waterdripsound.soundCategory"), SoundCategory.class, GENERAL.soundCategory.get()).setDefaultValue(SoundCategory.AMBIENT).setEnumNameProvider(anEnum -> Text.translatable("soundCategory." + ((SoundCategory)anEnum).getName())).setSaveConsumer(GENERAL.soundCategory::set).build());

        builder.setSavingRunnable(() -> {
            try {
                if (configFile != null) {
                    configFile.getParentFile().mkdirs();
                    if (!configFile.exists()) configFile.createNewFile();
                    JsonWriter jw = new JsonWriter(new FileWriter(configFile));
                    jw.setIndent("  ");
                    jw.beginObject();

                    jw.name("enabled").value(GENERAL.enabled.get());
                    jw.name("volume").value(GENERAL.volume.get());
                    jw.name("dripChance").value(GENERAL.dripChance.get());
                    jw.name("useDripstoneSounds").value(GENERAL.useDripstoneSounds.get());
                    jw.name("soundCategory").value(GENERAL.soundCategory.get().getName());

                    jw.endObject();
                    jw.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        });

        return builder.build();

    }

}