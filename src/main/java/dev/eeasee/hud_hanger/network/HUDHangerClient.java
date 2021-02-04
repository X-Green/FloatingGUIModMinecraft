package dev.eeasee.hud_hanger.network;

import dev.eeasee.hud_hanger.render.HungGUIRenderManager;

public class HUDHangerClient {
    private static HUDHangerClient instance = new HUDHangerClient();

    public static HUDHangerClient getInstance() {
        return instance;
    }

    private HUDHangerClient() {

    }

    private HungGUIRenderManager renderManager = new HungGUIRenderManager();

    public HungGUIRenderManager getManager() {
        return this.renderManager;
    }
}
