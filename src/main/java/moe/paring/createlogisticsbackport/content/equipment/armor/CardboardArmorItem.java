package moe.paring.createlogisticsbackport.content.equipment.armor;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import moe.paring.createlogisticsbackport.registry.ExtraArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class CardboardArmorItem extends BaseArmorItem {

	public CardboardArmorItem(Type type, Properties properties) {
		super(ExtraArmorMaterials.CARDBOARD, type, properties, Create.asResource("cardboard"));
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return 1000;
	}

}
