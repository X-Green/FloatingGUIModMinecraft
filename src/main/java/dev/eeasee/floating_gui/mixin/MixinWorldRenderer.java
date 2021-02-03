package dev.eeasee.floating_gui.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.border.WorldBorder;
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

    @Inject(method = "render",
            at = @At(value = "INVOKE_STRING", args = "ldc=weather",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void afterWorldBoarderRendered(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        double dx = cameraPos.x;
        double dy = cameraPos.y;
        double dz = cameraPos.z;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        this.textureManager.bindTexture(new Identifier("textures/item/apple.png"));
        RenderSystem.depthMask(false);
        RenderSystem.pushMatrix();
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

        BlockPos blockPos = new BlockPos(-60, 78, -216);

        double vx = blockPos.getX() - cameraPos.x;
        double vy = blockPos.getY() - cameraPos.y + 5;
        double vz = blockPos.getZ() - cameraPos.z;

        bufferBuilder.vertex(vx, vy, vz).texture(0, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vx + 2, vy, vz).texture(1, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vx + 2, vy, vz + 2).texture(1, 0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vx, vy, vz + 2).texture(0, 0).color(255, 255, 255, 255).next();


        Tessellator.getInstance().draw();

        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        RenderSystem.depthMask(true);

        /*
        VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
        matrices.push();
        Vec3d vec3d = camera.getPos();
        double d = vec3d.getX();
        double e = vec3d.getY();
        double f = vec3d.getZ();
        BlockPos blockPos2 = new BlockPos(-60, 78, -216);
        // RenderSystem.defaultBlendFunc();
        matrices.translate((double) blockPos2.getX() - d, (double) blockPos2.getY() - e, (double) blockPos2.getZ() - f);
        MinecraftClient.getInstance().getItemRenderer().renderItem(Items.ACACIA_BOAT.getStackForRender(), ModelTransformation.Mode.GROUND, 255, OverlayTexture.DEFAULT_UV, matrices, immediate);

        matrices.pop();

        matrices.push();

        Vec3d vec3d1 = camera.getPos();
        double d1 = vec3d1.getX();
        double e1 = vec3d1.getY();
        double f1 = vec3d1.getZ();
        BlockPos blockPos3 = new BlockPos(-60, 78, -216);
        matrices.translate((double) blockPos3.getX() - d1, (double) blockPos3.getY() - e1, (double) blockPos3.getZ() - f1);

        this.textureManager.bindTexture(new Identifier("textures/item/apple.png"));


         */
        /*
        VertexConsumer bufferBuilder = immediate.getBuffer(RenderLayer.getCutout());

        bufferBuilder.vertex(matrix4f, 0, 0, 1).texture(0, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 1, 0, 1).texture(1, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 1, 0, 0).texture(1, 0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 0, 0, 0).texture(0, 0).color(255, 255, 255, 255).next();

        immediate.draw();


        matrices.pop();

         */

        /*
        RenderSystem.multMatrix(matrices.peek().getModel());

        RenderSystem.enableDepthTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        Entity entity = this.client.gameRenderer.getCamera().getFocusedEntity();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        double dd = 0.0D - camera.getPos().y;
        double ee = 256.0D - camera.getPos().y;
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        double ff = (double)(entity.chunkX << 4) - camera.getPos().x;
        double g = (double)(entity.chunkZ << 4) - camera.getPos().
        // bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);


        this.textureManager.bindTexture(new Identifier("textures/item/apple.png"));

        bufferBuilder.vertex(matrix4f, 0, 0, 1).texture(0, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 1, 0, 1).texture(1, 1).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 1, 0, 0).texture(1, 0).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(matrix4f, 0, 0, 0).texture(0, 0).color(255, 255, 255, 255).next();


        tessellator.draw();
        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);


         */


    }
}
