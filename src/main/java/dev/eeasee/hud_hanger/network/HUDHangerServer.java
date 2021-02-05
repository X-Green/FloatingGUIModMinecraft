package dev.eeasee.hud_hanger.network;

import net.minecraft.server.MinecraftServer;

public class HUDHangerServer {

    public final MinecraftServer minecraftServer;
    public final HUDHangerServerNetworkHandler networkHandler;

    public HUDHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new HUDHangerServerNetworkHandler(server);
    }

    public void tick() {

    }

    public void onServerClosed() {
        this.networkHandler.close();
    }
}
