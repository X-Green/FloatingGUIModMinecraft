package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.AnvilSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class GUIHangerClient {
    public static final Object sync = new Object();
    public static boolean gameJoined = false;
    public static boolean isServerSupported = false;
    public static final Identifier HUD_HANGER_CHANNEL = new Identifier("h_h:c");
    private static final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();

    public static final byte HI = 2;
    public static final byte HELLO = 3;
    public static final byte CLIENT_INIT = 4;
    public static final byte DATA = 5;

    public static void gameJoined() {
        /*
        synchronized (sync) {
            // client didn't say hi back yet
            if (isServerSupported && MINECRAFT_CLIENT.player != null) {
                GUIHangerClientNetworkHandler.respondHello(MINECRAFT_CLIENT.player);
            }

        }
         */
        gameJoined = true;
    }

    public static void disconnect() {
        gameJoined = false;
    }
}
