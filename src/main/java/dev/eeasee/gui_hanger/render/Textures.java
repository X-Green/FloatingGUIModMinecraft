package dev.eeasee.gui_hanger.render;

import dev.eeasee.gui_hanger.util.QuadVec2f;
import net.minecraft.util.Identifier;

public class Textures {
    public static final Identifier MOUSE_ICON = new Identifier("gui_hanger","texture/mouse_icon.png");
    public static final QuadVec2f MOUSE_ICON_TEXTURE_UV = new QuadVec2f(
            0, 1,
            1, 1,
            1, 0,
            0, 0
    );
}
