package dev.eeasee.hud_hanger.render;

import com.google.common.collect.Sets;
import dev.eeasee.hud_hanger.render.ui.InWorldUIRenderer;

import java.util.Set;

public class RenderManager {
    private final Set<InWorldUIRenderer> activeInWorldUIRenderers = Sets.newHashSet();

    public RenderManager() {

    }

    public void renderItems() {

    }

    public void renderFaces() {

    }

    public static class Factory {
        public InWorldUIRenderer of(UIType type) {
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
