package dev.eeasee.gui_hanger.render;

import dev.eeasee.gui_hanger.render.renderer.GUIRendererBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;

public class HangedGUIRenderManager {

    public static final Int2ObjectMap<GUIRendererBase> ACTIVE_HUNG_GUI_RENDERERS = new Int2ObjectOpenHashMap<>();

    public static void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager) {
        for (GUIRendererBase renderer : ACTIVE_HUNG_GUI_RENDERERS.values()) {
            renderer.renderModels(matrices, tickDelta, camera, gameRenderer, textureManager);
        }
    }

    public static void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager) {
        for (GUIRendererBase renderer : ACTIVE_HUNG_GUI_RENDERERS.values()) {
            renderer.renderFlat(matrices, tickDelta, camera, gameRenderer, textureManager);
        }
    }

    public static class Factory {
        public GUIRendererBase of(GUIType type) {
            switch (type) {
                case CRAFTING_TABLE:
                    break;
                case INVENTORY:
                    break;
                case CHEST:
                    break;
                case LARGE_CHEST:
                    break;
            }
            return null;
        }
    }
}
