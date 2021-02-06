package dev.eeasee.gui_hanger.render.renderer;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.config.Configs;
import dev.eeasee.gui_hanger.render.Textures;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GUIRendererBase {

    protected int mouseX = -1;
    protected int mouseY = -1;

    protected float yaw = 0.0f;
    protected float pitch = 0.0f;
    protected Quaternion rotation = new Quaternion(0, 0, 0, 1);
    protected Matrix4f transformer = Matrix4f.translate(0, 0, 0);
    protected Vec3d center;
    public static Matrix4f SCALE = Matrix4f.scale(Configs.hungScreenScale * 2.5f / 256.0f, Configs.hungScreenScale * 2.5f / 256.0f, Configs.hungScreenScale * 2.5f / 256.0f);


    /**
     * ID should be coded in server side and be sent within packets related.
     */
    protected final int id;

    public GUIRendererBase(int id) {
        this.id = id;
    }

    public abstract void addItem(ItemStack stack, int x, int y);

    public void setMouse(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public void setPos(Vec3d vec3d) {
        this.center = vec3d;
        this.updateTransformingMatrix4f();
    }

    /**
     * The facing of player holding the screen
     * -back of the actual rendering facing
     *
     * @param yaw:   90.0(Looking down) -> -90.0(Looking up)
     * @param pitch: 0.0(Facing +Z); increase to 180.0(Clockwise); turn to -180.0; back to 0.0
     */
    public void setYawPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.updateRotationQuaternion();
        this.updateTransformingMatrix4f();
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

    public abstract Triple<QuadVec4f, Identifier, QuadVec2f> putBackground(float tickDelta);

    @NotNull
    public abstract List<Triple<QuadVec4f, Identifier, QuadVec2f>> putWidgets(float tickDelta);

    @NotNull
    public abstract List<Pair<Vector4f, Item>> putItems(float tickDelta);

    public Pair<QuadVec4f, QuadVec2f> putMouseAndBindTexture(TextureManager textureManager) {
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
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        Vec3d cameraPos = camera.getPos();

        matrices.push();

        List<Triple<QuadVec4f, Identifier, QuadVec2f>> assembled = new ArrayList<>();

        assembled.add(this.putBackground(tickDelta));
        assembled.addAll(this.putWidgets(tickDelta));

        for (Triple<QuadVec4f, Identifier, QuadVec2f> t : assembled) {
            Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

            textureManager.bindTexture(t.getMiddle());

            QuadVec4f vertexes = t.getLeft();
            QuadVec2f uv = t.getRight();

            vertexes.transform(this.getTransformer());

            bufferBuilder.vertex(vertexes.vectors[0].getX() - cameraPos.x, vertexes.vectors[0].getY() - cameraPos.y, vertexes.vectors[0].getZ() - cameraPos.z).texture(uv.vectors[0].x, uv.vectors[0].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[1].getX() - cameraPos.x, vertexes.vectors[1].getY() - cameraPos.y, vertexes.vectors[1].getZ() - cameraPos.z).texture(uv.vectors[1].x, uv.vectors[1].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[2].getX() - cameraPos.x, vertexes.vectors[2].getY() - cameraPos.y, vertexes.vectors[2].getZ() - cameraPos.z).texture(uv.vectors[2].x, uv.vectors[2].y).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(vertexes.vectors[3].getX() - cameraPos.x, vertexes.vectors[3].getY() - cameraPos.y, vertexes.vectors[3].getZ() - cameraPos.z).texture(uv.vectors[3].x, uv.vectors[3].y).color(255, 255, 255, 255).next();
            Tessellator.getInstance().draw();
        }


        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);

        Pair<QuadVec4f, QuadVec2f> t = this.putMouseAndBindTexture(textureManager);

        QuadVec4f vertexes = t.getLeft();
        QuadVec2f uv = t.getRight();

        vertexes.transform(this.getTransformer());

        bufferBuilder.vertex(vertexes.vectors[0].getX() - cameraPos.x, vertexes.vectors[0].getY() - cameraPos.y, vertexes.vectors[0].getZ() - cameraPos.z).texture(uv.vectors[0].x, uv.vectors[0].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vertexes.vectors[1].getX() - cameraPos.x, vertexes.vectors[1].getY() - cameraPos.y, vertexes.vectors[1].getZ() - cameraPos.z).texture(uv.vectors[1].x, uv.vectors[1].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vertexes.vectors[2].getX() - cameraPos.x, vertexes.vectors[2].getY() - cameraPos.y, vertexes.vectors[2].getZ() - cameraPos.z).texture(uv.vectors[2].x, uv.vectors[2].y).color(255, 255, 255, 255).next();
        bufferBuilder.vertex(vertexes.vectors[3].getX() - cameraPos.x, vertexes.vectors[3].getY() - cameraPos.y, vertexes.vectors[3].getZ() - cameraPos.z).texture(uv.vectors[3].x, uv.vectors[3].y).color(255, 255, 255, 255).next();
        Tessellator.getInstance().draw();

        matrices.pop();
    }

    public void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager) {

    }

    protected void updateRotationQuaternion() {
        this.rotation = Vector3f.NEGATIVE_Y.getDegreesQuaternion(this.getYaw());
        rotation.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(this.getPitch()));
        rotation.hamiltonProduct(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
    }

    protected void updateTransformingMatrix4f() {
        Vec3d pos = this.getPos();
        this.transformer = Matrix4f.translate((float) pos.x, (float) pos.y, (float) pos.z);
        this.transformer.multiply(this.getRotation());
        // this.transformer.multiply(SCALE);
        this.transformer.multiply(SCALE);
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
