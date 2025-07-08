package moe.paring.createlogisticsbackport.content.logistics.filter;

import com.simibubi.create.content.logistics.filter.FilterItemStack;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class PackageFilterItemStack extends FilterItemStack {
    public String filterString;

    protected PackageFilterItemStack(ItemStack filter) {
        super(filter);
        filterString = filter.getOrCreateTag()
                .getString("Address");
    }

    @Override
    public boolean test(Level world, ItemStack stack, boolean matchNBT) {
        return (filterString.isBlank() && super.test(world, stack, matchNBT))
                || PackageItem.isPackage(stack) && PackageItem.matchAddress(stack, filterString);
    }

    @Override
    public boolean test(Level world, FluidStack stack, boolean matchNBT) {
        return false;
    }

}
