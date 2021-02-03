package dev.eeasee.floating_gui.render.in_world_UI_renderer;

import com.google.common.collect.Sets;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.Set;

public interface InWorldUIRenderer {

    void addItem(ItemStack stack, int x, int y);

    void setMouse(int x, int y);

    int getUIWidth();

    int getUIHeight();

    void render(MatrixStack matrices, float tickDelta, long limitTime, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f);

    public static final Set<InWorldUIRenderer> ACTIVE_IN_GAME_INTERFACE_RENDERERS = Sets.newHashSet();
}
