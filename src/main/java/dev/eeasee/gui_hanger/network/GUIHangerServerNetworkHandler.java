package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import io.netty.buffer.Unpooled;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GUIHangerServerNetworkHandler {
    private Set<UUID> validPlayers = new HashSet<>();

    private final MinecraftServer minecraftServer;
    private final GUIHangerServer guiHangerServer;

    GUIHangerServerNetworkHandler(MinecraftServer minecraftServer, GUIHangerServer guiHangerServer) {
        this.minecraftServer = minecraftServer;
        this.guiHangerServer = guiHangerServer;
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
        GUIHangerMod.LOGGER.info(playerEntity.getEntityName() + " logged in with HUDHanger Client");
        this.syncExistingSprites(playerEntity);

        //todo: del this
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeByte(GUIHangerClient.DATA);

        packetByteBuf.writeVarInt(0);
        SpriteProperty.PropertyType.CREATE.writeOrdinalToPacket(packetByteBuf);
        packetByteBuf.writeByte(SpriteType.ANVIL.ordinal());

        SpriteProperty.POSITION.writePacketBytes(new Vector3f(-60, 82, -216), packetByteBuf);
        SpriteProperty.YAW_PITCH.writePacketBytes(new Vec2f(0, 0), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(0, Items.WOODEN_SWORD), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(1, Items.STONE_SWORD), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(2, Items.IRON_SWORD), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(3, Items.GOLDEN_SWORD), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(4, Items.DIAMOND_SWORD), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(8, Items.SUGAR), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(9, Items.PINK_BED), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(18, Items.GRASS_BLOCK), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(27, Items.DIAMOND_BLOCK), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(36, Items.DIAMOND_HELMET), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(37, Items.ELYTRA), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(38, Items.DIAMOND_LEGGINGS), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(39, Items.DIAMOND_BOOTS), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(40, Items.COOKED_BEEF), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(41, Items.GUNPOWDER), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(44, Items.PAPER), packetByteBuf);
        SpriteProperty.ADD_ITEM.writePacketBytes(new Pair<>(45, Items.FIREWORK_ROCKET), packetByteBuf);


        SpriteProperty.PropertyType.NULL.writeOrdinalToPacket(packetByteBuf);

        packetByteBuf.writeInt(-1);

        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf);
        this.sendToAllValidPlayers(packet);


    }

    private void syncExistingSprites(ServerPlayerEntity playerEntity) {

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
