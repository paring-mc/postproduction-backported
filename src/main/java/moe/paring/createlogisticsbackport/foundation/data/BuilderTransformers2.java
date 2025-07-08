package moe.paring.createlogisticsbackport.foundation.data;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BuilderTransformers2 {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> palettesIronBlock() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                        .sound(SoundType.NETHERITE_BLOCK)
                        .requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .cubeColumn(c.getName(), p.modLoc("block/" + c.getName()), p.modLoc("block/" + c.getName() + "_top"))))
                .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
                .recipe((c, p) -> p.stonecutting(DataIngredient.tag(Tags.Items.INGOTS_IRON), RecipeCategory.BUILDING_BLOCKS,
                        c::get, 2))
                .simpleItem();
    }
}
