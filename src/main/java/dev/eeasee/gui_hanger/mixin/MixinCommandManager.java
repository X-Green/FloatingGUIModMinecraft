package dev.eeasee.gui_hanger.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.eeasee.gui_hanger.network.GUIHangerClient;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import io.netty.buffer.Unpooled;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.command.arguments.Vec3ArgumentType;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(boolean boolean_1, CallbackInfo ci) {
        //todo: del this
        LiteralArgumentBuilder<ServerCommandSource> command = literal("test_gui_hanger")
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    Vec3d playerPos = context.getSource().getPosition();

                    PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                    packetByteBuf.writeByte(GUIHangerClient.DATA);

                    packetByteBuf.writeVarInt(0);

                    Vector4f spritePos = new Vector4f(0, 0, -1, 1);

                    Quaternion rotation = Vector3f.NEGATIVE_Y.getDegreesQuaternion(playerEntity.getYaw(0));
                    rotation.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(playerEntity.getPitch(0)));
                    rotation.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f));

                    spritePos.rotate(rotation);

                    SpriteProperty.POSITION.write(new Vector3f(
                                    (float) playerPos.x + spritePos.getX(),
                                    (float) playerPos.y + 1.65f + spritePos.getY(),
                                    (float) playerPos.z + spritePos.getZ()),
                            packetByteBuf);
                    SpriteProperty.YAW_PITCH.write(new Vec2f(playerEntity.yaw, playerEntity.pitch), packetByteBuf);
                    SpriteProperty.PropertyType.NULL.writeOrdinalToPacket(packetByteBuf);

                    packetByteBuf.writeVarInt(-1);

                    CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf);

                    context.getSource().getMinecraftServer().getPlayerManager().sendToAll(packet);
                    return 1;

                })
                .then(argument("position", Vec3ArgumentType.vec3())
                        .executes(context -> {
                            Vec3d vec3d = Vec3ArgumentType.getVec3(context, "position");

                            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                            packetByteBuf.writeByte(GUIHangerClient.DATA);

                            packetByteBuf.writeInt(0);

                            SpriteProperty.POSITION.write(new Vector3f((float) vec3d.x, (float) vec3d.y, (float) vec3d.z), packetByteBuf);
                            SpriteProperty.YAW_PITCH.write(new Vec2f(0, 0), packetByteBuf);
                            SpriteProperty.PropertyType.NULL.writeOrdinalToPacket(packetByteBuf);

                            packetByteBuf.writeInt(-1);

                            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf);

                            context.getSource().getMinecraftServer().getPlayerManager().sendToAll(packet);
                            return 1;
                        }));
        this.dispatcher.register(command);
    }
}
