package moe.paring.createlogisticsbackport.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import moe.paring.createlogisticsbackport.polyfill.DepotRendererExtra;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(DepotRenderer.class)
public class DepotRendererMixin {
    @Redirect(method = "renderItemsOf", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/depot/DepotRenderer;renderItem(Lnet/minecraft/world/level/Level;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/world/item/ItemStack;ILjava/util/Random;Lnet/minecraft/world/phys/Vec3;)V"), remap = false)
    private static void renderItem(Level level, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack,
                                   int angle, Random r, Vec3 itemPosition) {
        DepotRendererExtra.renderItem(level, ms, buffer, light, overlay, itemStack, angle, r, itemPosition, false);
    }
}
