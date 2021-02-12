package dev.eeasee.gui_hanger.sprites;

import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import dev.eeasee.gui_hanger.sprites.renderer.CraftingTableSprite;
import dev.eeasee.gui_hanger.sprites.renderer.InventorySprite;

import java.util.function.Function;
import java.util.function.IntFunction;

public enum SpriteType {
    CRAFTING_TABLE(CraftingTableSprite::new),
    INVENTORY(InventorySprite::new),
    CHEST(null),
    LARGE_CHEST(null),
    FURNACE(null),
    VILLAGER(null),
    ENCHANTING_TABLE(null),
    BREWING_STAND(null),
    BEACON(null),
    GENERIC_3X3(null),
    HOPPER(null),
    ANVIL(null),
    // COMMAND_BLOCK(null),
    // JIGSAW_BLOCK(null),
    // STRUCTURE_BLOCK(null),
    LOOM(null),
    LECTERN(null),
    HORSE(null),
    GRINDSTONE(null),
    CARTOGRAPHY_TABLE(null),
    BLAST_FURNACE(null),
    SMOKER(null);

    private final IntFunction<BaseSprite> spriteGenerator;

    SpriteType(IntFunction<BaseSprite> spriteGenerator) {
        this.spriteGenerator = spriteGenerator;
    }

    public BaseSprite generateSprite(int id) {
        return this.spriteGenerator.apply(id);
    }
}
