package moe.paring.createlogisticsbackport.content.logistics.crate;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.logistics.crate.CrateBlock;
import com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CreativeCrateBlock extends CrateBlock implements IBE<com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity> {

	public CreativeCrateBlock(Properties p_i48415_1_) {
		super(p_i48415_1_);
	}

	@Override
	public Class<com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity> getBlockEntityClass() {
		return com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CreativeCrateBlockEntity> getBlockEntityType() {
		return AllBlockEntityTypes.CREATIVE_CRATE.get();
	}
}
