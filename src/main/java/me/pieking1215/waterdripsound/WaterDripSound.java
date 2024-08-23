package me.pieking1215.waterdripsound;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class WaterDripSound implements ClientModInitializer {

    public static final Identifier LAVA_DRIP_ID = Identifier.of("waterdripsound", "lavadrip");
    public static SoundEvent LAVA_DRIP_EVENT = SoundEvent.of(LAVA_DRIP_ID);

    @Override
    public void onInitializeClient() {
        WaterDripSoundConfig.doneLoading();

        Registry.register(Registries.SOUND_EVENT, LAVA_DRIP_ID, LAVA_DRIP_EVENT);
    }
}
