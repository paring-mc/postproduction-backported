package moe.paring.createlogisticsbackport.content.logistics.redstoneRequester;

import com.simibubi.create.foundation.utility.Lang;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticallyLinkedBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class RedstoneRequesterBlockItem extends LogisticallyLinkedBlockItem {

	public RedstoneRequesterBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		if (!isTuned(pStack))
			return;

		if (!pStack.getTag()
			.contains("EncodedRequest", Tag.TAG_COMPOUND)) {
			super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
			return;
		}

		Lang.translate("logistically_linked.tooltip")
			.style(ChatFormatting.GOLD)
			.addTo(pTooltip);
		RedstoneRequesterBlock.appendRequesterTooltip(pStack, pTooltip);
	}

}
