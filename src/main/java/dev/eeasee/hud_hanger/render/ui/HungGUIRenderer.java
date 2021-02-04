package dev.eeasee.hud_hanger.render.ui;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public abstract class HungGUIRenderer {

    protected float yaw = 0.0f;
    protected float pitch = 0.0f;
    protected Quaternion rotation = new Quaternion(0, 0, 0, 1);
    protected Vec3d center;

    public abstract void addItem(ItemStack stack, int x, int y);

    public abstract void setMouse(int x, int y);

    public void setPos(Vec3d vec3d) {
        this.center = vec3d;
    }

    /**
     * @param yaw: 90.0(Looking down) -> -90.0(Looking up)
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.updateRotationQuaternion();
    }

    /**
     * @param pitch: 0.0(Facing +Z); increase to 180.0(Clockwise); turn to -180.0; back to 0.0
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.updateRotationQuaternion();
    }

    public void setYawPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.updateRotationQuaternion();
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

    public abstract void renderBackground(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    public abstract void renderWidgets(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    public abstract void renderItems();

    public void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f) {
        matrices.push();
        /*
        Quaternion rotation =
        Matrix4f transformMatrix = new Matrix4f()

         */
        this.renderBackground(tickDelta, camera, gameRenderer, matrix4f);
        this.renderWidgets(tickDelta, camera, gameRenderer, matrix4f);
        matrices.pop();
    }

    public void renderModels() {
    }

    private void updateRotationQuaternion() {

    }


}
