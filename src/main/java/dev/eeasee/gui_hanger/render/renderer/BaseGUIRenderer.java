package dev.eeasee.gui_hanger.render.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.eeasee.gui_hanger.config.Configs;
import dev.eeasee.gui_hanger.render.Textures;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Vec2i;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public abstract class BaseGUIRenderer {

    protected int mouseX = -1;
    protected int mouseY = -1;

    protected float yaw = 0.0f;
    protected float pitch = 0.0f;
    protected Quaternion rotation = new Quaternion(0, 0, 0, 1);
    protected Matrix4f transformer = Matrix4f.translate(0, 0, 0);
    protected Vec3d center;

    private Map<Identifier, List<Triple<QuadVec4f, Identifier, QuadVec2f>>> assembledKeyCached = Maps.newHashMap();

    public static final float SCALE_NUM = Configs.hungScreenScale * 2.5f / 256.0f;
    public static Matrix4f SCALE_MATRIX = Matrix4f.scale(SCALE_NUM, SCALE_NUM, SCALE_NUM);
    public static Matrix4f ITEM_SCALE = Matrix4f.scale(14, 14, 14);

    private volatile boolean isChanged = false;

    /**
     * ID should be coded in server side and be sent within packets related.
     */
    protected final int id;

    public BaseGUIRenderer(int id) {
        this.id = id;
    }

    public abstract void readPacketBytes(PacketByteBuf byteBuf);

    public void setMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public void setPos(Vec3d vec3d) {
        if (!vec3d.equals(this.center)) {
            this.isChanged = true;
            this.center = vec3d;
        }
    }

    /**
     * The facing of player holding the screen
     * -back of the actual rendering facing
     *
     * @param yaw:   90.0(Looking down) -> -90.0(Looking up)
     * @param pitch: 0.0(Facing +Z); increase to 180.0(Clockwise); turn to -180.0; back to 0.0
     */
    public void setYawPitch(float yaw, float pitch) {
        if (this.yaw != yaw || this.pitch != this.pitch) {
            this.isChanged = true;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    public Vec3d getPos() {
        return this.center;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public Matrix4f getTransformer() {
        return this.transformer;
    }

    @NotNull
    public abstract Int2ObjectMap<Item> getItems();

    public abstract Vec2i getItemCoordinate(int itemIndex);

    public abstract Triple<QuadVec4f, Identifier, QuadVec2f> putBackgroundRendering(float tickDelta);

    @NotNull
    public abstract List<Triple<QuadVec4f, Identifier, QuadVec2f>> putWidgetsRendering(float tickDelta);

    @NotNull
    public List<Pair<Matrix4f, ItemStack>> putItemsRendering(float tickDelta) {
        Int2ObjectMap<Item> items = this.getItems();
        List<Pair<Matrix4f, ItemStack>> result = Lists.newArrayListWithCapacity(items.size());
        for (Int2ObjectMap.Entry<Item> entry : items.int2ObjectEntrySet()) {
            int i = entry.getIntKey();
            Item item = entry.getValue();
            Vec2i vec2i = this.getItemCoordinate(i);
            if (vec2i == null) {
                continue;
            }
            Matrix4f matrix4f = Matrix4f.translate(vec2i.x * SCALE_NUM, vec2i.y * SCALE_NUM, -0.07f);
            result.add(new Pair<>(matrix4f, item.getStackForRender()));
        }
        return result;
    }

    @Nullable
    public Pair<QuadVec4f, QuadVec2f> putMouseRenderingAndBindTexture(TextureManager textureManager) {
        int mx = this.mouseX;
        int my = this.mouseY;
        if (mx < 0 || my < 0) {
            return null;
        }
        if (mx > this.getWidth() || my > this.getHeight()) {
            return null;
        }
        textureManager.bindTexture(Textures.MOUSE_ICON);
        int startX = this.getRealX(mx);
        int startY = this.getRealY(my);
        final float sideLength = 32.0f;
        return new Pair<>(
                new QuadVec4f(
                        startX, startY,
                        startX + sideLength, startY,
                        startX + sideLength, startY + sideLength,
                        startX, startY + sideLength
                ),
                Textures.MOUSE_ICON_TEXTURE_UV
        );
    }

    public void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager) {
        this.checkUpdateTransformingMatrix4f();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Vec3d cameraPos = camera.getPos();
        matrices.push();

        Triple<QuadVec4f, Identifier, QuadVec2f> background = this.putBackgroundRendering(tickDelta);
        this.assembledKeyCached.put(background.getMiddle(), Lists.newArrayList(background));

        for (Triple<QuadVec4f, Identifier, QuadVec2f> t : this.putWidgetsRendering(tickDelta)) {
            if (this.assembledKeyCached.containsKey(t.getMiddle())) {
                this.assembledKeyCached.get(t.getMiddle()).add(t);
            } else {
                this.assembledKeyCached.put(t.getMiddle(), Lists.newArrayList(t));
            }
        }

        for (Map.Entry<Identifier, List<Triple<QuadVec4f, Identifier, QuadVec2f>>> entry : this.assembledKeyCached.entrySet()) {
            Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

            textureManager.bindTexture(entry.getKey());

            for (Triple<QuadVec4f, Identifier, QuadVec2f> polygon : entry.getValue()) {

                QuadVec4f vertexes = polygon.getLeft();
                QuadVec2f uv = polygon.getRight();

                vertexes.transform(this.getTransformer());

                bufferBuilder.vertex(vertexes.vectors[0].getX() - cameraPos.x, vertexes.vectors[0].getY() - cameraPos.y, vertexes.vectors[0].getZ() - cameraPos.z).texture(uv.vectors[0].x, uv.vectors[0].y).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(vertexes.vectors[1].getX() - cameraPos.x, vertexes.vectors[1].getY() - cameraPos.y, vertexes.vectors[1].getZ() - cameraPos.z).texture(uv.vectors[1].x, uv.vectors[1].y).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(vertexes.vectors[2].getX() - cameraPos.x, vertexes.vectors[2].getY() - cameraPos.y, vertexes.vectors[2].getZ() - cameraPos.z).texture(uv.vectors[2].x, uv.vectors[2].y).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(vertexes.vectors[3].getX() - cameraPos.x, vertexes.vectors[3].getY() - cameraPos.y, vertexes.vectors[3].getZ() - cameraPos.z).texture(uv.vectors[3].x, uv.vectors[3].y).color(255, 255, 255, 255).next();
            }
            Tessellator.getInstance().draw();
        }
        this.assembledKeyCached.clear();

        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
        Pair<QuadVec4f, QuadVec2f> pair = this.putMouseRenderingAndBindTexture(textureManager);
        if (pair != null) {
            QuadVec4f vertexes = pair.getLeft();
            QuadVec2f uv = pair.getRight();
            vertexes.transform(this.getTransformer());
            bufferBuilder.vertex(vertexes.vectors[0].getX() - cameraPos.x, vertexes.vectors[0].getY() - cameraPos.y, vertexes.vectors[0].getZ() - cameraPos.z).texture(uv.vectors[0].x, uv.vectors[0].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[1].getX() - cameraPos.x, vertexes.vectors[1].getY() - cameraPos.y, vertexes.vectors[1].getZ() - cameraPos.z).texture(uv.vectors[1].x, uv.vectors[1].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[2].getX() - cameraPos.x, vertexes.vectors[2].getY() - cameraPos.y, vertexes.vectors[2].getZ() - cameraPos.z).texture(uv.vectors[2].x, uv.vectors[2].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[3].getX() - cameraPos.x, vertexes.vectors[3].getY() - cameraPos.y, vertexes.vectors[3].getZ() - cameraPos.z).texture(uv.vectors[3].x, uv.vectors[3].y).color(255, 255, 255, 255).next();
            Tessellator.getInstance().draw();
        }

        matrices.pop();
    }

    public void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager, BufferBuilderStorage bufferBuilders) {
        this.checkUpdateTransformingMatrix4f();

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();

        Vec3d cameraPos = camera.getPos();
        Matrix4f itemTransformer = Matrix4f.translate(-(float) cameraPos.x, -(float) cameraPos.y, -(float) cameraPos.z);
        itemTransformer.multiply(this.getTransformer());
        itemTransformer.multiply(ITEM_SCALE);

        List<Pair<Matrix4f, ItemStack>> items = this.putItemsRendering(tickDelta);
        for (Pair<Matrix4f, ItemStack> pair : items) {
            Matrix4f origin = pair.getLeft();
            origin.multiply(itemTransformer);
            matrices.push();
            matrices.peek().getModel().multiply(origin);
            itemRenderer.renderItem(
                    null,
                    pair.getRight(),
                    ModelTransformation.Mode.GUI,
                    false,
                    matrices,
                    immediate,
                    null,
                    0x00f000f0,
                    OverlayTexture.DEFAULT_UV
            );
            matrices.pop();
        }
    }

    protected void checkUpdateTransformingMatrix4f() {
        if (!this.isChanged) {
            return;
        }
        this.rotation = Vector3f.NEGATIVE_Y.getDegreesQuaternion(this.getYaw());
        rotation.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(this.getPitch()));
        rotation.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        Vec3d pos = this.getPos();
        this.transformer = Matrix4f.translate((float) pos.x, (float) pos.y, (float) pos.z);
        this.transformer.multiply(this.getRotation());
        this.transformer.multiply(SCALE_MATRIX);

        this.isChanged = false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public int getRealX(int x) {
        return x - this.getWidth() >> 1;
    }

    public int getRealY(int y) {
        return y - this.getHeight() >> 1;
    }
}
