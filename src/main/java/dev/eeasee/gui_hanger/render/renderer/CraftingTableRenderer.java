package dev.eeasee.gui_hanger.render.renderer;

import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Vec2i;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
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
    public void readPacketBytes(PacketByteBuf byteBuf) {

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
    public Vec2i getItemCoordinate(int itemIndex) {
        int x, y;
        if (itemIndex < 0) {
            return null;
        }
        if (itemIndex < 9) {
            return new Vec2i(72 - itemIndex * 18, -66);
        }
        if (itemIndex < 36) {
            int lineNumber, columnNumber;
            lineNumber = (itemIndex) / 9 - 1;
            columnNumber = itemIndex % 9;
            return new Vec2i(72 - columnNumber * 18, -44 + lineNumber * 18);
        }
        if (itemIndex < 45) {

        }
        if (itemIndex == 45) {

        }
        return null;

    }

    @Override
    public Triple<QuadVec4f, Identifier, QuadVec2f> putBackgroundRendering(float tickDelta) {
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
    public List<Triple<QuadVec4f, Identifier, QuadVec2f>> putWidgetsRendering(float tickDelta) {
        return Collections.EMPTY_LIST;
    }

}
