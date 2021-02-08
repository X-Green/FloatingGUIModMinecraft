package dev.eeasee.gui_hanger;

import dev.eeasee.gui_hanger.render.HangedGUIRenderManager;
import dev.eeasee.gui_hanger.render.renderer.CraftingTableRenderer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIHangerMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "GUI Hanger";

    @Override
    public void onInitialize() {
        System.out.println("Reeeeee!");
        CraftingTableRenderer craftingTableRenderer = new CraftingTableRenderer(0);
        craftingTableRenderer.setPos(new Vec3d(-60, 84, -216));
        craftingTableRenderer.setMouse(176, 166);
        craftingTableRenderer.setYawPitch(0, 0);
        HangedGUIRenderManager.ACTIVE_HUNG_GUI_RENDERERS.put(0, craftingTableRenderer);
    }

    public static void tellPlayer(Object o) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText(o.toString()), true);
        }
    }
}
