package dev.eeasee.gui_hanger.compat.modmenu;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.config.ConfigScreen;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public String getModId() {
        return GUIHangerMod.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return screen -> new ConfigScreen();
        // return null;
    }
}
