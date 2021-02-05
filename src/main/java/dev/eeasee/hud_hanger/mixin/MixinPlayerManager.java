package dev.eeasee.hud_hanger.mixin;

import dev.eeasee.hud_hanger.fakes.IMinecraftServer;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnected(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ((IMinecraftServer) this.server).getHUDHangerServer().networkHandler.onPlayerJoin(player);
    }
}
