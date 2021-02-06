package dev.eeasee.gui_hanger.network;

import net.minecraft.server.MinecraftServer;

public class GUIHangerServer {

    public final MinecraftServer minecraftServer;
    public final GUIHangerServerNetworkHandler networkHandler;

    public GUIHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new GUIHangerServerNetworkHandler(server);
    }

    public void tick() {

    }

    public void onServerClosed() {
        this.networkHandler.close();
    }
}
