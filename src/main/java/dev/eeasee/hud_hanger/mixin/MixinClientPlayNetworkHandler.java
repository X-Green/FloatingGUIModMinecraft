package dev.eeasee.hud_hanger.mixin;

import dev.eeasee.hud_hanger.network.HUDHangerClientNetworkHandler;
import dev.eeasee.hud_hanger.network.HUDHangerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onOnCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        if (HUDHangerClient.HUD_HANGER_CHANNEL.equals(packet.getChannel())) {
            HUDHangerClientNetworkHandler.handleData(packet.getData());
            ci.cancel();
        }
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoined(GameJoinS2CPacket packet, CallbackInfo info) {
        HUDHangerClient.gameJoined();
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onCMDisconnected(Text reason, CallbackInfo ci) {
        HUDHangerClient.disconnect();
    }

}
