package dev.eeasee.gui_hanger.sprites.renderer;

import com.google.common.collect.Lists;
import dev.eeasee.gui_hanger.config.Configs;
import dev.eeasee.gui_hanger.fakes.IItem;
import dev.eeasee.gui_hanger.sprites.SpriteProperty;
import dev.eeasee.gui_hanger.sprites.SpriteType;
import dev.eeasee.gui_hanger.util.Vec2i;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public abstract class ContainerSprite extends BaseSprite {

    public static final EnumSet<SpriteProperty.PropertyType> PROPERTIES = BaseSprite.PROPERTIES.clone();

    static {
        PROPERTIES.add(SpriteProperty.PropertyType.ADD_ITEM);
        PROPERTIES.add(SpriteProperty.PropertyType.REMOVE_ITEM);
        PROPERTIES.add(SpriteProperty.PropertyType.SET_ITEMS);
    }

    private final Int2ObjectMap<Item> items = new Int2ObjectOpenHashMap<>();

    public ContainerSprite(int id, SpriteType type) {
        super(id, type);
    }

    public EnumSet<SpriteProperty.PropertyType> getProperties() {
        return PROPERTIES;
    }

    public void setItem(int index, Item item) {
        this.items.put(index, item);
    }

    public void removeItem(int index) {
        this.items.remove(index);
    }

    public Item getItem(int index) {
        return this.items.getOrDefault(index, null);
    }

    public Int2ObjectMap<Item> getItems() {
        return items;
    }

    @NotNull
    public List<Pair<Matrix4f, ItemStack>> putItemsRendering(float tickDelta) {
        Int2ObjectMap<Item> items = this.getItems();
        List<Pair<Matrix4f, ItemStack>> result = Lists.newArrayListWithCapacity(items.size());
        for (Int2ObjectMap.Entry<Item> entry : items.int2ObjectEntrySet()) {
            int i = entry.getIntKey();
            Item item = entry.getValue();
            Vec2i vec2i = this.getItemCoordinate(i);
            if (vec2i == null) {
                continue;
            }
            Matrix4f matrix4f = Matrix4f.translate(vec2i.x * SCALE_NUM, vec2i.y * SCALE_NUM, -0.07f * Configs.hungScreenScale * (((IItem) item).getIsBlockItem() ? 1 : 0.15f));
            result.add(new Pair<>(matrix4f, item.getStackForRender()));
        }
        return result;
    }


    @Override
    public void renderModels(MatrixStack matrices, float tickDelta, Camera camera, GameRenderer gameRenderer, TextureManager textureManager, BufferBuilderStorage bufferBuilders) {
        this.checkUpdateTransformingMatrix4f();

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        VertexConsumerProvider.Immediate immediate = bufferBuilders.getEntityVertexConsumers();

        Vec3d cameraPos = camera.getPos();
        Matrix4f itemTransformer = Matrix4f.translate(-(float) cameraPos.x, -(float) cameraPos.y, -(float) cameraPos.z);
        itemTransformer.multiply(this.getTransformer());
        itemTransformer.multiply(ITEM_SCALE);

        List<Pair<Matrix4f, ItemStack>> items = this.putItemsRendering(tickDelta);
        for (Pair<Matrix4f, ItemStack> pair : items) {
            Matrix4f origin = pair.getLeft();
            origin.multiply(itemTransformer);
            matrices.push();
            matrices.peek().getModel().multiply(origin);
            itemRenderer.renderItem(
                    null,
                    pair.getRight(),
                    ModelTransformation.Mode.GUI,
                    false,
                    matrices,
                    immediate,
                    null,
                    0x00f000f0,
                    OverlayTexture.DEFAULT_UV
            );
            matrices.pop();
        }
    }


}
