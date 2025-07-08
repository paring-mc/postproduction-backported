package moe.paring.createlogisticsbackport.content.logistics.packagerLink;

import com.simibubi.create.foundation.utility.NBTHelper;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.foundation.utility.SavedDataUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogisticsNetworkSavedData extends SavedData {

	private Map<UUID, LogisticsNetwork> logisticsNetworks = new HashMap<>();

	@Override
	public CompoundTag save(CompoundTag nbt) {
		GlobalLogisticsManager logistics = CreateLogisticsBackport.LOGISTICS;
		nbt.put("LogisticsNetworks",
			NBTHelper.writeCompoundList(logistics.logisticsNetworks.values(), tg -> tg.write()));
		return nbt;
	}

	private static LogisticsNetworkSavedData load(CompoundTag nbt) {
		LogisticsNetworkSavedData sd = new LogisticsNetworkSavedData();
		sd.logisticsNetworks = new HashMap<>();
		NBTHelper.iterateCompoundList(nbt.getList("LogisticsNetworks", Tag.TAG_COMPOUND), c -> {
			LogisticsNetwork network = LogisticsNetwork.read(c);
			sd.logisticsNetworks.put(network.id, network);
		});
		return sd;
	}

	@Override
	public void save(@NotNull File file) {
		SavedDataUtil.saveWithDatOld(this, file);
	}

	public Map<UUID, LogisticsNetwork> getLogisticsNetworks() {
		return logisticsNetworks;
	}

	private LogisticsNetworkSavedData() {}

	public static LogisticsNetworkSavedData load(MinecraftServer server) {
		return server.overworld()
			.getDataStorage()
			.computeIfAbsent(LogisticsNetworkSavedData::load, LogisticsNetworkSavedData::new, "create_logistics");
	}

}
