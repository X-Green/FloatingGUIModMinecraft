package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.sprites.renderer.AnvilSprite;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import io.netty.buffer.Unpooled;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class GUIHangerServerNetworkHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private final MinecraftServer minecraftServer;
    private final GUIHangerServer guiHangerServer;

    GUIHangerServerNetworkHandler(MinecraftServer minecraftServer, GUIHangerServer guiHangerServer) {
        this.minecraftServer = minecraftServer;
        this.guiHangerServer = guiHangerServer;
    }

    public void handleData(PacketByteBuf data, ServerPlayerEntity player) {
        // run on network thread
        if (data != null) {
            try {
                byte id = data.readByte();
                if (id == GUIHangerClient.HELLO)
                    this.onHello(player, data);
                if (id == GUIHangerClient.DATA)
                    this.onClientData(player, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onPlayerJoin(ServerPlayerEntity playerEntity) {
        PacketByteBuf buffer = (new PacketByteBuf(Unpooled.buffer()));
        buffer.writeByte(GUIHangerClient.HI);
        playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(
                GUIHangerClient.HUD_HANGER_CHANNEL, buffer
        ));
        AnvilSprite anvilSprite = new AnvilSprite(0);
        anvilSprite.setPos(new Vector3f(-60, 82, -216));
        anvilSprite.setItem(0, Items.WOODEN_SWORD);
        anvilSprite.setItem(1, Items.STONE_SWORD);
        anvilSprite.setItem(2, Items.IRON_SWORD);
        anvilSprite.setItem(3, Items.GOLDEN_SWORD);
        anvilSprite.setItem(4, Items.DIAMOND_SWORD);
        anvilSprite.setItem(8, Items.SUGAR);
        anvilSprite.setItem(9, Items.PINK_BED);
        anvilSprite.setItem(18, Items.GRASS_BLOCK);
        anvilSprite.setItem(27, Items.DIAMOND_BLOCK);
        anvilSprite.setItem(36, Items.DIAMOND_HELMET);
        anvilSprite.setItem(37, Items.ELYTRA);
        anvilSprite.setItem(38, Items.DIAMOND_LEGGINGS);
        anvilSprite.setItem(39, Items.DIAMOND_BOOTS);
        anvilSprite.setItem(40, Items.COOKED_BEEF);
        anvilSprite.setItem(41, Items.GUNPOWDER);
        anvilSprite.setCanFixItem(false);
    }

    private void onHello(ServerPlayerEntity playerEntity, PacketByteBuf packetData) {
        this.syncExistingSprites(playerEntity);
        this.guiHangerServer.addPlayer(playerEntity.getUuid());
        GUIHangerMod.LOGGER.info(playerEntity.getEntityName() + " logged in with a GUIHanger Client");
    }

    private void syncExistingSprites(ServerPlayerEntity playerEntity) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeByte(GUIHangerClient.DATA);
        for (BaseSprite sprite : this.guiHangerServer.getAllSpriteObjects()) {
            sprite.writeCreatePacket(sprite.getID(), packetByteBuf);
        }

        packetByteBuf.writeVarInt(-1);
        playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf));
    }

    private void onClientData(ServerPlayerEntity player, PacketByteBuf data) {
        while (true) {
            try {
                int id = data.readVarInt();

                if (id < 0) {
                    // allocate ID
                    continue;
                }

                if (this.guiHangerServer.getSpriteIDsFromPlayer(player.getUuid()).contains(id)) {
                    // Operating on sprite with the ID read.
                    boolean success = SpriteManager.ACTIVE_SPRITES.get(id).readPacket(data);
                    if (!success) {
                        LOGGER.error("Error handling data on " + SpriteManager.ACTIVE_SPRITES.get(id).getSpriteName() + "@" + id);
                        return;
                    }
                } else if (data.readUnsignedByte() == SpriteProperty.PropertyType.CREATE.ordinal()) {
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
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        }
    }


    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        if (this.guiHangerServer.containsPlayer(player.getUuid())) {
            this.guiHangerServer.removePlayer(player.getUuid());
        }
    }


    public void sendToAllValidPlayers(Packet<ClientPlayPacketListener> packet) {
        for (UUID uuid : this.guiHangerServer.getAvailablePlayerUUIDs()) {
            ServerPlayerEntity player = this.minecraftServer.getPlayerManager().getPlayer(uuid);
            if (player == null) {
                this.guiHangerServer.removePlayer(uuid);
            } else {
                player.networkHandler.sendPacket(packet);
            }
        }
    }
}
