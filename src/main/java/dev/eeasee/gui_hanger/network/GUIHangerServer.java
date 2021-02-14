package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.CraftingTableSprite;
import io.netty.buffer.Unpooled;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class GUIHangerServer {

    public final MinecraftServer minecraftServer;
    public final GUIHangerServerNetworkHandler networkHandler;

    public GUIHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new GUIHangerServerNetworkHandler(server, this);
    }

    public void tick() {

    }

    public void onServerClosed() {
        this.networkHandler.close();
    }
}
