package moe.paring.createlogisticsbackport.content.logistics;

import com.google.common.cache.Cache;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.clipboard.ClipboardBlockEntity;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.trains.schedule.DestinationSuggestions;
import com.simibubi.create.foundation.utility.IntAttached;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import moe.paring.createlogisticsbackport.polyfill.DestinationSuggestions2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddressEditBoxHelper {

	private static final Cache<BlockPos, WeakReference<ClipboardBlockEntity>> NEARBY_CLIPBOARDS = new TickBasedCache<>(20, false);

	public static void advertiseClipboard(ClipboardBlockEntity blockEntity) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;
		BlockPos blockPos = blockEntity.getBlockPos();
		if (player.distanceToSqr(Vec3.atCenterOf(blockPos)) > 32 * 32)
			return;
		NEARBY_CLIPBOARDS.put(blockPos, new WeakReference<>(blockEntity));
	}

	public static DestinationSuggestions2 createSuggestions(Screen screen, EditBox pInput, boolean anchorToBottom, String localAddress) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		List<IntAttached<String>> options = new ArrayList<>();
		Set<String> alreadyAdded = new HashSet<>();

		DestinationSuggestions2 destinationSuggestions = new DestinationSuggestions2(mc, screen, pInput, mc.font, options,
			anchorToBottom, -72 + pInput.getY() + (anchorToBottom ? 0 : pInput.getHeight()));

		if (player == null)
			return destinationSuggestions;
		
		if (localAddress != null) {
			options.add(IntAttached.with(-1, localAddress));
			alreadyAdded.add(localAddress);
		}

		for (int i = 0; i < Inventory.INVENTORY_SIZE; i++)
			appendAddresses(options, alreadyAdded, player.getInventory()
				.getItem(i));

		for (WeakReference<ClipboardBlockEntity> wr : NEARBY_CLIPBOARDS.asMap()
			.values()) {
			ClipboardBlockEntity cbe = wr.get();
			if (cbe != null)
				appendAddresses(options, alreadyAdded, cbe.dataContainer);
		}

		return destinationSuggestions;
	}

	private static void appendAddresses(List<IntAttached<String>> options, Set<String> alreadyAdded, ItemStack item) {
		if (item == null || !AllBlocks.CLIPBOARD.isIn(item))
			return;

		List<List<ClipboardEntry>> pages = ClipboardEntry.readAll(item);
		pages.forEach(page -> page.forEach(entry -> {
			String string = entry.text.getString();
			if (entry.checked)
				return;
			if (!string.startsWith("#") || string.length() <= 1)
				return;
			String address = string.substring(1);
			if (address.isBlank())
				return;
			String trim = address.trim();
			if (!alreadyAdded.add(trim))
				return;
			options.add(IntAttached.withZero(trim));
		}));
	}

}
