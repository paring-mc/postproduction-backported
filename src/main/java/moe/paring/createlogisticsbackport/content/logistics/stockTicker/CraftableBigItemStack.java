package moe.paring.createlogisticsbackport.content.logistics.stockTicker;

import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public class CraftableBigItemStack extends BigItemStack {

	public Recipe<?> recipe;

	public CraftableBigItemStack(ItemStack stack, Recipe<?> recipe) {
		super(stack);
		this.recipe = recipe;
	}

	public List<Ingredient> getIngredients() {
		return recipe.getIngredients();
	}

	public int getOutputCount(Level level) {
		return recipe.getResultItem(level.registryAccess())
			.getCount();
	}

}
