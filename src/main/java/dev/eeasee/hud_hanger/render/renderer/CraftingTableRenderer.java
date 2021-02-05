package dev.eeasee.hud_hanger.render.renderer;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class CraftingTableRenderer extends ContainerHungRenderer {
    private static final Identifier BG_TEX = new Identifier("textures/gui/container/crafting_table.png");

    public CraftingTableRenderer(int id) {
        super(id);
    }

    @Override
    public void addItem(ItemStack stack, int x, int y) {

    }

    @Override
    public void setMouse(int x, int y) {

    }

    @Override
    public void renderBackground(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f) {

    }

    @Override
    public void renderWidgets(float tickDelta, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f) {

    }

    @Override
    public void renderItems() {

    }
}
