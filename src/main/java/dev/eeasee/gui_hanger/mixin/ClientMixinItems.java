package dev.eeasee.gui_hanger.mixin;

import dev.eeasee.gui_hanger.fakes.IItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ClientMixinItems {
    @Inject(method = "register(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"))
    private static void onRegisterItemAsBlockItem(Block block, Item iem, CallbackInfoReturnable<Item> cir) {
        ((IItem) iem).setIsBlockItem(true);
    }
}
