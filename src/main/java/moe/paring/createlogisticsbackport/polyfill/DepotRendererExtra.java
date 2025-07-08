package moe.paring.createlogisticsbackport.polyfill;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public interface DepotRendererExtra {
    static void renderItem(Level level, PoseStack ms, MultiBufferSource buffer, int light, int overlay,
                           ItemStack itemStack, int angle, Random r, Vec3 itemPosition, boolean alwaysUpright) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        var msr = TransformStack.cast(ms);
        int count = (int) (Mth.log2((int) (itemStack.getCount()))) / 2;
        BakedModel bakedModel = itemRenderer.getModel(itemStack, null, null, 0);
        boolean blockItem = bakedModel.isGui3d();
        boolean renderUpright = BeltHelper.isItemUpright(itemStack) || alwaysUpright && !blockItem;

        ms.pushPose();
        msr.rotateY(angle);

        if (renderUpright) {
            Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
            if (renderViewEntity != null) {
                Vec3 positionVec = renderViewEntity.position();
                Vec3 vectorForOffset = itemPosition;
                Vec3 diff = vectorForOffset.subtract(positionVec);
                float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
                ms.mulPose(Axis.YP.rotation(yRot));
            }
            ms.translate(0, 3 / 32d, -1 / 16f);
        }

        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem && r != null)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);

            if (PackageItem.isPackage(itemStack) && !alwaysUpright) {
                ms.translate(0, 4 / 16f, 0);
                ms.scale(1.5f, 1.5f, 1.5f);
            } else if (blockItem && alwaysUpright) {
                ms.translate(0, 1 / 16f, 0);
                ms.scale(.755f, .755f, .755f);
            } else
                ms.scale(.5f, .5f, .5f);

            if (!blockItem && !renderUpright) {
                ms.translate(0, -3 / 16f, 0);
                msr.rotateX(90);
            }
            itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, ms, buffer, light, overlay, bakedModel);
            ms.popPose();

            if (!renderUpright) {
                if (!blockItem)
                    msr.rotateY(10);
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else
                ms.translate(0, 0, -1 / 16f);
        }

        ms.popPose();
    }
}
