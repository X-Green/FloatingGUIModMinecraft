package dev.eeasee.gui_hanger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIHangerMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        System.out.println("Reeeeee!");
    }

    public static void tellPlayer(Object o) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText(o.toString()), true);
        }
    }
}
