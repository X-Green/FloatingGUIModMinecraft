package dev.eeasee.gui_hanger.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.gui_hanger.render.HangedGUIRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class ClientMixinWorldRenderer {
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

    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;", shift = At.Shift.AFTER))
    private void beforeBlockEntityRendered(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();

        BlockPos blockPos = new BlockPos(-60, 93, -216);
        Vec3d cameraPos = camera.getPos();

        Quaternion rotationQ = Vector3f.NEGATIVE_Y.getDegreesQuaternion(camera.getYaw());
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));


        Matrix4f origin = Matrix4f.translate(0, 1, 0);

        Matrix4f cameraTransformer = Matrix4f.translate(-(float) cameraPos.x, -(float) cameraPos.y, -(float) cameraPos.z);

        Matrix4f commonTransformer = Matrix4f.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        commonTransformer.multiply(rotationQ);
        commonTransformer.multiply(Matrix4f.scale(0.5f, 0.5f, 0.5f));

        cameraTransformer.multiply(commonTransformer);
        origin.multiply(cameraTransformer);

        matrices.push();

        matrices.peek().getModel().multiply(origin);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                null,
                Items.JACK_O_LANTERN.getStackForRender(),
                ModelTransformation.Mode.GUI,
                false,
                matrices,
                immediate,
                null,
                0x00f000f0,
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();

        HangedGUIRenderManager.renderModels(matrices, tickDelta, camera, gameRenderer, this.textureManager, this.bufferBuilders);


    }

    @Inject(method = "render",
            at = @At(value = "INVOKE_STRING", args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void onRenderWorldLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();

        HangedGUIRenderManager.renderFlat(matrices, tickDelta, camera, gameRenderer, this.textureManager);

        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }
}
