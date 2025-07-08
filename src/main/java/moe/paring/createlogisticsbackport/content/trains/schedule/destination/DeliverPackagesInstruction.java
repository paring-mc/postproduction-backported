package moe.paring.createlogisticsbackport.content.trains.schedule.destination;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime.State;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import moe.paring.createlogisticsbackport.accessor.GlobalStationMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.mixin.ScheduleRuntimeAccessor;
import moe.paring.createlogisticsbackport.polyfill.trains.GlobalPackagePort;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DeliverPackagesInstruction extends ScheduleInstruction implements IHaveScheduleInstructionRunner {

	@Override
	public Pair<ItemStack, Component> getSummary() {
		return Pair.of(getSecondLineIcon(), Lang.translateDirect("schedule.instruction.package_delivery"));
	}

	@Override
	public ItemStack getSecondLineIcon() {
		return ExtraBlocks.PACKAGE_POSTBOXES.get(DyeColor.WHITE)
			.asStack();
	}

	@Override
	public List<Component> getTitleAs(String type) {
		return ImmutableList.of(Lang.translate("schedule.instruction.package_delivery.summary")
			.style(ChatFormatting.GOLD)
			.component(),
			Lang.translateDirect("schedule.instruction.package_delivery.summary_1")
				.withStyle(ChatFormatting.GRAY),
			Lang.translateDirect("schedule.instruction.package_delivery.summary_2")
				.withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ResourceLocation getId() {
		return Create.asResource("package_delivery");
	}

	@Override
	public boolean supportsConditions() {
		return true;
	}

	@Override
	@Nullable
	public DiscoveredPath start(ScheduleRuntime runtime) {
		boolean anyMatch = false;
		String firstPackage = null;
		ArrayList<GlobalStation> validStations = new ArrayList<>();
		Train train = ((ScheduleRuntimeAccessor)runtime).getTrain();

		if (!train.hasForwardConductor() && !train.hasBackwardConductor()) {
			train.status.missingConductor();
			startCooldown(runtime);
			return null;
		}

		for (Carriage carriage : train.carriages) {
			IItemHandlerModifiable carriageInventory = carriage.storage.getItems();
			if (carriageInventory == null)
				continue;

			// Export to station
			for (int slot = 0; slot < carriageInventory.getSlots(); slot++) {
				ItemStack stack = carriageInventory.getStackInSlot(slot);
				if (!PackageItem.isPackage(stack))
					continue;
				if (firstPackage == null)
					firstPackage = PackageItem.getAddress(stack);
				for (GlobalStation globalStation : train.graph.getPoints(EdgePointType.STATION)) {
					for (Entry<BlockPos, GlobalPackagePort> port : ((GlobalStationMixinAccessor)globalStation).create_logistics_backport$getConnectedPorts().entrySet()) {
						if (!PackageItem.matchAddress(stack, port.getValue().address))
							continue;
						anyMatch = true;
						validStations.add(globalStation);
						break;
					}
				}
			}
		}

		if (validStations.isEmpty()) {
			if (firstPackage != null) {
				train.status.displayInformation("no_package_target", false, firstPackage);
				startCooldown(runtime);
			} else {
				runtime.state = State.PRE_TRANSIT;
				runtime.currentEntry++;
			}
			return null;
		}

		DiscoveredPath best = train.navigation.findPathTo(validStations, Double.MAX_VALUE);
		if (best == null) {
			if (anyMatch)
				train.status.failedNavigation();
			startCooldown(runtime);
			return null;
		}

		return best;
	}

}
