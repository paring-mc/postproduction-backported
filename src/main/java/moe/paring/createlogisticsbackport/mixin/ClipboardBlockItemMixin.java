package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.equipment.clipboard.ClipboardBlockItem;
import moe.paring.createlogisticsbackport.foundation.recipe.ItemCopyingRecipe.SupportsItemCopying;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClipboardBlockItem.class)
public class ClipboardBlockItemMixin implements SupportsItemCopying {
}
