package dev.eeasee.hud_hanger.mixin;

import dev.eeasee.hud_hanger.fakes.CustomPayloadC2SPacketInterface;
import dev.eeasee.hud_hanger.fakes.IMinecraftServer;
import dev.eeasee.hud_hanger.network.HUDHangerClient;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomCarpetPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        Identifier channel = ((CustomPayloadC2SPacketInterface) packet).getPacketChannel();
        if (HUDHangerClient.HUD_HANGER_CHANNEL.equals(channel)) {
            ((IMinecraftServer) (MinecraftServer) this.server).getHUDHangerServer().networkHandler.handleData(((CustomPayloadC2SPacketInterface) packet).getPacketData(), player);
            ci.cancel();
        }
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onPlayerDisconnect(Text reason, CallbackInfo ci) {
        ((IMinecraftServer) this.server).getHUDHangerServer().networkHandler.onPlayerLoggedOut(this.player);
    }
}
