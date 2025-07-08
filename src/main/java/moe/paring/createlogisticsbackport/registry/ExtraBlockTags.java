package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.Create;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public enum ExtraBlockTags {
    TABLE_CLOTHS,
    POSTBOXES
    ;

    public final TagKey<Block> tag;

    ExtraBlockTags() { this(CreateLogisticsBackport.MODID); };

    ExtraBlockTags(String id) {
        tag = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Create.ID, id));
    }

    @SuppressWarnings("deprecation")
    public boolean matches(Block block) {
        return block.builtInRegistryHolder()
                .is(tag);
    }

    public boolean matches(ItemStack stack) {
        return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
    }

    public static void init() {}
}
