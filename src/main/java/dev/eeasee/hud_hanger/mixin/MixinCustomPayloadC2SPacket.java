package dev.eeasee.hud_hanger.mixin;

import dev.eeasee.hud_hanger.fakes.CustomPayloadC2SPacketInterface;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadC2SPacket.class)
public abstract class MixinCustomPayloadC2SPacket implements CustomPayloadC2SPacketInterface {
    @Shadow
    private Identifier channel;
    @Shadow
    private PacketByteBuf data;

    @Override
    public Identifier getPacketChannel() {
        return channel;
    }

    @Override
    public PacketByteBuf getPacketData() {
        return new PacketByteBuf(this.data.copy());
    }
}
