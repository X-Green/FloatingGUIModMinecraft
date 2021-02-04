package dev.eeasee.hud_hanger.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private ClientWorld world;

    @Shadow
    protected abstract void method_22978(BufferBuilder bufferBuilder, double d, double e, double f, double g, int i, double h, float j, float k);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;", shift = At.Shift.AFTER))
    private void beforeBlockEntityRendered(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

        BlockPos blockPos = new BlockPos(-60, 78, -216);
        Vec3d cameraPos = camera.getPos();

        float vx = blockPos.getX() - (float) cameraPos.x;
        float vy = blockPos.getY() - (float) cameraPos.y + 5;
        float vz = blockPos.getZ() - (float) cameraPos.z;

        matrices.push();
        matrices.translate(vx, vy + 10, vz);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                null,
                Items.JACK_O_LANTERN.getStackForRender(),
                ModelTransformation.Mode.GROUND,
                false,
                matrices,
                immediate,
                null,
                0x00F000F0,
                OverlayTexture.DEFAULT_UV
        );
        matrices.pop();

    }

    @Inject(method = "render",
            at = @At(value = "INVOKE_STRING", args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void onRenderWorldLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();

        RenderSystem.depthMask(false);
        RenderSystem.pushMatrix();
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();


        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        this.textureManager.bindTexture(new Identifier("textures/gui/container/crafting_table.png"));
        BlockPos blockPos = new BlockPos(-60, 78, -216);

        float vx = blockPos.getX() - (float) cameraPos.x;
        float vy = blockPos.getY() - (float) cameraPos.y + 5;
        float vz = blockPos.getZ() - (float) cameraPos.z;

        float yaw = 142.2f;
        float pitch = -8.3f;

        yaw = camera.getYaw();
        pitch = camera.getPitch();


        Vector4f[] vector4f = new Vector4f[]{
                new Vector4f(1, 0, 0, 1),
                new Vector4f(-1, 0, 0, 1),
                new Vector4f(-1, 2, 0, 1),
                new Vector4f(1, 2, 0, 1),
        };

        Matrix4f rotation = new Matrix4f(Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw));

        rotation.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));

        Matrix4f translation = Matrix4f.translate(vx, vy, vz);

        for (Vector4f v : vector4f) {
            v.transform(rotation);
            v.transform(translation);
        }

        bufferBuilder.vertex(vector4f[0].getX(), vector4f[0].getY(), vector4f[0].getZ()).texture(0, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[1].getX(), vector4f[1].getY(), vector4f[1].getZ()).texture(1, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[2].getX(), vector4f[2].getY(), vector4f[2].getZ()).texture(1, 0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[3].getX(), vector4f[3].getY(), vector4f[3].getZ()).texture(0, 0).color(255, 255, 255, 255).next();


        Tessellator.getInstance().draw();

        RenderSystem.popMatrix();

        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

    }
}
