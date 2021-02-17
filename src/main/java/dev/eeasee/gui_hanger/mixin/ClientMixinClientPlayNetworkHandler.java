package dev.eeasee.gui_hanger.mixin;

import dev.eeasee.gui_hanger.network.GUIHangerClient;
import dev.eeasee.gui_hanger.network.GUIHangerClientNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientMixinClientPlayNetworkHandler {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onOnCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        if (GUIHangerClient.HUD_HANGER_CHANNEL.equals(packet.getChannel())) {
            GUIHangerClientNetworkHandler.handleData(packet.getData(), (ClientPlayNetworkHandler) (Object) this);
            ci.cancel();
        }
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoined(GameJoinS2CPacket packet, CallbackInfo info) {
        GUIHangerClient.gameJoined();
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onCMDisconnected(Text reason, CallbackInfo ci) {
        GUIHangerClient.disconnect();
    }

}
