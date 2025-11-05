package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import moe.paring.createlogisticsbackport.content.logistics.filter.FilterItem2;
import moe.paring.createlogisticsbackport.content.logistics.filter.PackageFilterItemStack;
import moe.paring.createlogisticsbackport.polyfill.behaviour.filtering.FilteringBehaviour2;
import moe.paring.createlogisticsbackport.registry.ExtraItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FilterItemStack.class)
public class FilterItemStackMixin {
    @Inject(method = "of(Lnet/minecraft/world/item/ItemStack;)Lcom/simibubi/create/content/logistics/filter/FilterItemStack;", at = @At("HEAD"), cancellable = true, remap = false)
    private static void filterItemStack(ItemStack filter, CallbackInfoReturnable<FilterItemStack> cir) {
        if (filter.hasTag()) {
            if (ExtraItems.PACKAGE_FILTER.isIn(filter)) {
                CompoundTag stackTag = filter.getTag();
                stackTag.remove("Enchantments");
                stackTag.remove("AttributeModifiers");
                cir.setReturnValue(new PackageFilterItemStack(filter));
                cir.cancel();
            }
        }
    }

    @Shadow(remap = false)
    private ItemStack filterItemStack;

    @Inject(method = "isFilterItem", at = @At("HEAD"), cancellable = true, remap = false)
    private void isFilterItemPre(CallbackInfoReturnable<Boolean> cir) {
        if (filterItemStack.getItem() instanceof FilterItem2)
            cir.setReturnValue(true);
    }
}
