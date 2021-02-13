package dev.eeasee.gui_hanger.sprites;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpriteProperty<T> {
    public static final int ID_NULL = 0;
    public static final int ID_POSITION = 1;
    public static final int ID_YAW_PITCH = 2;
    public static final int ID_ADD_ITEM = 3;
    public static final int ID_REMOVE_ITEM = 4;
    public static final int ID_SET_TYPE_GENERIC3x3 = 5;
    public static final int ID_ANVIL_ITEM_NAME_AND_AVAILABILITY = 6;
    public static final int ID_ANVIL_CAN_FIX = 7;

    private static final Int2ObjectMap<SpriteProperty> PROPERTY_TYPE_ID_MAP = new Int2ObjectOpenHashMap<>();

    public static final SpriteProperty<Object> NULL = new SpriteProperty<>(
            ID_NULL,
            (c, packetByteBuf) -> {
            }, ((o, packetByteBuf) -> {
    })
    );
    public static final SpriteProperty<Vector3f> POSITION = new SpriteProperty<>(
            ID_POSITION,
            (consumer, packetByteBuf) -> consumer.accept(new Vector3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (vector3f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vector3f.getX());
                packetByteBuf.writeFloat(vector3f.getY());
                packetByteBuf.writeFloat(vector3f.getZ());
            }
    );
    public static final SpriteProperty<Vec2f> YAW_PITCH = new SpriteProperty<>(
            ID_YAW_PITCH,
            (consumer, packetByteBuf) -> consumer.accept(new Vec2f(packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (vec2f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vec2f.x);
                packetByteBuf.writeFloat(vec2f.y);
            }
    );
    public static final SpriteProperty<Pair<Integer, Item>> ADD_ITEM = new SpriteProperty<>(
            ID_ADD_ITEM,
            (consumer, packetByteBuf) -> consumer.accept(new Pair<>((int) packetByteBuf.readUnsignedByte(), Registry.ITEM.get(packetByteBuf.readUnsignedShort()))),
            (itemPair, packetByteBuf) -> {
                packetByteBuf.writeByte(itemPair.getLeft());
                packetByteBuf.writeShort(Registry.ITEM.getRawId(itemPair.getRight()));
            }
    );
    public static final SpriteProperty<Integer> REMOVE_ITEM = new SpriteProperty<>(
            ID_REMOVE_ITEM,
            (consumer, packetByteBuf) -> consumer.accept((int) packetByteBuf.readUnsignedByte()),
            (integer, packetByteBuf) -> packetByteBuf.writeByte(integer)
    );
    public static final SpriteProperty<Byte> SET_TYPE_GENERIC3X3 = new SpriteProperty<>(
            ID_SET_TYPE_GENERIC3x3,
            (consumer, packetByteBuf) -> consumer.accept(packetByteBuf.readByte()),
            (aByte, packetByteBuf) -> packetByteBuf.writeByte(aByte)
    );
    public static final SpriteProperty<String> ANVIL_ITEM_NAME_AND_AVAILABILITY = new SpriteProperty<>(
            ID_ANVIL_ITEM_NAME_AND_AVAILABILITY,
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
    public static final SpriteProperty<Boolean> ANVIL_CAN_FIX = new BooleanProperty(SpriteProperty.ID_ANVIL_CAN_FIX);


    private final int propertyID;

    private final BiConsumer<Consumer<T>, PacketByteBuf> packetBytesReader;

    private final BiConsumer<T, PacketByteBuf> packetBytesWriter;

    private SpriteProperty(int propertyID, BiConsumer<Consumer<T>, PacketByteBuf> packetByteReader, BiConsumer<T, PacketByteBuf> packetBytesWriter) {
        this.propertyID = propertyID;
        PROPERTY_TYPE_ID_MAP.put(propertyID, this);
        this.packetBytesReader = packetByteReader;
        this.packetBytesWriter = packetBytesWriter;
    }

    public int getPropertyID() {
        return this.propertyID;
    }

    public void readPacketBytes(Consumer<T> consumer, PacketByteBuf packetByteBuf) {
        this.packetBytesReader.accept(consumer, packetByteBuf);
    }

    public void writePacketBytes(PacketByteBuf packetByteBuf, T target) {
        packetByteBuf.writeByte(propertyID);
        this.packetBytesWriter.accept(target, packetByteBuf);
    }

    public int hashCode() {
        return this.propertyID;
    }

    public static SpriteProperty getTypeByID(int id) {
        return PROPERTY_TYPE_ID_MAP.getOrDefault(id, null);
    }

    private static class BooleanProperty extends SpriteProperty<Boolean> {
        private static final BiConsumer<Consumer<Boolean>, PacketByteBuf> BOOLEAN_READER = (consumer, packetByteBuf) -> consumer.accept(packetByteBuf.readBoolean());
        private static final BiConsumer<Boolean, PacketByteBuf> BOOLEAN_WRITER = (aBoolean, packetByteBuf) -> packetByteBuf.writeBoolean(aBoolean);

        private BooleanProperty(int propertyID) {
            super(propertyID, BOOLEAN_READER, BOOLEAN_WRITER);
        }
    }
}
