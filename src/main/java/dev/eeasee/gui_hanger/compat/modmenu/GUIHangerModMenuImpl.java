package dev.eeasee.gui_hanger.compat.modmenu;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.config.ConfigScreen;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class GUIHangerModMenuImpl implements ModMenuApi {
    @Override
    public String getModId() {
        return GUIHangerMod.MOD_ID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new ConfigScreen();
    }
}
