package moe.paring.createlogisticsbackport.content.redstone.diodes;

import com.simibubi.create.content.redstone.diodes.BrassDiodeBlockEntity;
import moe.paring.createlogisticsbackport.mixin.BrassDiodeBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.redstone.diodes.BrassDiodeBlock.POWERING;

public class PulseTimerBlockEntity extends BrassDiodeBlockEntity implements IHaveBrassDiodeDefaultValue {
	public PulseTimerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public int defaultValue() {
		return 20;
	}

	@Override
	protected void updateState(boolean powered, boolean powering, boolean atMax, boolean atMin) {
		var maxState = ((BrassDiodeBlockEntityAccessor)this).getMaxState();
		if (powered || state >= maxState.getValue() - 1)
			state = 0;
		else
			state++;

		if (level.isClientSide)
			return;

		boolean shouldPower = !powered && (maxState.getValue() == 2 ? state == 0 : state <= 1);
		BlockState blockState = getBlockState();
		if (blockState.getValue(POWERING) != shouldPower)
			level.setBlockAndUpdate(worldPosition, blockState.setValue(POWERING, shouldPower));
	}

}
