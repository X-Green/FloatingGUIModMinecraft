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
        GUIHangerMod.LOGGER.info(playerEntity.getEntityName() + " logged in with HUDHanger Client");
        //todo: del this
        /*
        CraftingTableSprite craftingTableSprite = new CraftingTableSprite(0);
        craftingTableSprite.setPos(new BlockPos(-60, 82, -216));
        craftingTableSprite.setMouse(176, 166);
        craftingTableSprite.setYawPitch(0, 0);
        SpriteManager.ACTIVE_SPRITES.put(0, craftingTableSprite);
        craftingTableSprite.getItems().put(0, Items.WOODEN_SWORD);
        craftingTableSprite.getItems().put(1, Items.STONE_SWORD);
        craftingTableSprite.getItems().put(2, Items.IRON_SWORD);
        craftingTableSprite.getItems().put(3, Items.GOLDEN_SWORD);
        craftingTableSprite.getItems().put(4, Items.DIAMOND_SWORD);
        craftingTableSprite.getItems().put(8, Items.SUGAR);
        craftingTableSprite.getItems().put(9, Items.PINK_BED);
        craftingTableSprite.getItems().put(18, Items.GRASS_BLOCK);
        craftingTableSprite.getItems().put(27, Items.DIAMOND_BLOCK);
        craftingTableSprite.getItems().put(36, Items.IRON_INGOT);
        craftingTableSprite.getItems().put(37, Items.GOLD_INGOT);
        craftingTableSprite.getItems().put(38, Items.REDSTONE);
        craftingTableSprite.getItems().put(39, Items.EMERALD);
        craftingTableSprite.getItems().put(44, Items.DIAMOND);
        craftingTableSprite.getItems().put(45, Items.BEACON);
         */
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeByte(GUIHangerClient.DATA);

        packetByteBuf.writeInt(-2);
        packetByteBuf.writeVarInt(0);
        packetByteBuf.writeByte(SpriteType.INVENTORY.ordinal());

        packetByteBuf.writeInt(0);

        SpriteProperty.POSITION.writePacketBytes(packetByteBuf, new Vector3f(-60, 82, -216));
        SpriteProperty.YAW_PITCH.writePacketBytes(packetByteBuf, new Vec2f(0, 0));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(0, Items.WOODEN_SWORD));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(1, Items.STONE_SWORD));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(2, Items.IRON_SWORD));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(3, Items.GOLDEN_SWORD));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(4, Items.DIAMOND_SWORD));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(8, Items.SUGAR));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(9, Items.PINK_BED));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(18, Items.GRASS_BLOCK));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(27, Items.DIAMOND_BLOCK));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(36, Items.DIAMOND_HELMET));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(37, Items.ELYTRA));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(38, Items.DIAMOND_LEGGINGS));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(39, Items.DIAMOND_BOOTS));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(40, Items.COOKED_BEEF));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(41, Items.GUNPOWDER));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(44, Items.PAPER));
        SpriteProperty.ADD_ITEM.writePacketBytes(packetByteBuf, new Pair<>(45, Items.FIREWORK_ROCKET));
        SpriteProperty.NULL.writePacketBytes(packetByteBuf, null);

        packetByteBuf.writeInt(-1);

        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf);
        this.sendToAllValidPlayers(packet);


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
