package dev.eeasee.gui_hanger.network;

import net.minecraft.util.PacketByteBuf;

import java.util.function.BiConsumer;

public enum PacketDataType {

    SPAWN(),

    DESTROY();

    public static final PacketDataType[] VALUES = PacketDataType.values();
}
