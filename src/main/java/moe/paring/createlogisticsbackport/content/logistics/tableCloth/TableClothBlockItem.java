package moe.paring.createlogisticsbackport.content.logistics.tableCloth;

import com.simibubi.create.foundation.utility.Lang;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class TableClothBlockItem extends BlockItem {

	public TableClothBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		CompoundTag tag = pStack.getTag();
		return tag != null && tag.contains("TargetOffset");
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		if (!isFoil(pStack))
			return;

		Lang.translate("table_cloth.shop_configured")
			.style(ChatFormatting.GOLD)
			.addTo(pTooltip);

		RedstoneRequesterBlock.appendRequesterTooltip(pStack, pTooltip);
	}

}
