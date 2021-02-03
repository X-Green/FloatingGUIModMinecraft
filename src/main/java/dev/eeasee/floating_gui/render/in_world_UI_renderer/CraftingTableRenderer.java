package dev.eeasee.floating_gui.render.in_world_UI_renderer;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class CraftingTableRenderer implements InWorldUIRenderer {

    @Override
    public void addItem(ItemStack stack, int x, int y) {

    }

    @Override
    public void setMouse(int x, int y) {

    }

    @Override
    public int getUIWidth() {
        return 0;
    }

    @Override
    public int getUIHeight() {
        return 0;
    }

    @Override
    public void render(MatrixStack matrices, float tickDelta, long limitTime, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f) {

    }

}
