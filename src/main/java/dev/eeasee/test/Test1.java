package dev.eeasee.test;

import io.netty.buffer.Unpooled;
import net.minecraft.util.PacketByteBuf;

public class Test1 {
    private static volatile String s = "bbb";

    public static void main(String[] args) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeByte(-1);
        System.out.println(packetByteBuf.readUnsignedByte());
    }

    public static void printVarInt(int i) {
        while((i & -128) != 0) {
            System.out.println(i & 127 | 128);
            i >>>= 7;
        }

        System.out.println(i);
    }

}
