package dev.eeasee.gui_hanger.sprites;

import dev.eeasee.gui_hanger.sprites.renderer.AnvilSprite;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import dev.eeasee.gui_hanger.sprites.renderer.ContainerSprite;
import dev.eeasee.gui_hanger.sprites.renderer.Generic3x3Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;

import java.util.EnumMap;
import java.util.function.BiConsumer;

public class SpriteProperty<T> {
    public static final byte ID_CREATE = -3;

    private static final EnumMap<PropertyType, SpriteProperty> PROPERTY_TYPE_ID_MAP = new EnumMap<>(PropertyType.class);

    public static final SpriteProperty<Vector3f> POSITION = new SpriteProperty<>(
            PropertyType.POSITION,
            (baseSprite, packetByteBuf) -> baseSprite.setPos(new Vector3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (vector3f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vector3f.getX());
                packetByteBuf.writeFloat(vector3f.getY());
                packetByteBuf.writeFloat(vector3f.getZ());
            }
    );
    public static final SpriteProperty<Vec2f> YAW_PITCH = new SpriteProperty<>(
            PropertyType.YAW_PITCH,
            (baseSprite, packetByteBuf) -> baseSprite.setYawPitch(packetByteBuf.readFloat(), packetByteBuf.readFloat()),
            (vec2f, packetByteBuf) -> packetByteBuf.writeFloat(vec2f.x).writeFloat(vec2f.y)
    );
    public static final SpriteProperty<Pair<Integer, Item>> ADD_ITEM = new SpriteProperty<>(
            PropertyType.ADD_ITEM,
            (baseSprite, packetByteBuf) -> ((ContainerSprite) baseSprite).setItem(packetByteBuf.readUnsignedByte(), Registry.ITEM.get(packetByteBuf.readUnsignedShort())),
            (itemPair, packetByteBuf) -> {
                packetByteBuf.writeByte(itemPair.getLeft());
                packetByteBuf.writeShort(Registry.ITEM.getRawId(itemPair.getRight()));
            }
    );
    public static final SpriteProperty<Integer> REMOVE_ITEM = new SpriteProperty<>(
            PropertyType.REMOVE_ITEM,
            (baseSprite, packetByteBuf) -> ((ContainerSprite) baseSprite).removeItem(packetByteBuf.readUnsignedByte()),
            (integer, packetByteBuf) -> packetByteBuf.writeByte(integer)
    );
    public static final SpriteProperty<Byte> SET_TYPE_GENERIC3X3 = new SpriteProperty<>(
            PropertyType.SET_TYPE_GENERIC3x3,
            (baseSprite, packetByteBuf) -> ((Generic3x3Sprite) baseSprite).set3x3ContainerType(packetByteBuf.readByte()),
            (aByte, packetByteBuf) -> packetByteBuf.writeByte(aByte)
    );
    public static final SpriteProperty<String> ANVIL_ITEM_NAME_AND_AVAILABILITY = new SpriteProperty<>(
            PropertyType.ANVIL_ITEM_NAME_AND_AVAILABILITY,
            ((baseSprite, packetByteBuf) -> ((AnvilSprite) baseSprite).setAnvilItemNameDisplay(packetByteBuf.readBoolean() ? packetByteBuf.readString() : null)),
            ((string, packetByteBuf) -> {
                if (string == null) {
                    packetByteBuf.writeBoolean(false);
                } else {
                    packetByteBuf.writeBoolean(true);
                    packetByteBuf.writeString(string);
                }
            })
    );
    public static final SpriteProperty<Boolean> ANVIL_CAN_FIX = new SpriteProperty<>(
            PropertyType.ANVIL_CAN_FIX,
            (baseSprite, packetByteBuf) -> ((AnvilSprite) baseSprite).setCanFixItem(packetByteBuf.readBoolean()),
            (b, packetByteBuf) -> packetByteBuf.writeBoolean(b)
    );


    private final PropertyType type;

    private final BiConsumer<BaseSprite, PacketByteBuf> packetBytesReader;

    private final BiConsumer<T, PacketByteBuf> packetBytesWriter;

    private SpriteProperty(PropertyType type, BiConsumer<BaseSprite, PacketByteBuf> packetByteReader, BiConsumer<T, PacketByteBuf> packetBytesWriter) {
        this.type = type;
        PROPERTY_TYPE_ID_MAP.put(type, this);
        this.packetBytesReader = packetByteReader;
        this.packetBytesWriter = packetBytesWriter;
    }

    public PropertyType getType() {
        return this.type;
    }

    public void readPacketBytes(BaseSprite sprite, PacketByteBuf packetByteBuf) {
        this.packetBytesReader.accept(sprite, packetByteBuf);
    }

    public void writePacketBytes(T sprite, PacketByteBuf packetByteBuf) {
        type.writeOrdinalToPacket(packetByteBuf);
        this.packetBytesWriter.accept(sprite, packetByteBuf);
    }

    public int hashCode() {
        return this.type.ordinal();
    }

    public static SpriteProperty getPropertyByType(PropertyType type) {
        return PROPERTY_TYPE_ID_MAP.getOrDefault(type, null);
    }

    public enum PropertyType {
        CREATE,
        DESTROY,
        NULL,
        POSITION,
        YAW_PITCH,
        ADD_ITEM,
        REMOVE_ITEM,
        SET_TYPE_GENERIC3x3,
        ANVIL_ITEM_NAME_AND_AVAILABILITY,
        ANVIL_CAN_FIX;

        public void writeOrdinalToPacket(PacketByteBuf packetByteBuf) {
            packetByteBuf.writeByte(this.ordinal());
        }
    }
}
