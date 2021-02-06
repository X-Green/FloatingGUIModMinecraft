package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.GUIHangerMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GUIHangerServerNetworkHandler {
    private Set<UUID> validPlayers = new HashSet<>();

    private final MinecraftServer minecraftServer;

    GUIHangerServerNetworkHandler(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    public void handleData(PacketByteBuf data, ServerPlayerEntity player) {
        if (data != null) {
            byte id = data.readByte();
            if (id == GUIHangerClient.HELLO)
                this.onHello(player, data);
            if (id == GUIHangerClient.DATA)
                this.onClientData(player, data);
        }
    }

    public void onPlayerJoin(ServerPlayerEntity playerEntity) {
        PacketByteBuf buffer = (new PacketByteBuf(Unpooled.buffer()));
        buffer.writeByte(GUIHangerClient.HI);
        playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(
                GUIHangerClient.HUD_HANGER_CHANNEL, buffer
        ));
    }

    public void onHello(ServerPlayerEntity playerEntity, PacketByteBuf packetData) {
        this.validPlayers.add(playerEntity.getUuid());
        GUIHangerMod.LOGGER.info(playerEntity.getNameAndUuid().asString() + "logged in with HUDHanger Client");
    }

    private void onClientData(ServerPlayerEntity player, PacketByteBuf data) {

    }


    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        this.validPlayers.remove(player.getUuid());
    }

    public void close() {
        this.validPlayers.clear();
    }

    public boolean isValidPlayer(ServerPlayerEntity player) {
        return this.validPlayers.contains(player.getUuid());
    }

    public void sendToAllValidPlayers(Packet<ClientPlayPacketListener> packet) {
        PlayerManager manager = this.minecraftServer.getPlayerManager();
        for (UUID uuid : this.validPlayers) {
            ServerPlayerEntity playerEntity = manager.getPlayer(uuid);
            if (playerEntity == null) {
                this.validPlayers.remove(uuid);
                continue;
            }
            playerEntity.networkHandler.sendPacket(packet);
        }
    }
}
