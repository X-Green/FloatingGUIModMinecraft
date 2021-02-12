package dev.eeasee.gui_hanger;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.eeasee.gui_hanger.network.GUIHangerClient;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.command.arguments.Vec3ArgumentType;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GUIHangerMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "gui_hanger";

    @Override
    public void onInitialize() {
        System.out.println("Reeeeee!");
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> command = literal("test_gui_hanger")
                    .executes(context -> {
                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                        Vec3d vec3d = context.getSource().getPosition().add(2, 2, 2);

                        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                        packetByteBuf.writeByte(GUIHangerClient.DATA);

                        packetByteBuf.writeInt(0);

                        SpriteProperty.POSITION.writePacketBytes(packetByteBuf, new Vector3f((float) vec3d.x, (float) vec3d.y, (float) vec3d.z));
                        SpriteProperty.YAW_PITCH.writePacketBytes(packetByteBuf, new Vec2f(playerEntity.yaw, playerEntity.pitch));
                        SpriteProperty.NULL.writePacketBytes(packetByteBuf, null);

                        packetByteBuf.writeInt(-1);

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

                                SpriteProperty.POSITION.writePacketBytes(packetByteBuf, new Vector3f((float) vec3d.x, (float) vec3d.y, (float) vec3d.z));
                                SpriteProperty.YAW_PITCH.writePacketBytes(packetByteBuf, new Vec2f(0, 0));
                                SpriteProperty.NULL.writePacketBytes(packetByteBuf, null);

                                packetByteBuf.writeInt(-1);

                                CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(GUIHangerClient.HUD_HANGER_CHANNEL, packetByteBuf);

                                context.getSource().getMinecraftServer().getPlayerManager().sendToAll(packet);
                                return 1;
                            }));
            dispatcher.register(command);
        });
    }

    public static void tellPlayer(Object o) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText(o.toString()), true);
        }
    }
}
