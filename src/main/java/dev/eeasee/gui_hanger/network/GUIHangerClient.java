package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.render.HangedGUIRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class GUIHangerClient {
    public static final Object sync = new Object();
    public static boolean gameJoined = false;
    public static boolean isServerSupported = false;
    public static final Identifier HUD_HANGER_CHANNEL = new Identifier("h_h:c");
    private static final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();

    public static final byte HI = 69;
    public static final byte HELLO = 111;
    public static final byte DATA = 1;

    public static void gameJoined() {
        synchronized (sync) {
            // client didn't say hi back yet
            if (isServerSupported && MINECRAFT_CLIENT.player != null) {
                GUIHangerClientNetworkHandler.respondHello(MINECRAFT_CLIENT.player);
            }
            gameJoined = true;
        }
    }

    public static void disconnect() {
        gameJoined = false;
    }
}
