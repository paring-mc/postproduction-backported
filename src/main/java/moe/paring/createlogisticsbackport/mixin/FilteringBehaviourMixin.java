package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import moe.paring.createlogisticsbackport.content.logistics.filter.FilterItem2;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FilteringBehaviour.class)
public class FilteringBehaviourMixin {
    @Redirect(method = "onShortInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private Item getItem(ItemStack instance) {
        var result = instance.getItem();
        if (result instanceof FilterItem2) return AllItems.FILTER.get();
        return result;
    }
}
