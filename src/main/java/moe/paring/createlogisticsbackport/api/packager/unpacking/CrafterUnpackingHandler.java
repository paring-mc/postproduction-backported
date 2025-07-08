package moe.paring.createlogisticsbackport.api.packager.unpacking;

import com.simibubi.create.content.kinetics.crafter.ConnectedInputHandler.ConnectedInput;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity.Inventory;
import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.PackageOrderWithCrafts;
import moe.paring.createlogisticsbackport.mixin.MechanicalCrafterBlockEntityAccessor;
import moe.paring.createlogisticsbackport.polyfill.ConnectedInputExtra;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum CrafterUnpackingHandler implements UnpackingHandler {
	INSTANCE;

	@Override
	public boolean unpack(Level level, BlockPos pos, BlockState state, Direction side, List<ItemStack> items, @Nullable PackageOrderWithCrafts orderContext, boolean simulate) {
		if (!PackageOrderWithCrafts.hasCraftingInformation(orderContext))
			return DEFAULT.unpack(level, pos, state, side, items, null, simulate);

		// Get item placement
		List<BigItemStack> craftingContext = orderContext.getCraftingInformation();

		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof MechanicalCrafterBlockEntity crafter))
			return false;

		ConnectedInput input = ((MechanicalCrafterBlockEntityAccessor)crafter).getInput();
		List<Inventory> inventories = ConnectedInputExtra.getInventories(input, level, pos);
		if (inventories.isEmpty())
			return false;

		// insert in the order's defined ordering
		int max = Math.min(inventories.size(), craftingContext.size());
		outer: for (int i = 0; i < max; i++) {
			BigItemStack targetStack = craftingContext.get(i);
			if (targetStack.stack.isEmpty())
				continue;

			Inventory inventory = inventories.get(i);
			// if there's already an item here, no point in trying
			if (!inventory.getStackInSlot(0).isEmpty())
				continue;

			// go through each item in the box and try insert if it matches the target
			for (ItemStack stack : items) {
				if (ItemHandlerHelper.canItemStacksStack(stack, targetStack.stack)) {
					ItemStack toInsert = stack.copyWithCount(1);
					if (inventory.insertItem(0, toInsert, simulate).isEmpty()) {
						stack.shrink(1);
						// one item per crafter, move to next once successful
						continue outer;
					}
				}
			}
		}

		// if anything is still non-empty insertion failed
		for (ItemStack item : items) {
			if (!item.isEmpty()) {
				return false;
			}
		}

		if (!simulate) {
			((MechanicalCrafterBlockEntityAccessor)crafter).invokeCheckCompletedRecipe(true);
		}

		return true;
	}
}
