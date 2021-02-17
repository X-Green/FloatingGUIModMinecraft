package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIHangerClientNetworkHandler {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void handleData(PacketByteBuf data, ClientPlayNetworkHandler clientPlayNetworkHandler) {
        if (data != null) {
            try {
                byte id = data.readByte();
                System.out.println("!!!!!!!!!!!!!!!!!id = " + id);
                if (id == GUIHangerClient.HI)
                    onHi(data, clientPlayNetworkHandler);
                if (id == GUIHangerClient.DATA)
                    onSyncData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void onHi(PacketByteBuf data, ClientPlayNetworkHandler clientPlayNetworkHandler) {
        GUIHangerClient.isServerSupported = true;
        respondHello(clientPlayNetworkHandler);
    }

    private static void respondHello(ClientPlayNetworkHandler clientPlayNetworkHandler) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeByte(GUIHangerClient.HELLO);
        clientPlayNetworkHandler.sendPacket(new CustomPayloadC2SPacket(
                GUIHangerClient.HUD_HANGER_CHANNEL, buffer));
        LOGGER.info("Connected to a HUDHanger Server");
    }

    private static void onSyncData(PacketByteBuf data) {
        while (true) {
            try {
                int id = data.readVarInt();
                if (SpriteManager.ACTIVE_SPRITES.containsKey(id)) {
                    // Operating on sprite with the ID read.
                    boolean success = SpriteManager.ACTIVE_SPRITES.get(id).readPacket(data);
                    if (!success) {
                        LOGGER.error("Error handling data on " + SpriteManager.ACTIVE_SPRITES.get(id).getSpriteName() + "@" + id);
                        return;
                    }
                } else {
                    if (data.readUnsignedByte() == SpriteProperty.PropertyType.CREATE.ordinal()) {
                        BaseSprite newSprite = SpriteType.values()[data.readUnsignedByte()].generateSprite(id);
                        SpriteManager.ACTIVE_SPRITES.put(id, newSprite);
                        boolean success = newSprite.readPacket(data);
                        if (!success) {
                            LOGGER.error("Error handling data on " + newSprite.getSpriteName() + "@" + id);
                            return;
                        }
                    } else {
                        return;
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
