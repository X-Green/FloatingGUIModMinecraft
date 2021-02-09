package dev.eeasee.gui_hanger.sprites.renderer;

import dev.eeasee.gui_hanger.sprites.SpriteType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

public abstract class ContainerSprite extends BaseSprite {

    private final Int2ObjectMap<Item> items = new Int2ObjectOpenHashMap<>();

    public ContainerSprite(int id, SpriteType type) {
        super(id, type);
    }

    @Override
    public @NotNull Int2ObjectMap<Item> getItems() {
        return items;
    }


}
