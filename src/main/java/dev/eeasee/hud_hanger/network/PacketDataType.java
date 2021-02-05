package dev.eeasee.hud_hanger.network;

import net.minecraft.util.PacketByteBuf;

import java.util.function.BiConsumer;

public enum PacketDataType {

    SPAWN(PacketOperations::readSpawnPacket),

    DESTROY(PacketOperations::readDestroyPacket),

    UPDATE_POS(PacketOperations::readPosUpdatePacket),

    UPDATE_FACING(PacketOperations::readFacingUpdatePacket),

    UPDATE_MOUSE_COORD(PacketOperations::readMouseCoordUpdatePacket);

    public final BiConsumer<PacketByteBuf, Integer> packetReader;


    PacketDataType(BiConsumer<PacketByteBuf, Integer> packetByteConsumer) {
        this.packetReader = packetByteConsumer;
    }

    public static final PacketDataType[] VALUES = PacketDataType.values();
}
