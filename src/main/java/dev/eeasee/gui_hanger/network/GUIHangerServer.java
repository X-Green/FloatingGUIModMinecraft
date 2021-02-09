package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.render.HangedGUIRenderManager;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;

public class GUIHangerServer {

    public final MinecraftServer minecraftServer;
    public final GUIHangerServerNetworkHandler networkHandler;

    public GUIHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new GUIHangerServerNetworkHandler(server);
        //todo: del this
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(0, Items.WOODEN_SWORD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(1, Items.STONE_SWORD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(2, Items.IRON_SWORD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(3, Items.GOLDEN_SWORD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(4, Items.DIAMOND_SWORD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(8, Items.SUGAR);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(9, Items.PINK_BED);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(18, Items.GRASS_BLOCK);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(27, Items.DIAMOND_BLOCK);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(36, Items.IRON_INGOT);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(37, Items.GOLD_INGOT);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(38, Items.REDSTONE);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(39, Items.EMERALD);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(44, Items.DIAMOND);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.get(0).getItems().put(45, Items.BEACON);
    }

    public void tick() {

    }

    public void onServerClosed() {
        this.networkHandler.close();
    }
}
