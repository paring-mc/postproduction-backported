package moe.paring.createlogisticsbackport.content.logistics.stockTicker;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticsNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class StockKeeperLockPacket extends BlockEntityConfigurationPacket<StockTickerBlockEntity> {

	private boolean lock;

	public StockKeeperLockPacket(BlockPos pos, boolean lock) {
		super(pos);
		this.lock = lock;
	}

	public StockKeeperLockPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeBoolean(lock);
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		lock = buffer.readBoolean();
	}

	@Override
	protected void applySettings(StockTickerBlockEntity be) {}

	@Override
	protected void applySettings(ServerPlayer player, StockTickerBlockEntity be) {
		if (!be.behaviour.mayAdministrate(player))
			return;
		LogisticsNetwork network = CreateLogisticsBackport.LOGISTICS.logisticsNetworks.get(be.behaviour.freqId);
		if (network != null) {
			network.locked = lock;
			CreateLogisticsBackport.LOGISTICS.markDirty();
		}
	}

}
