package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.GUIHangerMod;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

public class GUIHangerClientNetworkHandler {

    public static void handleData(PacketByteBuf data) {
        if (data != null) {
            byte id = data.readByte();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!id=" + id);
            if (id == GUIHangerClient.HI)
                onHi(data);
            if (id == GUIHangerClient.DATA)
                onSyncData(data);
        }
    }

    private static void onHi(PacketByteBuf data) {
        synchronized (GUIHangerClient.sync) {
            GUIHangerClient.isServerSupported = true;
            if (GUIHangerClient.gameJoined) {
                if (MinecraftClient.getInstance().player != null) {
                    respondHello(MinecraftClient.getInstance().player);
                }
            }
        }
    }

    public static void respondHello(ClientPlayerEntity clientPlayerEntity) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeByte(GUIHangerClient.HELLO);
        clientPlayerEntity.networkHandler.sendPacket(new CustomPayloadC2SPacket(
                GUIHangerClient.HUD_HANGER_CHANNEL, buffer));
        GUIHangerMod.LOGGER.info("Connected to a HUDHanger Server");
    }

    private static void onSyncData(PacketByteBuf data) {
        int objectID = data.readVarInt();
        while (true) {
            int key = data.readByte();
            if (key <= 0) {
                break;
            }
            PacketDataType dataType = PacketDataType.VALUES[key];
            dataType.packetReader.accept(data, objectID);
        }
    }
}
