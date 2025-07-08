package moe.paring.createlogisticsbackport.api.packager.unpacking;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import moe.paring.createlogisticsbackport.accessor.BasinInventoryMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.PackageOrderWithCrafts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum BasinUnpackingHandler implements UnpackingHandler {
	INSTANCE;

	@Override
	public boolean unpack(Level level, BlockPos pos, BlockState state, Direction side, List<ItemStack> items, @Nullable PackageOrderWithCrafts orderContext, boolean simulate) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof BasinBlockEntity basin))
			return false;

		((BasinInventoryMixinAccessor)basin.inputInventory).setCreate_logistics_backport$packagerMode(true);

		try {
			return UnpackingHandler.DEFAULT.unpack(level, pos, state, side, items, orderContext, simulate);
		} finally {
			((BasinInventoryMixinAccessor)basin.inputInventory).setCreate_logistics_backport$packagerMode(false);
		}
	}
}
