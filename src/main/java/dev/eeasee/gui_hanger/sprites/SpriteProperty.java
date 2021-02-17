package dev.eeasee.gui_hanger.sprites;

import dev.eeasee.gui_hanger.sprites.renderer.AnvilSprite;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import dev.eeasee.gui_hanger.sprites.renderer.ContainerSprite;
import dev.eeasee.gui_hanger.sprites.renderer.Generic3x3Sprite;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.Registry;

import java.util.EnumMap;
import java.util.function.BiConsumer;

public class SpriteProperty<T> {

    private static final EnumMap<PropertyType, SpriteProperty> PROPERTY_TYPE_ID_MAP = new EnumMap<>(PropertyType.class);

    public static final SpriteProperty<Vector3f> POSITION = new SpriteProperty<>(
            PropertyType.POSITION,
            (baseSprite, packetByteBuf) -> baseSprite.setPos(new Vector3f(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat())),
            (baseSprite, packetByteBuf) -> packetByteBuf.writeFloat(baseSprite.getPos().getX()).writeFloat(baseSprite.getPos().getY()).writeFloat(baseSprite.getPos().getZ()),
            (vector3f, packetByteBuf) -> {
                packetByteBuf.writeFloat(vector3f.getX());
                packetByteBuf.writeFloat(vector3f.getY());
                packetByteBuf.writeFloat(vector3f.getZ());
            }
    );
    public static final SpriteProperty<Vec2f> YAW_PITCH = new SpriteProperty<>(
            PropertyType.YAW_PITCH,
            (baseSprite, packetByteBuf) -> baseSprite.setYawPitch(packetByteBuf.readFloat(), packetByteBuf.readFloat()),
            (baseSprite, packetByteBuf) -> packetByteBuf.writeFloat(baseSprite.getYaw()).writeFloat(baseSprite.getPitch()),
            (vec2f, packetByteBuf) -> packetByteBuf.writeFloat(vec2f.x).writeFloat(vec2f.y)
    );
    public static final SpriteProperty<Pair<Integer, Item>> ADD_ITEM = new SpriteProperty<>(
            PropertyType.ADD_ITEM,
            (baseSprite, packetByteBuf) -> ((ContainerSprite) baseSprite).setItem(packetByteBuf.readUnsignedByte(), Registry.ITEM.get(packetByteBuf.readUnsignedShort())),
            (baseSprite, packetByteBuf) -> {
            },
            (itemPair, packetByteBuf) -> {
                packetByteBuf.writeByte(itemPair.getLeft());
                packetByteBuf.writeShort(Registry.ITEM.getRawId(itemPair.getRight()));
            }
    );
    public static final SpriteProperty<Int2ObjectMap<Item>> SET_ITEMS = new SpriteProperty<>(
            PropertyType.SET_ITEMS,
            (baseSprite, packetByteBuf) -> {
                while (true) {
                    short itemIndex = packetByteBuf.readUnsignedByte();
                    if (itemIndex == 255)
                        break;
                    ((ContainerSprite) baseSprite).setItem(itemIndex, Registry.ITEM.get(packetByteBuf.readUnsignedShort()));
                }
            },
            (baseSprite, packetByteBuf) -> {
                ((ContainerSprite) baseSprite).getItems().forEach((integer, item) -> packetByteBuf.writeByte(integer).writeShort(Registry.ITEM.getRawId(item)));
                packetByteBuf.writeByte(255);
            },
            (itemInt2ObjectMap, packetByteBuf) -> {
                itemInt2ObjectMap.forEach((integer, item) -> packetByteBuf.writeByte(integer).writeShort(Registry.ITEM.getRawId(item)));
                packetByteBuf.writeByte(255);
            }
    );
    public static final SpriteProperty<Integer> REMOVE_ITEM = new SpriteProperty<>(
            PropertyType.REMOVE_ITEM,
            (baseSprite, packetByteBuf) -> ((ContainerSprite) baseSprite).removeItem(packetByteBuf.readUnsignedByte()),
            (baseSprite, packetByteBuf) -> {
            },
            (integer, packetByteBuf) -> packetByteBuf.writeByte(integer)
    );
    public static final SpriteProperty<Byte> SET_TYPE_GENERIC3X3 = new SpriteProperty<>(
            PropertyType.SET_TYPE_GENERIC3x3,
            (baseSprite, packetByteBuf) -> ((Generic3x3Sprite) baseSprite).set3x3ContainerType(packetByteBuf.readByte()),
            (baseSprite, packetByteBuf) -> packetByteBuf.writeByte(((Generic3x3Sprite) baseSprite).get3x3ContainerType()),
            (aByte, packetByteBuf) -> packetByteBuf.writeByte(aByte)
    );
    public static final SpriteProperty<String> ANVIL_ITEM_NAME_AND_AVAILABILITY = new SpriteProperty<>(
            PropertyType.ANVIL_ITEM_NAME_AND_AVAILABILITY,
            (baseSprite, packetByteBuf) -> ((AnvilSprite) baseSprite).setAnvilItemNameDisplay(packetByteBuf.readBoolean() ? packetByteBuf.readString() : null),
            (baseSprite, packetByteBuf) -> {
                if (((AnvilSprite) baseSprite).getAnvilItemNameDisplay() == null)
                    packetByteBuf.writeBoolean(false);
                else {
                    packetByteBuf.writeBoolean(true);
                    packetByteBuf.writeString(((AnvilSprite) baseSprite).getAnvilItemNameDisplay());
                }
            },
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
            (baseSprite, packetByteBuf) -> packetByteBuf.writeBoolean(((AnvilSprite) baseSprite).getCanFixItem()),
            (b, packetByteBuf) -> packetByteBuf.writeBoolean(b)
    );


