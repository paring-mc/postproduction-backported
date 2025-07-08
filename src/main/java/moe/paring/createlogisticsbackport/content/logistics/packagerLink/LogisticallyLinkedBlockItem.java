package moe.paring.createlogisticsbackport.content.logistics.packagerLink;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LogisticallyLinkedBlockItem extends BlockItem {

	public LogisticallyLinkedBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return isTuned(pStack);
	}

	public static boolean isTuned(ItemStack pStack) {
		return pStack.hasTag() && pStack.getTag()
			.contains(BLOCK_ENTITY_TAG);
	}
	
	@Nullable
	public static UUID networkFromStack(ItemStack pStack) {
		if (!isTuned(pStack))
			return null;
		CompoundTag tag = pStack.getTag()
			.getCompound(BLOCK_ENTITY_TAG);
		if (!tag.hasUUID("Freq"))
			return null;
		return tag.getUUID("Freq");
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		if (!isTuned(pStack))
			return;

		CompoundTag tag = pStack.getTag()
			.getCompound(BLOCK_ENTITY_TAG);
		if (!tag.hasUUID("Freq"))
			return;

		Lang.translate("logistically_linked.tooltip")
			.style(ChatFormatting.GOLD)
			.addTo(pTooltip);

		Lang.translate("logistically_linked.tooltip_clear")
			.style(ChatFormatting.GRAY)
			.addTo(pTooltip);
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		ItemStack stack = pContext.getItemInHand();
		BlockPos pos = pContext.getClickedPos();
		Level level = pContext.getLevel();
		Player player = pContext.getPlayer();

		if (player == null)
			return InteractionResult.FAIL;
		if (player.isShiftKeyDown())
			return super.useOn(pContext);

		LogisticallyLinkedBehaviour link = BlockEntityBehaviour.get(level, pos, LogisticallyLinkedBehaviour.TYPE);
		boolean tuned = isTuned(stack);

		if (link != null) {
			if (level.isClientSide)
				return InteractionResult.SUCCESS;
			if (!link.mayInteractMessage(player))
				return InteractionResult.SUCCESS;

			assignFrequency(stack, player, link.freqId);
			return InteractionResult.SUCCESS;
		}

		InteractionResult useOn = super.useOn(pContext);
		if (level.isClientSide || useOn == InteractionResult.FAIL)
			return useOn;

		player.displayClientMessage(tuned ? Lang.translateDirect("logistically_linked.connected")
			: Lang.translateDirect("logistically_linked.new_network_started"), true);
		return useOn;
	}

	public static void assignFrequency(ItemStack stack, Player player, UUID frequency) {
		CompoundTag stackTag = stack.getOrCreateTag();
		CompoundTag teTag = new CompoundTag();
		teTag.putUUID("Freq", frequency);
		stackTag.put(BLOCK_ENTITY_TAG, teTag);

		player.displayClientMessage(Lang.translateDirect("logistically_linked.tuned"), true);
		stack.setTag(stackTag);
	}

}
