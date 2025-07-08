package moe.paring.createlogisticsbackport.content.kinetics.chainConveyor;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;

import moe.paring.createlogisticsbackport.config.ExtraConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ServerboundChainConveyorRidingPacket extends BlockEntityConfigurationPacket<ChainConveyorBlockEntity> {

	private boolean stop;

	public ServerboundChainConveyorRidingPacket(BlockPos pos, boolean stop) {
		super(pos);
		this.stop = stop;
	}

	public ServerboundChainConveyorRidingPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeBoolean(stop);
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		stop = buffer.readBoolean();
	}

	@Override
	protected int maxRange() {
		return ExtraConfigs.server().kinetics.maxChainConveyorLength.get() * 2;
	}
	
	@Override
	protected void applySettings(ChainConveyorBlockEntity be) {}

	@Override
	protected void applySettings(ServerPlayer sender, ChainConveyorBlockEntity be) {
		sender.fallDistance = 0;
		sender.connection.aboveGroundTickCount = 0;
		sender.connection.aboveGroundVehicleTickCount = 0;

		if (stop)
			ServerChainConveyorHandler.handleStopRidingPacket(sender);
		else
			ServerChainConveyorHandler.handleTTLPacket(sender);
	}

}
