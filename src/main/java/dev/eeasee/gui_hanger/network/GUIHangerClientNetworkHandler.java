package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.sprites.SpriteManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIHangerClientNetworkHandler {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void handleData(PacketByteBuf data) {
        if (data != null) {
            try {
                byte id = data.readByte();
                System.out.println("!!!!!!!!!!!!!!!!!id = " + id);
                if (id == GUIHangerClient.HI)
                    onHi(data);
                if (id == GUIHangerClient.DATA)
                    onSyncData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        LOGGER.info("Connected to a HUDHanger Server");
    }

    private static void onSyncData(PacketByteBuf data) {
        while (true) {
            try {
                int id = data.readInt();
                if (id < 0) {
                    switch (id) {
                        case -1:
                            return;
                        case -2: // Sprite Creating
                            int newID = data.readVarInt();
                            SpriteManager.ACTIVE_SPRITES.put(
                                    newID,
                                    SpriteType.values()[data.readUnsignedByte()].generateSprite(newID)
                            );
                            break;
                        case -3: // Sprite Removing
                            SpriteManager.ACTIVE_SPRITES.remove(data.readInt());
                            break;
                        default:
                            LOGGER.error("Handling GUIHanger Network Packet");
                            return;
                    }
                } else {
                    if (SpriteManager.ACTIVE_SPRITES.containsKey(id)) {
                        // Operating on sprite with the ID read.
                        SpriteManager.ACTIVE_SPRITES.get(id).readPacketBytes(data);
                    } else {
                        if (data.readByte() == SpriteProperty.ID_CREATE) {
                            int newID = data.readVarInt();
                            SpriteManager.ACTIVE_SPRITES.put(
                                    newID,
                                    SpriteType.values()[data.readUnsignedByte()].generateSprite(newID)
                            );
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        }
    }

    private static void verifySprite(int id) {
        //todo: ask the server to send the sprite again.
    }
}
