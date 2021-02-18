package dev.eeasee.gui_hanger.sprites.renderer;

import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Vec2i;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class Generic3x3Sprite extends ContainerSprite {
    private static final Identifier BG_TEX = new Identifier("textures/gui/container/dispenser.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    public static final QuadVec2f BG_TEX_UV = new QuadVec2f(
            0, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, 0,
            0, 0
    );

    public static final byte TYPE_DISPENSER = 0;
    public static final byte TYPE_DROPPER = 1;

    private static final EnumSet<SpriteProperty.PropertyType> PROPERTIES = ContainerSprite.PROPERTIES.clone();

    static {
        PROPERTIES.add(SpriteProperty.PropertyType.SET_TYPE_GENERIC3x3);
    }


    private byte container3x3Type = 0;

    public Generic3x3Sprite(int id) {
        super(id, SpriteType.GENERIC_3X3);
    }

    public void set3x3ContainerType(byte type) {
        this.container3x3Type = type;
    }

    public byte get3x3ContainerType() {
        return this.container3x3Type;
    }

    public EnumSet<SpriteProperty.PropertyType> getProperties() {
        return PROPERTIES;
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
        switch (container3x3Type) {
            case TYPE_DISPENSER:
                return "DispenserSprite";
            case TYPE_DROPPER:
                return "DropperSprite";
            default:
                return "Generic3x3Sprite";
        }
    }

    @Override
    public SpriteType getType() {
        return SpriteType.GENERIC_3X3;
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
        if (itemIndex < 45) {
            int x = (itemIndex % 3 - 1) * 18;
            int y = 142 - (itemIndex - 36) / 3 * 18;
            return new Vec2i(x, y);
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
