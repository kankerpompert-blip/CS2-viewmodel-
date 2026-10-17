package dev.cs2view.mixin;

import dev.cs2view.CS2ViewModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    // 1.21.11 (yarn 1.21.11+build.4): last parameter before light is an OrderedRenderCommandQueue
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void cs2$pre(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand,
                         float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
                         OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        if (!item.isIn(ItemTags.SWORDS)) return;
        matrices.push();
        CS2ViewModel.applyKnifeTransform(matrices);
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
    private void cs2$post(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand,
                          float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
                          OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        if (!item.isIn(ItemTags.SWORDS)) return;
        matrices.pop();
    }
}
