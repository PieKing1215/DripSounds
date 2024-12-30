package me.pieking1215.dripsounds;

import net.minecraft.client.KeyMapping;

import java.io.File;
import java.util.Optional;

public class DripSoundsNoOp extends DripSounds {
    @Override
    public boolean hasMod(String modid) {
        return false;
    }

    @Override
    public File configDir() {
        return null;
    }
}
