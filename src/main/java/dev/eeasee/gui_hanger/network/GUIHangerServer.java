package dev.eeasee.gui_hanger.network;

import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.CraftingTableSprite;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class GUIHangerServer {

    public final MinecraftServer minecraftServer;
    public final GUIHangerServerNetworkHandler networkHandler;

    public GUIHangerServer(MinecraftServer server) {
        this.minecraftServer = server;
        this.networkHandler = new GUIHangerServerNetworkHandler(server);
        //todo: del this
        CraftingTableSprite craftingTableSprite = new CraftingTableSprite(0);
        craftingTableSprite.setPos(new BlockPos(-60, 82, -216));
        craftingTableSprite.setMouse(176, 166);
        craftingTableSprite.setYawPitch(0, 0);
        SpriteManager.ACTIVE_SPRITES.put(0, craftingTableSprite);
        craftingTableSprite.getItems().put(0, Items.WOODEN_SWORD);
        craftingTableSprite.getItems().put(1, Items.STONE_SWORD);
        craftingTableSprite.getItems().put(2, Items.IRON_SWORD);
        craftingTableSprite.getItems().put(3, Items.GOLDEN_SWORD);
        craftingTableSprite.getItems().put(4, Items.DIAMOND_SWORD);
        craftingTableSprite.getItems().put(8, Items.SUGAR);
        craftingTableSprite.getItems().put(9, Items.PINK_BED);
        craftingTableSprite.getItems().put(18, Items.GRASS_BLOCK);
        craftingTableSprite.getItems().put(27, Items.DIAMOND_BLOCK);
        craftingTableSprite.getItems().put(36, Items.IRON_INGOT);
        craftingTableSprite.getItems().put(37, Items.GOLD_INGOT);
        craftingTableSprite.getItems().put(38, Items.REDSTONE);
        craftingTableSprite.getItems().put(39, Items.EMERALD);
        craftingTableSprite.getItems().put(44, Items.DIAMOND);
        craftingTableSprite.getItems().put(45, Items.BEACON);
    }

    public void tick() {

    }

    public void onServerClosed() {
        this.networkHandler.close();
    }
}
