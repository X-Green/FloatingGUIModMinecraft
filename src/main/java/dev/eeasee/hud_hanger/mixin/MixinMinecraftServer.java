package dev.eeasee.hud_hanger.mixin;

import com.google.gson.JsonElement;
import dev.eeasee.hud_hanger.fakes.IMinecraftServer;
import dev.eeasee.hud_hanger.network.HUDHangerServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements IMinecraftServer {
    private HUDHangerServer hudHangerServer;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void onTick(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        this.hudHangerServer.tick();
    }

    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void serverLoaded(String string_1, String string_2, long long_1, LevelGeneratorType levelGeneratorType_1, JsonElement jsonElement_1, CallbackInfo ci) {
        this.hudHangerServer = new HUDHangerServer((MinecraftServer) (Object) this);

    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void serverClosed(CallbackInfo ci) {
        this.hudHangerServer.onServerClosed();
    }

    @Override
    public HUDHangerServer getHUDHangerServer() {
        return this.hudHangerServer;
    }

}
