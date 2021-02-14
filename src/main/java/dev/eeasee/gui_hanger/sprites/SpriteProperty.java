package dev.eeasee.gui_hanger.sprites;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpriteProperty<T> {
    public static final byte ID_CREATE = -3;
    public static final byte ID_DESTROY = -2;
    public static final byte ID_NULL = 0;
    public static final byte ID_POSITION = 1;
    public static final byte ID_YAW_PITCH = 2;
    public static final byte ID_ADD_ITEM = 3;
    public static final byte ID_REMOVE_ITEM = 4;
    public static final byte ID_SET_TYPE_GENERIC3x3 = 5;
    public static final byte ID_ANVIL_ITEM_NAME_AND_AVAILABILITY = 6;
    public static final byte ID_ANVIL_CAN_FIX = 7;

    private static final EnumMap<PropertyType, SpriteProperty> PROPERTY_TYPE_ID_MAP = new EnumMap<>(PropertyType.class);

    public static final SpriteProperty<Object> NULL = new SpriteProperty<>(
            PropertyType.NULL,
            (c, packetByteBuf) -> {
            }, ((o, packetByteBuf) -> {
    })
    );
    public static final SpriteProperty<Vector3f> POSITION = new SpriteProperty<>(
            PropertyType.POSITION,
            (consumer, packetByteBuf) -> consumer.accept(new Vector3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (vector3f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vector3f.getX());
                packetByteBuf.writeFloat(vector3f.getY());
                packetByteBuf.writeFloat(vector3f.getZ());
            }
    );
    public static final SpriteProperty<Vec2f> YAW_PITCH = new SpriteProperty<>(
            PropertyType.YAW_PITCH,
            (consumer, packetByteBuf) -> consumer.accept(new Vec2f(packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (vec2f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vec2f.x);
                packetByteBuf.writeFloat(vec2f.y);
            }
    );
    public static final SpriteProperty<Pair<Integer, Item>> ADD_ITEM = new SpriteProperty<>(
            PropertyType.ADD_ITEM,
            (consumer, packetByteBuf) -> consumer.accept(new Pair<>((int) packetByteBuf.readUnsignedByte(), Registry.ITEM.get(packetByteBuf.readUnsignedShort()))),
            (itemPair, packetByteBuf) -> {
                packetByteBuf.writeByte(itemPair.getLeft());
                packetByteBuf.writeShort(Registry.ITEM.getRawId(itemPair.getRight()));
            }
    );
    public static final SpriteProperty<Integer> REMOVE_ITEM = new SpriteProperty<>(
            PropertyType.REMOVE_ITEM,
            (consumer, packetByteBuf) -> consumer.accept((int) packetByteBuf.readUnsignedByte()),
            (integer, packetByteBuf) -> packetByteBuf.writeByte(integer)
    );
    public static final SpriteProperty<Byte> SET_TYPE_GENERIC3X3 = new SpriteProperty<>(
            PropertyType.SET_TYPE_GENERIC3x3,
            (consumer, packetByteBuf) -> consumer.accept(packetByteBuf.readByte()),
            (aByte, packetByteBuf) -> packetByteBuf.writeByte(aByte)
    );
    public static final SpriteProperty<String> ANVIL_ITEM_NAME_AND_AVAILABILITY = new SpriteProperty<>(
            PropertyType.ANVIL_ITEM_NAME_AND_AVAILABILITY,
            ((consumer, packetByteBuf) -> consumer.accept(packetByteBuf.readBoolean() ? packetByteBuf.readString() : null)),
            ((string, packetByteBuf) -> {
                if (string == null) {
                    packetByteBuf.writeBoolean(false);
                } else {
                    packetByteBuf.writeBoolean(true);
                    packetByteBuf.writeString(string);
                }
            })
    );
    public static final SpriteProperty<Boolean> ANVIL_CAN_FIX = new BooleanProperty(PropertyType.ANVIL_CAN_FIX);


    private final PropertyType type;

    private final BiConsumer<Consumer<T>, PacketByteBuf> packetBytesReader;

    private final BiConsumer<T, PacketByteBuf> packetBytesWriter;

    private SpriteProperty(PropertyType type, BiConsumer<Consumer<T>, PacketByteBuf> packetByteReader, BiConsumer<T, PacketByteBuf> packetBytesWriter) {
        this.type = type;
        PROPERTY_TYPE_ID_MAP.put(type, this);
        this.packetBytesReader = packetByteReader;
        this.packetBytesWriter = packetBytesWriter;
    }

    public PropertyType getType() {
        return this.type;
    }

    public void readPacketBytes(Consumer<T> consumer, PacketByteBuf packetByteBuf) {
        this.packetBytesReader.accept(consumer, packetByteBuf);
    }

    public void writePacketBytes(PacketByteBuf packetByteBuf, T target) {
        packetByteBuf.writeByte(type.ordinal());
        this.packetBytesWriter.accept(target, packetByteBuf);
    }

    public int hashCode() {
        return this.type.ordinal();
    }

    public static SpriteProperty getPropertyByType(PropertyType type) {
        return PROPERTY_TYPE_ID_MAP.getOrDefault(type, null);
    }

    private static class BooleanProperty extends SpriteProperty<Boolean> {
        private static final BiConsumer<Consumer<Boolean>, PacketByteBuf> BOOLEAN_READER = (consumer, packetByteBuf) -> consumer.accept(packetByteBuf.readBoolean());
        private static final BiConsumer<Boolean, PacketByteBuf> BOOLEAN_WRITER = (aBoolean, packetByteBuf) -> packetByteBuf.writeBoolean(aBoolean);

        private BooleanProperty(PropertyType type) {
            super(type, BOOLEAN_READER, BOOLEAN_WRITER);
        }
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
    }
}
