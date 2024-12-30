package me.pieking1215.dripsounds.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pieking1215.dripsounds.DripSoundsConfig;

public class DripSoundsModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return DripSoundsConfig::setupCloth;
    }
}