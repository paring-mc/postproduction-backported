package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.equipment.clipboard.ClipboardBlockItem;
import com.simibubi.create.content.logistics.filter.FilterItem;
import moe.paring.createlogisticsbackport.foundation.recipe.ItemCopyingRecipe.SupportsItemCopying;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FilterItem.class)
public class FilterItemMixin implements SupportsItemCopying {
}
