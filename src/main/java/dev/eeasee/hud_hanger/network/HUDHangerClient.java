package dev.eeasee.hud_hanger.network;

import dev.eeasee.hud_hanger.render.HangedGUIRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class HUDHangerClient {
    public static final Object sync = new Object();
    public static boolean gameJoined = false;
    public static boolean isServerSupported = false;
    public static final Identifier HUD_HANGER_CHANNEL = new Identifier("h_h:c");
    public static HangedGUIRenderManager renderManager = new HangedGUIRenderManager();
    private static final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();

    public static final byte HI = 69;
    public static final byte HELLO = 111;
    public static final byte DATA = 1;

    public static void gameJoined() {
        synchronized (sync) {
            // client didn't say hi back yet
            if (isServerSupported && MINECRAFT_CLIENT.player != null) {
                HUDHangerClientNetworkHandler.respondHello(MINECRAFT_CLIENT.player);
            }
            gameJoined = true;
        }
    }

    public static void disconnect() {
        gameJoined = false;
    }
}
