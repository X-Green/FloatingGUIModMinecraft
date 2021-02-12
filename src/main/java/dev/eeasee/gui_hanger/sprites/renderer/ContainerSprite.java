package dev.eeasee.gui_hanger.sprites.renderer;

import dev.eeasee.gui_hanger.GUIHangerMod;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public abstract class ContainerSprite extends BaseSprite {


    private final Int2ObjectMap<Item> items = new Int2ObjectOpenHashMap<>();

    public ContainerSprite(int id, SpriteType type) {
        super(id, type);
    }

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
                                // System.out.println("ADD ITEM: " + itemPair);
                                this.setItem(itemPair.getLeft(), itemPair.getRight());
                            }, byteBuf
                    );
                    break;
                case SpriteProperty.ID_REMOVE_ITEM:
                    SpriteProperty.REMOVE_ITEM.readPacketBytes(
                            integer -> this.getItems().remove(integer.intValue()), byteBuf
                    );
                    break;
                default:
                    GUIHangerMod.LOGGER.error("Wrong property for sprite:" + this.getSpriteName() + " ->id:" + propertyID);
            }
        }
    }

    @Override
    public void setItem(int index, Item item) {
        this.items.put(index, item);
    }

    @Override
    public Item getItem(int index) {
        return this.items.getOrDefault(index, null);
    }

    @Override
    public @NotNull Int2ObjectMap<Item> getItems() {
        return items;
    }

}
