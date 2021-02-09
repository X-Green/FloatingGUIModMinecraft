package dev.eeasee.gui_hanger;

import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.CraftingTableSprite;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIHangerMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "gui_hanger";

    @Override
    public void onInitialize() {
        System.out.println("Reeeeee!");
        CraftingTableSprite craftingTableRenderer = new CraftingTableSprite(0);
        craftingTableRenderer.setPos(new BlockPos(-60, 82, -216));
        craftingTableRenderer.setMouse(176, 166);
        craftingTableRenderer.setYawPitch(0, 0);
        SpriteManager.ACTIVE_SPRITES.put(0, craftingTableRenderer);
    }

    public static void tellPlayer(Object o) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText(o.toString()), true);
        }
    }
}
