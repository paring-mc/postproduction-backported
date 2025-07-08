package moe.paring.createlogisticsbackport.content.logistics.itemHatch;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import moe.paring.createlogisticsbackport.polyfill.behaviour.filtering.FilteringBehaviour2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ItemHatchBlockEntity extends SmartBlockEntity {

	public FilteringBehaviour2 filtering;

	public ItemHatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(filtering = new FilteringBehaviour2(this, new HatchFilterSlot()));
	}

}
