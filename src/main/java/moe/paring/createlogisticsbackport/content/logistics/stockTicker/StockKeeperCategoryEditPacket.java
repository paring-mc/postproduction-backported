package moe.paring.createlogisticsbackport.content.logistics.stockTicker;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StockKeeperCategoryEditPacket extends BlockEntityConfigurationPacket<StockTickerBlockEntity> {

	private List<ItemStack> schedule;

	public StockKeeperCategoryEditPacket(BlockPos pos, List<ItemStack> schedule) {
		super(pos);
		this.schedule = schedule;
	}
	
	public StockKeeperCategoryEditPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		int size = buffer.readVarInt();
		schedule = new ArrayList<>();
		for (int i = 0; i < size; i++)
			schedule.add(buffer.readItem());
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeVarInt(schedule.size());
		schedule.forEach(buffer::writeItem);
	}

	@Override
	protected void applySettings(StockTickerBlockEntity be) {
		be.categories = schedule;
		be.notifyUpdate();
	}

}
