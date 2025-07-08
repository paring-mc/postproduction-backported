package moe.paring.createlogisticsbackport.content.redstone.deskBell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class DeskBellRenderer extends SmartBlockEntityRenderer<DeskBellBlockEntity> {

	public DeskBellRenderer(Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(DeskBellBlockEntity blockEntity, float partialTicks, PoseStack ms,
		MultiBufferSource buffer, int light, int overlay) {
		BlockState blockState = blockEntity.getBlockState();
		float p = blockEntity.animation.getValue(partialTicks);
		if (p < 0.004 && !blockState.getOptionalValue(DeskBellBlock.POWERED)
			.orElse(false))
			return;

		float f = (float) (1 - 4 * Math.pow((Math.max(p - 0.5, 0)) - 0.5, 2));
		float f2 = (float) (Math.pow(p, 1.25f));

		Direction facing = blockState.getValue(DeskBellBlock.FACING);

		CachedBufferer.partial(ExtraPartialModels.DESK_BELL_PLUNGER, blockState)
			.centre()
			.rotateY(AngleHelper.horizontalAngle(facing))
			.rotateX(AngleHelper.verticalAngle(facing) + 90)
			.unCentre()
			.translate(0, f * -.75f / 16f, 0)
			.light(light)
			.overlay(overlay)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));

		CachedBufferer.partial(ExtraPartialModels.DESK_BELL_BELL, blockState)
			.centre()
			.rotateY(AngleHelper.horizontalAngle(facing))
			.rotateX(AngleHelper.verticalAngle(facing) + 90)
			.translate(0, -1 / 16, 0)
			.rotateX(f2 * 8 * Mth.sin(p * Mth.PI * 4 + blockEntity.animationOffset))
			.rotateZ(f2 * 8 * Mth.cos(p * Mth.PI * 4 + blockEntity.animationOffset))
			.translate(0, 1 / 16, 0)
			.scale(0.995f)
			.unCentre()
			.light(light)
			.overlay(overlay)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));
	}

}
