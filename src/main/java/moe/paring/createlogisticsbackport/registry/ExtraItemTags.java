package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Lang;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum ExtraItemTags {
    TABLE_CLOTHS,
    DYED_TABLE_CLOTHS,
    POSTBOXES,
    PACKAGES,
    ;

    public final TagKey<Item> tag;

    ExtraItemTags() {
        tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Create.ID, Lang.asId(name())));
    }

    public boolean matches(ItemStack stack) {
        return stack.is(tag);
    }

    public static void init() {}
}
