package moe.paring.createlogisticsbackport.content.logistics.stockTicker;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import moe.paring.createlogisticsbackport.content.logistics.packager.IdentifiedInventory;
import moe.paring.createlogisticsbackport.content.logistics.packager.InventorySummary;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticallyLinkedBehaviour;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticallyLinkedBehaviour.RequestType;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticsManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public abstract class StockCheckingBlockEntity extends SmartBlockEntity {

	public LogisticallyLinkedBehaviour behaviour;

	public StockCheckingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(10);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(behaviour = new LogisticallyLinkedBehaviour(this, false));
	}

	public InventorySummary getRecentSummary() {
		return LogisticsManager.getSummaryOfNetwork(behaviour.freqId, false);
	}

	public InventorySummary getAccurateSummary() {
		return LogisticsManager.getSummaryOfNetwork(behaviour.freqId, true);
	}

	public boolean broadcastPackageRequest(RequestType type, PackageOrder order, @Nullable IdentifiedInventory ignoredHandler, String address) {
		return broadcastPackageRequest(type, PackageOrderWithCrafts.simple(order.stacks()), ignoredHandler, address);
	}

	public boolean broadcastPackageRequest(RequestType type, PackageOrderWithCrafts order, @Nullable IdentifiedInventory ignoredHandler, String address) {
		return LogisticsManager.broadcastPackageRequest(behaviour.freqId, type, order, ignoredHandler, address);
	}

}
