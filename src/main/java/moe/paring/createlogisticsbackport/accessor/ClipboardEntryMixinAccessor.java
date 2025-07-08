package moe.paring.createlogisticsbackport.accessor;

import net.minecraft.world.item.ItemStack;

//@Mixin(ClipboardEntry.class)
public interface ClipboardEntryMixinAccessor  {
//    @Accessor("create_logistics_backport$itemAmount")
    int getCreate_logistics_backport$itemAmount();

//    @Accessor("create_logistics_backport$itemAmount")
    void setCreate_logistics_backport$itemAmount(int value);

    void create_logistics_backport$displayItem(ItemStack icon, int amount);
}
