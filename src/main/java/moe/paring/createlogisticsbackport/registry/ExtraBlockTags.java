package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.Create;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public enum ExtraBlockTags {
    TABLE_CLOTHS,
    POSTBOXES,
    SINGLE_BLOCK_INVENTORIES,
    ;

    public final TagKey<Block> tag;

    ExtraBlockTags() {
        this(CreateLogisticsBackport.MODID);
    }

    ;

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

    public boolean matches(BlockState state) {
        return state.is(tag);
    }


    public static void init() {
    }
}
