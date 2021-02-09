package dev.eeasee.gui_hanger.sprites;

import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;

public class SpriteManager {

    public static final Int2ObjectMap<BaseSprite> ACTIVE_SPRITES = new Int2ObjectOpenHashMap<>();

    public static void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager, BufferBuilderStorage bufferBuilders) {
        for (BaseSprite renderer : ACTIVE_SPRITES.values()) {
            renderer.renderModels(matrices, tickDelta, camera, gameRenderer, textureManager, bufferBuilders);
        }
    }

    public static void renderFlat(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager) {
        for (BaseSprite renderer : ACTIVE_SPRITES.values()) {
            renderer.renderFlat(matrices, tickDelta, camera, gameRenderer, textureManager);
        }
    }

}
