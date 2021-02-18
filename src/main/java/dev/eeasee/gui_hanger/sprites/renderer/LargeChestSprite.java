package dev.eeasee.gui_hanger.sprites.renderer;

import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Vec2i;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LargeChestSprite extends ContainerSprite {
    private static final Identifier BG_TEX = new Identifier("textures/gui/container/generic_54.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 222;
    public static final QuadVec2f BG_TEX_UV = new QuadVec2f(
            0, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, 0,
            0, 0
    );

    public LargeChestSprite(int id) {
        super(id, SpriteType.LARGE_CHEST);
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
    public String getSpriteName() {
        return "LargeChestSprite";
    }

    @Override
    public SpriteType getType() {
        return SpriteType.LARGE_CHEST;
    }

    @Override
    public Vec2i getItemCoordinate(int itemIndex) {
        if (itemIndex < 0) {
            return null;
        }
        if (itemIndex < 9) {
            return new Vec2i(72 - itemIndex * 18, 17);
        }
        if (itemIndex < 36) {
            int lineNumber, columnNumber;
            lineNumber = (itemIndex) / 9 - 1;
            columnNumber = itemIndex % 9;
            return new Vec2i(72 - columnNumber * 18, 39 + lineNumber * 18);
        }
        if (itemIndex < 90) {
            int lineNumber, columnNumber;
            lineNumber = (itemIndex) / 9 - 1;
            columnNumber = itemIndex % 9;
            return new Vec2i(72 - columnNumber * 18, 50 + lineNumber * 18);
        }
        return null;
    }

    @Override
    public Triple<QuadVec4f, Identifier, QuadVec2f> putBackgroundRendering(float tickDelta) {
        return Triple.of(
                new QuadVec4f(
                        -(float) WIDTH / 2.0f, 0,
                        (float) WIDTH / 2.0f, 0,
                        (float) WIDTH / 2.0f, (float) HEIGHT,
                        -(float) WIDTH / 2.0f, (float) HEIGHT
                ),
                BG_TEX,
                BG_TEX_UV);
    }

    @Override
    public @NotNull List<Triple<QuadVec4f, Identifier, QuadVec2f>> putWidgetsRendering(float tickDelta) {
        return Collections.emptyList();
    }
}
