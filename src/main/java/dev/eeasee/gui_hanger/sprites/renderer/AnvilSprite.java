package dev.eeasee.gui_hanger.sprites.renderer;

import com.google.common.collect.Lists;
import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.util.QuadVec2f;
import dev.eeasee.gui_hanger.util.QuadVec4f;
import dev.eeasee.gui_hanger.util.Vec2i;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnvilSprite extends ContainerSprite {
    private static final Identifier BG_TEX = new Identifier("textures/gui/container/anvil.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static final QuadVec2f BG_TEX_UV = new QuadVec2f(
            0, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, (float) HEIGHT / 256.0f,
            (float) WIDTH / 256.0f, 0,
            0, 0
    );
    private static final QuadVec2f NAME_BAR_AVAILABLE_TEX_UV = new QuadVec2f(
            0, (166.0f + 16.0f) / 256.0f,
            110.0f / 256.0f, (166.0f + 16.0f) / 256.0f,
            110.0f / 256.0f, 166.0f / 256.0f,
            0, 166.0f / 256.0f
    );
    private static final QuadVec2f NAME_BAR_UNAVAILABLE_TEX_UV = new QuadVec2f(
            0, (166.0f + 32.0f) / 256.0f,
            110.0f / 256.0f, (166.0f + 32.0f) / 256.0f,
            110.0f / 256.0f, (166.0f + 16.0f) / 256.0f,
            0, (166.0f + 16.0f) / 256.0f
    );
    private static final QuadVec2f ARROW_CANNOT_FIX_ITEM_UV = new QuadVec2f(
            176.0f / 256.0f, 21.0f / 256.0f,
            (176.0f + 28.0f) / 256.0f, 21.0f / 256.0f,
            (176.0f + 28.0f) / 256.0f, 0f,
            176.0f / 256.0f, 0f
    );


    private String displayItemName = null;
    private boolean canFixItem = true;

    public AnvilSprite(int id) {
        super(id, SpriteType.ANVIL);
    }

    public void setAnvilItemNameDisplay(@Nullable String itemName) {
        this.displayItemName = itemName;
    }

    public void setCanFixItem(boolean b) {
        this.canFixItem = b;
    }

    @Override
    public void readPacketBytes(PacketByteBuf byteBuf) {
        while (true) {
            int propertyID = byteBuf.readUnsignedByte();
            switch (propertyID) {
                case SpriteProperty.ID_NULL:
                    return;
                case SpriteProperty.ID_POSITION:
                    SpriteProperty.POSITION.readPacketBytes(this::setPos, byteBuf);
                    break;
                case SpriteProperty.ID_YAW_PITCH:
                    SpriteProperty.YAW_PITCH.readPacketBytes(
                            vec2f -> this.setYawPitch(vec2f.x, vec2f.y), byteBuf
                    );
                    break;
                case SpriteProperty.ID_ADD_ITEM:
                    SpriteProperty.ADD_ITEM.readPacketBytes(
                            itemPair -> {
                                this.setItem(itemPair.getLeft(), itemPair.getRight());
                            }, byteBuf
                    );
                    break;
                case SpriteProperty.ID_REMOVE_ITEM:
                    SpriteProperty.REMOVE_ITEM.readPacketBytes(
                            integer -> this.getItems().remove(integer.intValue()), byteBuf
                    );
                    break;
                case SpriteProperty.ID_ANVIL_ITEM_NAME_AND_AVAILABILITY:
                    SpriteProperty.ANVIL_ITEM_NAME_AND_AVAILABILITY.readPacketBytes(
                            this::setAnvilItemNameDisplay, byteBuf
                    );
                    break;
                case SpriteProperty.ID_ANVIL_CAN_FIX:
                    SpriteProperty.ANVIL_CAN_FIX.readPacketBytes(
                            this::setCanFixItem, byteBuf
                    );
                    break;

                default:
                    GUIHangerMod.LOGGER.error("Wrong property for sprite:" + this.getSpriteName() + " ->id:" + propertyID);
            }
        }
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
        return "AnvilSprite";
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
        if (itemIndex < 39) {
            int x;
            switch (itemIndex) {
                case 36:
                    x = 53;
                    break;
                case 37:
                    x = 4;
                    break;
                case 38:
                    x = -54;
                    break;
                default:
                    x = 0;
            }
            return new Vec2i(x, 112);
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
        Triple<QuadVec4f, Identifier, QuadVec2f> nameBar = Triple.of(
                new QuadVec4f(
                        -29, 130,
                        81, 130,
                        81, 146,
                        -29, 146
                ),
                BG_TEX,
                this.displayItemName == null ? NAME_BAR_AVAILABLE_TEX_UV : NAME_BAR_UNAVAILABLE_TEX_UV
        );
        List<Triple<QuadVec4f, Identifier, QuadVec2f>> result = Lists.newArrayList(nameBar);
        if (this.canFixItem) {
            return result;
        } else {
            Triple<QuadVec4f, Identifier, QuadVec2f> arrow = Triple.of(
                    new QuadVec4f(
                            11, 100,
                            39, 100,
                            39, 121,
                            11, 121
                    ),
                    BG_TEX,
                    ARROW_CANNOT_FIX_ITEM_UV
            );
            result.add(arrow);
            return result;
        }
        //todo: render texts
    }
}
