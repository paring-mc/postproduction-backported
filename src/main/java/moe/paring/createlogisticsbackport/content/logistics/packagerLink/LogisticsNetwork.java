package moe.paring.createlogisticsbackport.content.logistics.packagerLink;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.NBTHelper;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LogisticsNetwork {

	public UUID id;
	public RequestPromiseQueue panelPromises;

	public Set<GlobalPos> totalLinks;
	public Set<GlobalPos> loadedLinks;

	public UUID owner;
	public boolean locked;

	public LogisticsNetwork(UUID networkId) {
		id = networkId;
		panelPromises = new RequestPromiseQueue(CreateLogisticsBackport.LOGISTICS::markDirty);
		totalLinks = new HashSet<>();
		loadedLinks = new HashSet<>();
		owner = null;
		locked = false;
	}

	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putUUID("Id", id);
		tag.put("Promises", panelPromises.write());

		tag.put("Links", NBTHelper.writeCompoundList(totalLinks, p -> {
			CompoundTag nbt = NbtUtils.writeBlockPos(p.pos());
			if (p.dimension() != Level.OVERWORLD)
				nbt.putString("Dim", p.dimension()
					.location()
					.toString());
			return nbt;
		}));

		if (owner != null)
			tag.putUUID("Owner", owner);

		tag.putBoolean("Locked", locked);
		return tag;
	}

	public static LogisticsNetwork read(CompoundTag tag) {
		LogisticsNetwork network = new LogisticsNetwork(tag.getUUID("Id"));
		network.panelPromises = RequestPromiseQueue.read(tag.getCompound("Promises"), CreateLogisticsBackport.LOGISTICS::markDirty);

		NBTHelper.iterateCompoundList(tag.getList("Links", Tag.TAG_COMPOUND), nbt -> {
			network.totalLinks.add(GlobalPos.of(nbt.contains("Dim")
				? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("Dim")))
				: Level.OVERWORLD, NbtUtils.readBlockPos(nbt)));
		});

		network.owner = tag.contains("Owner") ? tag.getUUID("Owner") : null;
		network.locked = tag.getBoolean("Locked");

		return network;
	}

}
