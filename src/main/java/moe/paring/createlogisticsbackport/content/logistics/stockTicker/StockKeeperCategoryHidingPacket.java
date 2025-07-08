package moe.paring.createlogisticsbackport.content.logistics.stockTicker;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.stream.IntStream;

public class StockKeeperCategoryHidingPacket extends BlockEntityConfigurationPacket<StockTickerBlockEntity> {

	private List<Integer> indices;

	public StockKeeperCategoryHidingPacket(BlockPos pos, List<Integer> indices) {
		super(pos);
		this.indices = indices;
	}

	public StockKeeperCategoryHidingPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeVarIntArray(indices.stream()
			.mapToInt(i -> i)
			.toArray());
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		indices = IntStream.of(buffer.readVarIntArray())
			.boxed()
			.toList();
	}

	@Override
	protected void applySettings(StockTickerBlockEntity be) {}

	@Override
	protected void applySettings(ServerPlayer player, StockTickerBlockEntity be) {
		if (indices.isEmpty()) {
			be.hiddenCategoriesByPlayer.remove(player.getUUID());
		} else {
			be.hiddenCategoriesByPlayer.put(player.getUUID(), indices);
			be.notifyUpdate();
		}
		return;
	}

}
