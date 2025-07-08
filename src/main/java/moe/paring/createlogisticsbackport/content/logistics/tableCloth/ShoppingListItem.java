package moe.paring.createlogisticsbackport.content.logistics.tableCloth;

import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import moe.paring.createlogisticsbackport.content.logistics.packager.InventorySummary;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ShoppingListItem extends Item {

	public static record ShoppingList(List<IntAttached<BlockPos>> purchases, UUID shopOwner, UUID shopNetwork) {

		public static ShoppingList load(CompoundTag tag) {
			return new ShoppingList(
				NBTHelper.readCompoundList(tag.getList("Purchases", Tag.TAG_COMPOUND),
					c -> IntAttached.read(c, NbtUtils::readBlockPos)),
				tag.getUUID("ShopOwner"), tag.getUUID("ShopNetwork"));
		}

		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			tag.put("Purchases",
				NBTHelper.writeCompoundList(purchases, ia -> ia.serializeNBT(NbtUtils::writeBlockPos)));
			tag.putUUID("ShopOwner", shopOwner);
			tag.putUUID("ShopNetwork", shopNetwork);
			return tag;
		}

		// Y value of clothPos is pixel perfect (x16)
		public void addPurchases(BlockPos clothPos, int amount) {
			for (IntAttached<BlockPos> entry : purchases) {
				if (clothPos.equals(entry.getValue())) {
					entry.setFirst(entry.getFirst() + amount);
					return;
				}
			}
			purchases.add(IntAttached.with(amount, clothPos));
		}

		public int getPurchases(BlockPos clothPos) {
			for (IntAttached<BlockPos> entry : purchases)
				if (clothPos.equals(entry.getValue()))
					return entry.getFirst();
			return 0;
		}

		public Couple<InventorySummary> bakeEntries(LevelAccessor level, @Nullable BlockPos clothPosToIgnore) {
			InventorySummary input = new InventorySummary();
			InventorySummary output = new InventorySummary();

			for (IntAttached<BlockPos> entry : purchases) {
				if (clothPosToIgnore != null && clothPosToIgnore.equals(entry.getValue()))
					continue;
				if (!(level.getBlockEntity(entry.getValue()) instanceof TableClothBlockEntity dcbe))
					continue;
				input.add(dcbe.getPaymentItem(), dcbe.getPaymentAmount() * entry.getFirst());
				for (BigItemStack stackEntry : dcbe.requestData.encodedRequest.stacks())
					output.add(stackEntry.stack, stackEntry.count * entry.getFirst());
			}

			return Couple.create(output, input);
		}
	}

	public ShoppingListItem(Properties pProperties) {
		super(pProperties);
	}

	public static ShoppingList getList(ItemStack stack) {
		if (!stack.hasTag() || !stack.getTag()
			.contains("ShoppingList"))
			return null;
		return ShoppingList.load(stack.getTag()
			.getCompound("ShoppingList"));
	}

	public static ItemStack saveList(ItemStack stack, ShoppingList list, String address) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.put("ShoppingList", list.save());
		tag.putString("Address", address);
		return stack;
	}

	public static String getAddress(ItemStack stack) {
		if (!stack.hasTag())
			return "";
		return stack.getTag()
			.getString("Address");
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
								TooltipFlag pIsAdvanced) {
		ShoppingList list = getList(pStack);

		if (list != null) {
			Couple<InventorySummary> lists = list.bakeEntries(pLevel, null);

			if (lists != null) {
				for (InventorySummary items : lists) {
					List<BigItemStack> entries = items.getStacksByCount();
					boolean cost = items == lists.getSecond();

					if (cost)
						pTooltipComponents.add(Component.empty());

					if (entries.size() == 1) {
						BigItemStack entry = entries.get(0);
						(cost ? Lang.translate("table_cloth.total_cost") : Lang.text(""))
							.style(ChatFormatting.GOLD)
							.add(Lang.builder()
								.add(entry.stack.getHoverName())
								.text(" x")
								.text(String.valueOf(entry.count))
								.style(cost ? ChatFormatting.YELLOW : ChatFormatting.GRAY))
							.addTo(pTooltipComponents);

					} else {
						if (cost)
							Lang.translate("table_cloth.total_cost")
								.style(ChatFormatting.GOLD)
								.addTo(pTooltipComponents);
						for (BigItemStack entry : entries) {
							Lang.builder()
								.add(entry.stack.getHoverName())
								.text(" x")
								.text(String.valueOf(entry.count))
								.style(cost ? ChatFormatting.YELLOW : ChatFormatting.GRAY)
								.addTo(pTooltipComponents);
						}
					}
				}
			}
		}

		Lang.translate("table_cloth.hand_to_shop_keeper")
			.style(ChatFormatting.GRAY)
			.addTo(pTooltipComponents);

		Lang.translate("table_cloth.sneak_click_discard")
			.style(ChatFormatting.DARK_GRAY)
			.addTo(pTooltipComponents);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		if (pUsedHand == InteractionHand.OFF_HAND || pPlayer == null || !pPlayer.isShiftKeyDown())
			return new InteractionResultHolder<>(InteractionResult.PASS, pPlayer.getItemInHand(pUsedHand));

		Lang.translate("table_cloth.shopping_list_discarded")
			.sendStatus(pPlayer);
		pPlayer.playSound(SoundEvents.BOOK_PAGE_TURN);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, ItemStack.EMPTY);
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		InteractionHand pUsedHand = pContext.getHand();
		Player pPlayer = pContext.getPlayer();
		if (pUsedHand == InteractionHand.OFF_HAND || pPlayer == null || !pPlayer.isShiftKeyDown())
			return InteractionResult.PASS;
		pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);

		Lang.translate("table_cloth.shopping_list_discarded")
			.sendStatus(pPlayer);
		pPlayer.playSound(SoundEvents.BOOK_PAGE_TURN);
		return InteractionResult.SUCCESS;
	}

}
