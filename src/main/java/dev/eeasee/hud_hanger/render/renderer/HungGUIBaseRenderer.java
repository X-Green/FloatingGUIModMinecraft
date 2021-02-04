package dev.eeasee.hud_hanger.render.renderer;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public abstract class HungGUIBaseRenderer {

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
     * @param yaw: 90.0(Looking down) -> -90.0(Looking up)
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.updateRotationQuaternion();
        this.updateTransformingMatrix4f();
    }

    /**
     * @param pitch: 0.0(Facing +Z); increase to 180.0(Clockwise); turn to -180.0; back to 0.0
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.updateRotationQuaternion();
        this.updateTransformingMatrix4f();
    }

    public void setYawPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.updateRotationQuaternion();
        this.updateTransformingMatrix4f();
    }

    public Vec3d getPos() {
        return this.center;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public Matrix4f getTransformer() {
        return this.transformer;
    }

    public abstract void renderBackground(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    public abstract void renderWidgets(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    public abstract void renderItems();

    public void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f) {
        matrices.push();
        this.renderBackground(tickDelta, camera, gameRenderer, this.getTransformer());
        this.renderWidgets(tickDelta, camera, gameRenderer, this.getTransformer());
        matrices.pop();
    }

    public void renderModels() {
    }

    protected void updateRotationQuaternion() {
        this.rotation = Vector3f.NEGATIVE_Y.getDegreesQuaternion(yaw);
        rotation.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));
    }

    protected void updateTransformingMatrix4f() {
        Vec3d pos = this.getPos();
        this.transformer = Matrix4f.translate((float) pos.x, (float) pos.y, (float) pos.z);
        this.transformer.multiply(this.getRotation());
    }

    @Override
    public int hashCode() {
        return this.id;
    }

}
