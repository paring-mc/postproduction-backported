package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.trains.schedule.ScheduleItem;
import moe.paring.createlogisticsbackport.foundation.recipe.ItemCopyingRecipe.SupportsItemCopying;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScheduleItem.class)
public class ScheduleMixin implements SupportsItemCopying {
}
