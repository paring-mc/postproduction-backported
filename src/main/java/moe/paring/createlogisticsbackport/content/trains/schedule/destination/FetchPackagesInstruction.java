package moe.paring.createlogisticsbackport.content.trains.schedule.destination;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime.State;
import com.simibubi.create.content.trains.schedule.destination.TextScheduleInstruction;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import moe.paring.createlogisticsbackport.accessor.GlobalStationMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageStyles;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlockEntity;
import moe.paring.createlogisticsbackport.mixin.ScheduleRuntimeAccessor;
import moe.paring.createlogisticsbackport.polyfill.trains.GlobalPackagePort;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

public class FetchPackagesInstruction extends TextScheduleInstruction implements IHaveScheduleInstructionRunner {

	@Override
	public Pair<ItemStack, Component> getSummary() {
		return Pair.of(getSecondLineIcon(), Lang.translateDirect("schedule.instruction.package_retrieval"));
	}

	@Override
	public List<Component> getTitleAs(String type) {
		return ImmutableList.of(Lang.translate("schedule.instruction.package_retrieval.summary")
			.style(ChatFormatting.GOLD)
			.component(), Lang.translateDirect("generic.in_quotes", Components.literal(getLabelText())),
			Lang.translateDirect("schedule.instruction.package_retrieval.summary_1")
				.withStyle(ChatFormatting.GRAY),
			Lang.translateDirect("schedule.instruction.package_retrieval.summary_2")
				.withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ItemStack getSecondLineIcon() {
		return PackageStyles.getDefaultBox();
	}

	public String getFilter() {
		return getLabelText();
	}

	public String getFilterForRegex() {
		String filter = getFilter();
		if (filter.isBlank())
			return filter;
		return "\\Q" + filter.replace("*", "\\E.*\\Q") + "\\E";
	}

	@Override
	public List<Component> getSecondLineTooltip(int slot) {
		return ImmutableList.of(Lang.translateDirect("schedule.instruction.address_filter_edit_box"),
			Lang.translateDirect("schedule.instruction.address_filter_edit_box_1")
				.withStyle(ChatFormatting.GRAY),
			Lang.translateDirect("schedule.instruction.address_filter_edit_box_2")
				.withStyle(ChatFormatting.DARK_GRAY),
			Lang.translateDirect("schedule.instruction.address_filter_edit_box_3")
				.withStyle(ChatFormatting.DARK_GRAY));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void modifyEditBox(EditBox box) {
		box.setFilter(s -> StringUtils.countMatches(s, '*') <= 3);
	}

	@Override
	public ResourceLocation getId() {
		return Create.asResource("package_retrieval");
	}

	@Override
	public boolean supportsConditions() {
		return false;
	}

	@Override
	public DiscoveredPath start(ScheduleRuntime runtime) {
		String regex = getFilterForRegex();
		boolean anyMatch = false;
		ArrayList<GlobalStation> validStations = new ArrayList<>();
		Train train = ((ScheduleRuntimeAccessor)runtime).getTrain();

		if (!train.hasForwardConductor() && !train.hasBackwardConductor()) {
			train.status.missingConductor();
			startCooldown(runtime);
			return null;
		}

		Level level = null;
		for (Carriage carriage : train.carriages) {
			if (level == null) {
				CarriageContraptionEntity entity = carriage.anyAvailableEntity();
				if (entity != null && entity.level() instanceof ServerLevel sl)
					level = sl;
			}
		}

		for (GlobalStation globalStation : train.graph.getPoints(EdgePointType.STATION)) {
			for (Entry<BlockPos, GlobalPackagePort> entry : ((GlobalStationMixinAccessor)globalStation).create_logistics_backport$getConnectedPorts().entrySet()) {
				GlobalPackagePort port = entry.getValue();
				BlockPos pos = entry.getKey();

				IItemHandlerModifiable postboxInventory = port.offlineBuffer;
				if (level != null && level.isLoaded(pos)
					&& level.getBlockEntity(pos) instanceof PostboxBlockEntity ppbe) {
					postboxInventory = ppbe.inventory;
				}

				for (int slot = 0; slot < postboxInventory.getSlots(); slot++) {
					ItemStack stack = postboxInventory.getStackInSlot(slot);
					if (!PackageItem.isPackage(stack))
						continue;
					if (PackageItem.matchAddress(stack, port.address))
						continue;
					try {
						if (!PackageItem.getAddress(stack)
							.matches(regex))
							continue;
						anyMatch = true;
						validStations.add(globalStation);
					} catch (PatternSyntaxException ignored) {
					}
				}
			}
		}

		if (validStations.isEmpty()) {
			startCooldown(runtime);
			runtime.state = State.PRE_TRANSIT;
			runtime.currentEntry++;
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
