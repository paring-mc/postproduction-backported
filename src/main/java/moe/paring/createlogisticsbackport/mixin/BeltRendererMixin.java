package moe.paring.createlogisticsbackport.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.belt.BeltRenderer;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeltRenderer.class)
public class BeltRendererMixin {
    @Unique
    private static boolean create_logistics_backport$isBox;

    @Redirect(method = "renderItems", at = @At(value = "FIELD", target = "Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;beltPosition:F"), remap = false)
    private float sans(TransportedItemStack instance) {
        create_logistics_backport$isBox = PackageItem.isPackage(instance.stack);
        return instance.beltPosition;
    }

//    @Inject(method = "renderItems", at = @At(value = "LOAD", target = "Lnet/minecraft/world/phys/Vec3;atLowerCornerOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void renderItems(BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
//
//    }

    @Redirect(method = "renderItems", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 3))
    private void onTranslate(PoseStack instance, float x, float y, float z) {
        if (create_logistics_backport$isBox) return;
        instance.translate(x, y, z);
    }

    @Redirect(method = "renderItems", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void scale(PoseStack ms, float x, float y, float z) {
        if (create_logistics_backport$isBox) {
            ms.translate(0, 4 / 16f, 0);
            ms.scale(1.5f, 1.5f, 1.5f);
        } else {
            ms.scale(.5f, .5f, .5f);
        }
    }
}
