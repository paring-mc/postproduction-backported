package moe.paring.createlogisticsbackport.foundation.recipe;

import com.simibubi.create.foundation.utility.IntAttached;
import moe.paring.createlogisticsbackport.registry.ExtraRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ItemCopyingRecipe extends CustomRecipe {

    public static interface SupportsItemCopying {

        public default ItemStack createCopy(ItemStack original, int count) {
            ItemStack copyWithCount = original.copyWithCount(count);
            copyWithCount.removeTagKey("Enchantments");
            return copyWithCount;
        }

        public default boolean canCopyFromItem(ItemStack item) {
            return item.hasTag();
        }

        public default boolean canCopyToItem(ItemStack item) {
            return !item.hasTag();
        }

    }

    public ItemCopyingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        return copyCheck(inv) != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        IntAttached<ItemStack> copyCheck = copyCheck(container);
        if (copyCheck == null)
            return ItemStack.EMPTY;

        ItemStack itemToCopy = copyCheck.getValue();
        if (!(itemToCopy.getItem() instanceof SupportsItemCopying sic))
            return ItemStack.EMPTY;

        return sic.createCopy(itemToCopy, copyCheck.getFirst() + 1);
    }

    @Nullable
    private IntAttached<ItemStack> copyCheck(CraftingContainer inv) {
        ItemStack itemToCopy = ItemStack.EMPTY;
        int copyTargets = 0;

        for (int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemInSlot = inv.getItem(j);
            if (itemInSlot.isEmpty())
                continue;
            if (!itemToCopy.isEmpty() && itemToCopy.getItem() != itemInSlot.getItem())
                return null;
            if (!(itemInSlot.getItem() instanceof SupportsItemCopying sic))
                continue;

            if (sic.canCopyFromItem(itemInSlot)) {
                if (!itemToCopy.isEmpty())
                    return null;
                itemToCopy = itemInSlot;
                continue;
            }

            if (sic.canCopyToItem(itemInSlot))
                copyTargets++;
        }

        if (itemToCopy.isEmpty() || copyTargets == 0)
            return null;

        return IntAttached.with(copyTargets, itemToCopy);
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        return nonnulllist;
    }

    public RecipeSerializer<?> getSerializer() {
        return ExtraRecipeTypes.ITEM_COPYING.getSerializer();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }
}

