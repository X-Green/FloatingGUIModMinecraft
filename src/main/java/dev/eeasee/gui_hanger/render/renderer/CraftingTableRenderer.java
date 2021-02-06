package dev.eeasee.gui_hanger.render.renderer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class CraftingTableRenderer extends ContainerGUIRenderer {
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
    public void updateSizeSettings() {

    }
}
