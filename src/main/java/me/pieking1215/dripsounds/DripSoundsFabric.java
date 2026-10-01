package me.pieking1215.dripsounds;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;

public class DripSoundsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DripSounds.finishInit();
    }
}
//?}