    private final PropertyType type;

    private final BiConsumer<BaseSprite, PacketByteBuf> packet2SpriteReader;
    private final BiConsumer<BaseSprite, PacketByteBuf> sprite2PacketWriter;
    private final BiConsumer<T, PacketByteBuf> dataPacketWriter;

    private SpriteProperty(PropertyType type, BiConsumer<BaseSprite, PacketByteBuf> packetByteReader, BiConsumer<BaseSprite, PacketByteBuf> sprite2PacketWriter, BiConsumer<T, PacketByteBuf> dataPacketWriter) {
        this.type = type;
        PROPERTY_TYPE_ID_MAP.put(type, this);
        this.packet2SpriteReader = packetByteReader;
        this.sprite2PacketWriter = sprite2PacketWriter;
        this.dataPacketWriter = dataPacketWriter;
    }

    public PropertyType getType() {
        return this.type;
    }

    public void readPacketBytesToSprite(BaseSprite sprite, PacketByteBuf packetByteBuf) {
        this.packet2SpriteReader.accept(sprite, packetByteBuf);
    }

    public void writePacketBytesFromSprite(BaseSprite sprite, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeByte(this.getType().ordinal());
        this.sprite2PacketWriter.accept(sprite, packetByteBuf);
    }

    public void write(T data, PacketByteBuf packetByteBuf) {
        type.writeOrdinalToPacket(packetByteBuf);
        this.dataPacketWriter.accept(data, packetByteBuf);
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
        POSITION(true),
        YAW_PITCH(true),
        ADD_ITEM,
        SET_ITEMS(true),
        REMOVE_ITEM,
        SET_TYPE_GENERIC3x3(true),
        ANVIL_ITEM_NAME_AND_AVAILABILITY(true),
        ANVIL_CAN_FIX(true);

        public final boolean initial;

        PropertyType(boolean initial) {
            this.initial = initial;
        }

        PropertyType() {
            this.initial = false;
        }

        public void writeOrdinalToPacket(PacketByteBuf packetByteBuf) {
            packetByteBuf.writeByte(this.ordinal());
        }
    }
}
