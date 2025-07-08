package moe.paring.createlogisticsbackport.content.logistics.tableCloth;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBoxTransform2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

class TableClothFilterSlot extends ValueBoxTransform2 {

	private TableClothBlockEntity be;

	public TableClothFilterSlot(TableClothBlockEntity be) {
		this.be = be;
	}

	@Override
	public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
		Vec3 v = be.sideOccluded ? VecHelper.voxelSpace(8, 0.75, 15.25) : VecHelper.voxelSpace(12, -2.75, 16.75);
		return VecHelper.rotateCentered(v, -be.facing.toYRot(), Axis.Y);
	}

	@Override
	public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
		TransformStack.cast(ms)
			.rotateY(180 - be.facing.toYRot())
			.rotateX(be.sideOccluded ? 90 : 0);
	}

}
