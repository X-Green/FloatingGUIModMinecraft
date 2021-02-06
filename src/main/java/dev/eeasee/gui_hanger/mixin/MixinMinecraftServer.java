package dev.eeasee.gui_hanger.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import dev.eeasee.gui_hanger.fakes.IMinecraftServer;
import dev.eeasee.gui_hanger.network.GUIHangerServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.UserCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements IMinecraftServer {
    private GUIHangerServer GUIHangerServer;

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
        this.GUIHangerServer.tick();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void serverInit(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName, CallbackInfo ci) {
        this.GUIHangerServer = new GUIHangerServer((MinecraftServer) (Object) this);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void serverClosed(CallbackInfo ci) {
        this.GUIHangerServer.onServerClosed();
    }

    @Override
    public GUIHangerServer getHUDHangerServer() {
        return this.GUIHangerServer;
    }

}
