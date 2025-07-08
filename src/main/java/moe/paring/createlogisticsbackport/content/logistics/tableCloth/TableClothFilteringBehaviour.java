package moe.paring.createlogisticsbackport.content.logistics.tableCloth;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueSettingsBoard2;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueSettingsFormatter2;
import moe.paring.createlogisticsbackport.polyfill.behaviour.filtering.FilteringBehaviour2;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public class TableClothFilteringBehaviour extends FilteringBehaviour2 {

	public TableClothFilteringBehaviour(TableClothBlockEntity be) {
		super(be, new TableClothFilterSlot(be));
		withPredicate(is -> !(is.getItem() instanceof FilterItem) && !(is.getItem() instanceof ShoppingListItem));
		count = 1;
	}

	@Override
	public void onShortInteract(Player player, InteractionHand hand, Direction side, BlockHitResult hitResult) {
		super.onShortInteract(player, hand, side, hitResult);
	}

	@Override
	public float getRenderDistance() {
		return 32;
	}

	private TableClothBlockEntity dbe() {
		return (TableClothBlockEntity) blockEntity;
	}

	@Override
	public boolean mayInteract(Player player) {
		return dbe().owner == null || player.getUUID()
			.equals(dbe().owner);
	}

	@Override
	public boolean isSafeNBT() {
		return false;
	}

	@Override
	public MutableComponent getLabel() {
		return Lang.translateDirect("table_cloth.price_per_order");
	}

	public boolean isCountVisible() {
		return !filter.isEmpty();
	}

	@Override
	public boolean setFilter(ItemStack stack) {
		int before = count;
		boolean result = super.setFilter(stack);
		count = before;
		return result;
	}

	@Override
	public void setValueSettings(Player player, ValueSettings2 settings, boolean ctrlDown) {
		if (getValueSettings().equals(settings))
			return;
		count = Math.max(1, settings.value());
		blockEntity.setChanged();
		blockEntity.sendData();
		playFeedbackSound(this);
	}

	@Override
	public ValueSettingsBoard2 createBoard(Player player, BlockHitResult hitResult) {
		return new ValueSettingsBoard2(getLabel(), 100, 10, Lang.translatedOptions("table_cloth", "amount"),
			new ValueSettingsFormatter2(this::formatValue));
	}

	public MutableComponent formatValue(ValueSettings2 value) {
		return Component.literal(String.valueOf(Math.max(1, value.value())));
	}

	@Override
	public MutableComponent getCountLabelForValueBox() {
		return Component.literal(isCountVisible() ? String.valueOf(count) : "");
	}

	@Override
	public boolean isActive() {
		return dbe().isShop();
	}

}
