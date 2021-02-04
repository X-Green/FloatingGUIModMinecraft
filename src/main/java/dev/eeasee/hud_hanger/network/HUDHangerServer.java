package dev.eeasee.hud_hanger.network;

import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class HUDHangerServer {
    public static void sendToAllPlayers(Packet<ClientPlayPacketListener> packet, MinecraftServer server) {
        for (ServerPlayerEntity playerEntity: server.getPlayerManager().getPlayerList()) {
            playerEntity.networkHandler.sendPacket(packet);
        }
    }
}
