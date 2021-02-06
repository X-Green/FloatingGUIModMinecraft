package dev.eeasee.gui_hanger.render.renderer;

import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Quadruple;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
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

import java.util.List;

public abstract class HungGUIBaseRenderer {

    protected int mouseX = -1;
    protected int mouseY = -1;

    protected float yaw = 0.0f;
    protected float pitch = 0.0f;
    protected Quaternion rotation = new Quaternion(0, 0, 0, 1);
    protected Matrix4f transformer = Matrix4f.translate(0, 0, 0);
    protected Vec3d center;
    /**
     * ID should be coded in server side and be sent within packets related.
     */
    protected final int id;

    public HungGUIBaseRenderer(int id) {
        this.id = id;
    }

    public abstract void addItem(ItemStack stack, int x, int y);

    public abstract void setMouse(int x, int y);

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

    protected abstract Pair<Integer, Integer> getHeightWidth();

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

    public abstract List<Quadruple<QuadVec4f, Identifier, Float, Float>> putBackground(float tickDelta);

    public abstract List<Quadruple<QuadVec4f, Identifier, Float, Float>> putWidgets(float tickDelta);

    public abstract List<Pair<Vector4f, Item>> putItems(float tickDelta);

    public QuadVec4f putMouseAndBindTexture() {
        int mx = this.mouseX;
        int my = this.mouseY;
        if (mx < 0 || my < 0) {
            return null;
        }
        Pair<Integer, Integer> size = this.getHeightWidth();
        if (mx > size.getLeft() || my > size.getRight()) {
            return null;
        }

        //todo: mouse
        return null;
    }

    public void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer) {
        matrices.push();
        matrices.pop();
    }

    public void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer) {

    }

    protected void updateRotationQuaternion() {
        this.rotation = Vector3f.NEGATIVE_Y.getDegreesQuaternion(this.getYaw());
        rotation.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(this.getPitch()));
    }

    protected void updateTransformingMatrix4f() {
        Vec3d pos = this.getPos();
        this.transformer = Matrix4f.translate((float) pos.x, (float) pos.y, (float) pos.z);
        this.transformer.multiply(this.getRotation());
    }

    public abstract void updateSizeSettings();

    @Override
    public int hashCode() {
        return this.id;
    }

}
