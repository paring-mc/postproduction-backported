package moe.paring.createlogisticsbackport.content.logistics.factoryBoard;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelBlock.PanelSlot;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBoxTransform2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

class FactoryPanelSlotPositioning extends ValueBoxTransform2 {

	public PanelSlot slot;

	public FactoryPanelSlotPositioning(PanelSlot slot) {
		this.slot = slot;
	}

	@Override
	public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
		return getCenterOfSlot(state, slot);
	}

	public static Vec3 getCenterOfSlot(BlockState state, PanelSlot slot) {
		Vec3 vec = new Vec3(.25 + slot.xOffset * .5, 1.5 / 16f, .25 + slot.yOffset * .5);
		vec = VecHelper.rotateCentered(vec, 180, Axis.Y);
		vec = VecHelper.rotateCentered(vec, Mth.RAD_TO_DEG * FactoryPanelBlock.getXRot(state) + 90, Axis.X);
		vec = VecHelper.rotateCentered(vec, Mth.RAD_TO_DEG * FactoryPanelBlock.getYRot(state), Axis.Y);
		return vec;
	}

	@Override
	public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
		Vec3 offset = getLocalOffset(level, pos, state);
		if (offset == null)
			return false;
		return localHit.distanceTo(offset) < scale / 2;
	}

	@Override
	public float getScale() {
		return super.getScale();
	}

	@Override
	public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
		TransformStack.cast(ms)
			.rotate(Direction.UP, FactoryPanelBlock.getYRot(state) + Mth.PI)
			.rotate(Direction.EAST, -FactoryPanelBlock.getXRot(state));
	}

}
