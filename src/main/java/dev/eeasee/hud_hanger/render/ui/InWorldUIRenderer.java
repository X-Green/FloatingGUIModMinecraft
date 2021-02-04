package dev.eeasee.hud_hanger.render.ui;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public interface InWorldUIRenderer {

    void addItem(ItemStack stack, int x, int y);

    void setMouse(int x, int y);

    void setPos(Vec3d vec3d);

    void setFacing(float yaw, float pitch);

    Vec3d getPos();

    float getYRotationDegree();

    void render(MatrixStack matrices, float tickDelta, long limitTime, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    void renderBackground();

    void renderItems();

    void renderWidgets();


}
