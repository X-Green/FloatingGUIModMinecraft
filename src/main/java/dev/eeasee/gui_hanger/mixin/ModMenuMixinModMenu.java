package dev.eeasee.gui_hanger.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.prospector.modmenu.ModMenu;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Function;

@Pseudo
@Mixin(ModMenu.class)
public class ModMenuMixinModMenu {

}
