package dev.eeasee.hud_hanger.render;

import dev.eeasee.hud_hanger.render.renderer.HungGUIBaseRenderer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;

public class HangedGUIRenderManager {

    private final Int2ObjectMap<HungGUIBaseRenderer> activeHungGUIRenderers = new Int2ObjectOpenHashMap<>();

    public HangedGUIRenderManager() {

    }

    public void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer) {
        for (HungGUIBaseRenderer renderer : activeHungGUIRenderers.values()) {
            renderer.renderModels(matrices, tickDelta, camera, gameRenderer);
        }
    }

    public void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer) {
        for (HungGUIBaseRenderer renderer : activeHungGUIRenderers.values()) {
            renderer.renderFlat(matrices, tickDelta, camera, gameRenderer);
        }
    }

    public static class Factory {
        public HungGUIBaseRenderer of(GUIType type) {
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
