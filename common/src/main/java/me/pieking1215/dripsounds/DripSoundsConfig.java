package me.pieking1215.dripsounds;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DripSoundsConfig {

    public static final General GENERAL = new General();

    public static File configFile;

    public static class General {
        public final AtomicBoolean enabled = new AtomicBoolean(true);
        public final AtomicDouble volume = new AtomicDouble(0.3);
        public final AtomicInteger dripChance = new AtomicInteger(10);
        public final AtomicDouble blockAnimateRangeMultiplier = new AtomicDouble(1.0);
        public final AtomicDouble blockAnimateCountMultiplier = new AtomicDouble(1.0);
        public final AtomicBoolean useDripstoneSounds = new AtomicBoolean(true);
        public final AtomicReference<SoundSource> soundCategory = new AtomicReference<>(SoundSource.AMBIENT);
    }

    public static Screen setupCloth(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(DripSounds.instance().translatableComponent("config.waterdripsound.general"));
        builder.setDefaultBackgroundTexture(DripSounds.instance().parseResource("minecraft:textures/block/mossy_stone_bricks.png"));
        builder.transparentBackground();

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(DripSounds.instance().translatableComponent("key.waterdripsound.category"));
        general.addEntry(eb.startBooleanToggle(DripSounds.instance().translatableComponent("config.waterdripsound.enable"), GENERAL.enabled.get()).setDefaultValue(true).setSaveConsumer(GENERAL.enabled::set).build());
        general.addEntry(eb.startIntSlider(DripSounds.instance().translatableComponent("config.waterdripsound.volume"), (int)(GENERAL.volume.get() * 100), 0, 100).setDefaultValue(30).setTextGetter(integer -> DripSounds.instance().literalComponent("Volume: " + integer + "%")).setSaveConsumer(integer -> GENERAL.volume.set(integer / 100.0)).build());
        general.addEntry(eb.startEnumSelector(DripSounds.instance().translatableComponent("config.waterdripsound.soundCategory"), SoundSource.class, GENERAL.soundCategory.get()).setDefaultValue(SoundSource.AMBIENT).setEnumNameProvider(anEnum -> DripSounds.instance().translatableComponent("soundCategory." + ((SoundSource)anEnum).getName())).setSaveConsumer(GENERAL.soundCategory::set).build());
//        general.addEntry(eb.startBooleanToggle(DripSounds.instance().translatableComponent("config.waterdripsound.useDripstoneSounds"), GENERAL.useDripstoneSounds.get()).setDefaultValue(true).setTooltip(DripSounds.instance().translatableComponent("tooltip.config.waterdripsound.useDripstoneSounds")).setSaveConsumer(GENERAL.useDripstoneSounds::set).build());
        general.addEntry(eb.startIntSlider(DripSounds.instance().translatableComponent("config.waterdripsound.dripChance"), GENERAL.dripChance.get(), 1, 100).setDefaultValue(10).setTextGetter(integer -> DripSounds.instance().literalComponent("One in " + integer)).setTooltip(DripSounds.instance().translatableComponent("tooltip.config.waterdripsound.dripChance")).setSaveConsumer(GENERAL.dripChance::set).build());

        boolean blockAnimateEnabled = !DripSounds.instance().hasMod("effective");
        general.addEntry(handleDisabling(
            eb.startIntSlider(DripSounds.instance().translatableComponent("config.waterdripsound.blockAnimateRangeMultiplier"), (int)(GENERAL.blockAnimateRangeMultiplier.get() / 0.25), 1, (int)(4.0 / 0.25)).setDefaultValue((int)(1.0 / 0.25)).setTextGetter(integer -> DripSounds.instance().literalComponent(integer * 0.25f + "x (" + (int)(integer * 0.25f * 32) + " Blocks)")).setSaveConsumer(integer -> GENERAL.blockAnimateRangeMultiplier.set(integer * 0.25)),
            () -> Optional.ofNullable(blockAnimateEnabled ? null : DripSounds.instance().translatableComponent("config.waterdripsound.blockAnimateNotSupported.effective")),
            DripSounds.instance().translatableComponent("tooltip.config.waterdripsound.blockAnimateRangeMultiplier")
        ));

        general.addEntry(handleDisabling(
            eb.startIntSlider(DripSounds.instance().translatableComponent("config.waterdripsound.blockAnimateCountMultiplier"), (int)(GENERAL.blockAnimateCountMultiplier.get() / 0.25), 0, (int)(4.0 * 4.0 / 0.25)).setDefaultValue((int)(1.0 / 0.25)).setTextGetter(integer -> DripSounds.instance().literalComponent(integer * 0.25f + "x")).setSaveConsumer(integer -> GENERAL.blockAnimateCountMultiplier.set(integer * 0.25)),
            () -> Optional.ofNullable(blockAnimateEnabled ? null : DripSounds.instance().translatableComponent("config.waterdripsound.blockAnimateNotSupported.effective")),
            DripSounds.instance().translatableComponent("tooltip.config.waterdripsound.blockAnimateCountMultiplier")
        ));

        // save

        return builder.setSavingRunnable(DripSoundsConfig::save).build();
    }

    private static AbstractConfigListEntry<Integer> handleDisabling(IntSliderBuilder entry, Supplier<Optional<MutableComponent>> getDisable, MutableComponent normalTooltip) {
        Optional<MutableComponent> disableReason = getDisable.get();
        disableReason.ifPresent(c -> {
            entry.setTooltip(
                    DripSounds.instance().translatableComponent("config.waterdripsound.blockAnimateNotSupported"),
                    c,
                    DripSounds.instance().literalComponent(" "),
                    normalTooltip);
        });

        AbstractConfigListEntry<Integer> built = entry.build();

        disableReason.ifPresent(c -> {
            built.setEditable(false);
        });

        return built;
    }

    public static void save() {
        try {
            File configDir = DripSounds.instance().configDir();
            if (configDir != null) {
                // main config

                File configFile = new File(configDir, "waterdripsound.json");

                if (!configFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();
                }

                if (configFile.exists()) {
                    writeMainConfig(configFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            File configDir = DripSounds.instance().configDir();
            if (configDir != null) {
                // main config

                File configFile = new File(configDir, "waterdripsound.json");

                if (!configFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();

                    if (configFile.exists()) {
                        writeMainConfig(configFile);
                    }
                }

                if (configFile.exists()) {
                    readMainConfig(configFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeMainConfig(File file) throws IOException {
        JsonWriter jw = new JsonWriter(new FileWriter(file));
        jw.setIndent("  ");
        jw.beginObject();

        jw.name("enabled").value(GENERAL.enabled.get());
        jw.name("volume").value(GENERAL.volume.get());
        jw.name("dripChance").value(GENERAL.dripChance.get());
        jw.name("useDripstoneSounds").value(GENERAL.useDripstoneSounds.get());
        jw.name("soundCategory").value(GENERAL.soundCategory.get().getName());
        jw.name("blockAnimateRangeMultiplier").value(GENERAL.blockAnimateRangeMultiplier.get());
        jw.name("blockAnimateCountMultiplier").value(GENERAL.blockAnimateCountMultiplier.get());

        jw.endObject();
        jw.close();
    }

    private static void readMainConfig(File file) throws IOException {
        JsonReader jr = new JsonReader(new FileReader(file));
        JsonElement jsonEl = new Gson().fromJson(jr, JsonElement.class);
        jr.close();
        if (jsonEl != null) {
            JsonObject json = jsonEl.getAsJsonObject();
            if(json.has("enabled")) GENERAL.enabled.set(json.get("enabled").getAsBoolean());
            if(json.has("volume")) GENERAL.volume.set(json.get("volume").getAsDouble());
            if(json.has("dripChance")) GENERAL.dripChance.set(json.get("dripChance").getAsInt());
            if(json.has("useDripstoneSounds")) GENERAL.useDripstoneSounds.set(json.get("useDripstoneSounds").getAsBoolean());
            if(json.has("soundCategory")) GENERAL.soundCategory.set(
                Arrays.stream(SoundSource.values())
                        .filter(c -> c.getName().equals(json.get("soundCategory").getAsString()))
                        .findFirst()
                        .orElse(SoundSource.AMBIENT));
            if(json.has("blockAnimateRangeMultiplier")) GENERAL.blockAnimateRangeMultiplier.set(json.get("blockAnimateRangeMultiplier").getAsDouble());
            if(json.has("blockAnimateCountMultiplier")) GENERAL.blockAnimateCountMultiplier.set(json.get("blockAnimateCountMultiplier").getAsDouble());
        }
    }
}
