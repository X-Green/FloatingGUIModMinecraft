package dev.eeasee.gui_hanger.render.renderer;

import com.google.common.collect.Lists;
import dev.eeasee.gui_hanger.config.Configs;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Quadruple;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CraftingTableRenderer extends ContainerGUIRenderer {
    private static final Identifier BG_TEX = new Identifier("textures/gui/container/crafting_table.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    public static final QuadVec2f BG_TEX_UV = new QuadVec2f(
            0, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, 0,
            0, 0
    );

    public CraftingTableRenderer(int id) {
        super(id);
    }

    @Override
    public void addItem(ItemStack stack, int x, int y) {

    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    public Triple<QuadVec4f, Identifier, QuadVec2f> putBackground(float tickDelta) {
        return Triple.of(
                new QuadVec4f(
                        -(float) WIDTH / 2.0f, -(float) HEIGHT / 2.0f,
                        (float) WIDTH / 2.0f, -(float) HEIGHT / 2.0f,
                        (float) WIDTH / 2.0f, (float) HEIGHT / 2.0f,
                        -(float) WIDTH / 2.0f, (float) HEIGHT / 2.0f
                ),
                BG_TEX,
                BG_TEX_UV);
    }

    @NotNull
    @Override
    public List<Triple<QuadVec4f, Identifier, QuadVec2f>> putWidgets(float tickDelta) {
        return Collections.EMPTY_LIST;
    }

    @NotNull
    @Override
    public List<Pair<Vector4f, Item>> putItems(float tickDelta) {
        return Collections.EMPTY_LIST;
    }

}
