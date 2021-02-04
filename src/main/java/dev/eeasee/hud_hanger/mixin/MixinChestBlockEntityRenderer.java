package dev.eeasee.hud_hanger.mixin;

import dev.eeasee.hud_hanger.HUDHangerMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class MixinChestBlockEntityRenderer<T extends BlockEntity & ChestAnimationProgress> {
    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void afterPop(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        matrices.translate(0, 1, 0);
        HUDHangerMod.tellPlayer(String.valueOf(light));
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                null,
                Items.REDSTONE_BLOCK.getStackForRender(),
                ModelTransformation.Mode.GROUND,
                false,
                matrices,
                vertexConsumers,
                null,
                light,
                overlay
        );
    }
}
