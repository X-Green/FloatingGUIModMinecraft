package dev.eeasee.gui_hanger.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.gui_hanger.network.GUIHangerClient;
import dev.eeasee.gui_hanger.render.HangedGUIRenderManager;
import dev.eeasee.gui_hanger.render.renderer.CraftingTableRenderer;
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
import org.lwjgl.opengl.GL11;
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

        BlockPos blockPos = new BlockPos(-60, 78, -216);
        Vec3d cameraPos = camera.getPos();

        float vx = blockPos.getX() - (float) cameraPos.x;
        float vy = blockPos.getY() - (float) cameraPos.y + 5;
        float vz = blockPos.getZ() - (float) cameraPos.z;

        matrices.push();
        matrices.translate(vx, vy + 10, vz);

        float yaw = camera.getYaw();
        float pitch = camera.getPitch();

        Quaternion rotationYawPitch = Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw);
        rotationYawPitch.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));

        Quaternion rotationQ = Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw);
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));

        matrices.push();
        matrices.multiply(rotationQ);

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
        matrices.push();

        matrices.translate(5, 0, 0);

        matrices.multiply(rotationQ);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                Items.DIAMOND.getStackForRender(),
                ModelTransformation.Mode.GUI,
                0x00f000f0,
                OverlayTexture.DEFAULT_UV,
                matrices,
                immediate
        );

        matrices.pop();
        matrices.pop();

        HangedGUIRenderManager.renderModels(matrices, tickDelta, camera, gameRenderer, this.textureManager);


    }

    @Inject(method = "render",
            at = @At(value = "INVOKE_STRING", args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void onRenderWorldLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        Vec3d cameraPos = camera.getPos();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();

        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        this.textureManager.bindTexture(new Identifier("textures/gui/container/crafting_table.png"));
        BlockPos blockPos = new BlockPos(-60, 83, -216);

        float yaw = camera.getYaw();
        float pitch = camera.getPitch();


        Vector4f[] vector4f = new Vector4f[]{
                new Vector4f(-1, 0, 0, 1),
                new Vector4f(1, 0, 0, 1),
                new Vector4f(1, 2, 0, 1),
                new Vector4f(-1, 2, 0, 1),
        };

        Quaternion rotationQ = Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw);
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));
        rotationQ.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));

        Matrix4f transformer1 = Matrix4f.translate((float) blockPos.getX(), (float) blockPos.getY(), (float) blockPos.getZ());
        transformer1.multiply(rotationQ);

        for (Vector4f v : vector4f) {
            v.transform(transformer1);
        }

        /*
        bufferBuilder.vertex(vector4f[0].getX() - cameraPos.x, vector4f[0].getY() - cameraPos.y, vector4f[0].getZ() - cameraPos.z).texture(0, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[1].getX() - cameraPos.x, vector4f[1].getY() - cameraPos.y, vector4f[1].getZ() - cameraPos.z).texture(1, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[2].getX() - cameraPos.x, vector4f[2].getY() - cameraPos.y, vector4f[2].getZ() - cameraPos.z).texture(1, 0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[3].getX() - cameraPos.x, vector4f[3].getY() - cameraPos.y, vector4f[3].getZ() - cameraPos.z).texture(0, 0).color(255, 255, 255, 255).next();
         */

        bufferBuilder.vertex(vector4f[0].getX() - cameraPos.x, vector4f[0].getY() - cameraPos.y, vector4f[0].getZ() - cameraPos.z).texture(CraftingTableRenderer.BG_TEX_UV.vectors[0].x, CraftingTableRenderer.BG_TEX_UV.vectors[0].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[1].getX() - cameraPos.x, vector4f[1].getY() - cameraPos.y, vector4f[1].getZ() - cameraPos.z).texture(CraftingTableRenderer.BG_TEX_UV.vectors[1].x, CraftingTableRenderer.BG_TEX_UV.vectors[1].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[2].getX() - cameraPos.x, vector4f[2].getY() - cameraPos.y, vector4f[2].getZ() - cameraPos.z).texture(CraftingTableRenderer.BG_TEX_UV.vectors[2].x, CraftingTableRenderer.BG_TEX_UV.vectors[2].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vector4f[3].getX() - cameraPos.x, vector4f[3].getY() - cameraPos.y, vector4f[3].getZ() - cameraPos.z).texture(CraftingTableRenderer.BG_TEX_UV.vectors[3].x, CraftingTableRenderer.BG_TEX_UV.vectors[3].y).color(255, 255, 255, 255).next();

        Tessellator.getInstance().draw();


        HangedGUIRenderManager.renderFlat(matrices, tickDelta, camera, gameRenderer, this.textureManager);

        // RenderSystem.popMatrix();
        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

        //todo: del this
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).setYawPitch(camera.getYaw(), camera.getPitch());

    }
}
