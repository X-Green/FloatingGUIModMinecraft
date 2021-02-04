package dev.eeasee.hud_hanger.render;

import dev.eeasee.hud_hanger.render.renderer.HungGUIBaseRenderer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class HungGUIRenderManager {

    private final Int2ObjectMap<HungGUIBaseRenderer> activeHungGUIRenderers = new Int2ObjectOpenHashMap<>();

    public HungGUIRenderManager() {

    }

    public void renderModels() {

    }

    public void renderFaces() {

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
