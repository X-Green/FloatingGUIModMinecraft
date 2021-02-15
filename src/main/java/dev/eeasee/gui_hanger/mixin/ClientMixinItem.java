package dev.eeasee.gui_hanger.mixin;

import dev.eeasee.gui_hanger.fakes.IItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ClientMixinItem implements IItem {
    private boolean isBlock = false;

    @Override
    public void setIsBlockItem(boolean b) {
        this.isBlock = b;
    }

    @Override
    public boolean getIsBlockItem() {
        return this.isBlock;
    }
}
