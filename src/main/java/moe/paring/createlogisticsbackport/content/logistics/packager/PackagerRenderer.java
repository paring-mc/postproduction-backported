package moe.paring.createlogisticsbackport.content.logistics.packager;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PackagerRenderer extends SmartBlockEntityRenderer<PackagerBlockEntity> {

    public PackagerRenderer(Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(PackagerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        ItemStack renderedBox = be.getRenderedBox();
        float trayOffset = be.getTrayOffset(partialTicks);
        BlockState blockState = be.getBlockState();
        Direction facing = blockState.getValue(PackagerBlock.FACING)
                .getOpposite();

        if (!Backend.canUseInstancing(be.getLevel())) {
            var hatchModel = getHatchModel(be);

            SuperByteBuffer sbb = CachedBufferer.partial(hatchModel, blockState);
            sbb.translate(Vec3.atLowerCornerOf(facing.getNormal())
                            .scale(.49999f))
                    .centre()
                    .rotateY(AngleHelper.horizontalAngle(facing))
                    .rotateX(AngleHelper.verticalAngle(facing))
                    .unCentre()
                    .light(light)
                    .renderInto(ms, buffer.getBuffer(RenderType.solid()));

            sbb = CachedBufferer.partial(getTrayModel(blockState), blockState);
            sbb.translate(Vec3.atLowerCornerOf(facing.getNormal())
                            .scale(trayOffset))
                    .centre()
                    .rotateY(facing.toYRot())
                    .unCentre()
                    .light(light)
                    .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }

        if (!renderedBox.isEmpty()) {
            ms.pushPose();
            var msr = TransformStack.cast(ms);
            msr.translate(Vec3.atLowerCornerOf(facing.getNormal())
                            .scale(trayOffset))
                    .translate(.5f, .5f, .5f)
                    .rotateY(facing.toYRot())
                    .translate(0, 2 / 16f, 0)
                    .scale(1.49f, 1.49f, 1.49f);
            Minecraft.getInstance()
                    .getItemRenderer()
                    .renderStatic(null, renderedBox, ItemDisplayContext.FIXED, false, ms, buffer, be.getLevel(), light,
                            overlay, 0);
            ms.popPose();
        }
    }

    public static PartialModel getTrayModel(BlockState blockState) {
        return ExtraBlocks.PACKAGER.has(blockState) ? ExtraPartialModels.PACKAGER_TRAY_REGULAR
                : ExtraPartialModels.PACKAGER_TRAY_DEFRAG;
    }

    public static PartialModel getHatchModel(PackagerBlockEntity be) {
        return isHatchOpen(be) ? ExtraPartialModels.PACKAGER_HATCH_OPEN : ExtraPartialModels.PACKAGER_HATCH_CLOSED;
    }

    public static boolean isHatchOpen(PackagerBlockEntity be) {
        return be.animationTicks > (be.animationInward ? 1 : 5)
                && be.animationTicks < PackagerBlockEntity.CYCLE - (be.animationInward ? 5 : 1);
    }

}
