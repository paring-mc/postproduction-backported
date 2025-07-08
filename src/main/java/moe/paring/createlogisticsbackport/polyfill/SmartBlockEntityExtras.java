package moe.paring.createlogisticsbackport.polyfill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public interface SmartBlockEntityExtras<T extends SmartBlockEntity> {
    default void renderNameplateOnHover(T blockEntity, Component tag, float yOffset, PoseStack ms,
                                          MultiBufferSource buffer, int light) {
        Minecraft mc = Minecraft.getInstance();
        if (blockEntity.isVirtual())
            return;
        if (mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) > 4096.0f)
            return;
        HitResult hitResult = mc.hitResult;
        if (!(hitResult instanceof BlockHitResult bhr) || bhr.getType() == HitResult.Type.MISS || !bhr.getBlockPos()
                .equals(blockEntity.getBlockPos()))
            return;

        float f = yOffset + 0.25f;
        ms.pushPose();
        ms.translate(0.5, f, 0.5);
        ms.mulPose(mc.getEntityRenderDispatcher()
                .cameraOrientation());
        ms.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = ms.last()
                .pose();
        float f1 = mc.options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        float f2 = (float) (-mc.font.width(tag) / 2);
        mc.font.drawInBatch(tag, f2, 0, 553648127, false, matrix4f, buffer, Font.DisplayMode.SEE_THROUGH, j, light);
        mc.font.drawInBatch(tag, f2, 0, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, light);
        ms.popPose();
    }
}
