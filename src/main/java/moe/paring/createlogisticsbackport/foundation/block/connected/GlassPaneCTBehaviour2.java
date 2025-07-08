package moe.paring.createlogisticsbackport.foundation.block.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GlassPaneCTBehaviour2 extends SimpleCTBehaviour2 {

	public GlassPaneCTBehaviour2(CTSpriteShiftEntry2 shift) {
		super(shift);
	}
	
	@Override
	public boolean buildContextForOccludedDirections() {
		return true;
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos,
		Direction face) {
		return state.getBlock() == other.getBlock();
	}

	@Override
	protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
		if (face.getAxisDirection() == AxisDirection.NEGATIVE)
			return true;
		return super.reverseUVsHorizontally(state, face);
	}
}
