package dev.eeasee.gui_hanger.render.renderer;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public abstract class ContainerGUIRenderer extends BaseGUIRenderer {

    private final Int2ObjectMap<Item> items = new Int2ObjectOpenHashMap<>();

    public ContainerGUIRenderer(int id) {
        super(id);
    }

    @Override
    public @NotNull Int2ObjectMap<Item> getItems() {
        return items;
    }


}
