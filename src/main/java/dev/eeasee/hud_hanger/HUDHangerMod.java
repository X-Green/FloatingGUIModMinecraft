package dev.eeasee.hud_hanger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class HUDHangerMod implements ModInitializer {
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