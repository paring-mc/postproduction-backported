package moe.paring.createlogisticsbackport.content.logistics.packager.repackager;

import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlock;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlockEntity;
import moe.paring.createlogisticsbackport.registry.ExtraBlockEntityTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class RepackagerBlock extends PackagerBlock {

	public RepackagerBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public BlockEntityType<? extends PackagerBlockEntity> getBlockEntityType() {
		return ExtraBlockEntityTypes.REPACKAGER.get();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

}
