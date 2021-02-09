package dev.eeasee.gui_hanger.sprites;

import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import dev.eeasee.gui_hanger.sprites.renderer.CraftingTableSprite;

import java.util.function.Function;
import java.util.function.IntFunction;

public enum SpriteType {
    CRAFTING_TABLE(CraftingTableSprite::new, null),
    INVENTORY(null, null),
    CHEST(null, null),
    LARGE_CHEST(null, null),
    FURNACE(null, null),
    VILLAGER(null, null),
    ENCHANTING_TABLE(null, null),
    BREWING_STAND(null, null),
    BEACON(null, null),
    GENERIC_3X3(null, null),
    HOPPER(null, null),
    ANVIL(null, null),
    // COMMAND_BLOCK(null, null),
    // JIGSAW_BLOCK(null, null),
    // STRUCTURE_BLOCK(null, null),
    LOOM(null, null),
    LECTERN(null, null),
    HORSE(null, null),
    GRINDSTONE(null, null),
    CARTOGRAPHY_TABLE(null, null),
    BLAST_FURNACE(null, null),
    SMOKER(null, null);

    private final IntFunction<BaseSprite> spriteGenerator;
    private final Function<BaseSprite, SpritePropertyParser> propertyParserSupplier;

    SpriteType(IntFunction<BaseSprite> spriteGenerator, Function<BaseSprite, SpritePropertyParser> propertyParserGenerator) {
        this.spriteGenerator = spriteGenerator;
        this.propertyParserSupplier = propertyParserGenerator;
    }

    public BaseSprite generateSprite(int id) {
        return this.spriteGenerator.apply(id);
    }

    public SpritePropertyParser generatePropertyParser(BaseSprite sprite) {
        return this.propertyParserSupplier.apply(sprite);
    }

}
