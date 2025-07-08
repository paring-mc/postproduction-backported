package moe.paring.createlogisticsbackport;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageStyles;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerGenerator;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockItem;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothModel;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import moe.paring.createlogisticsbackport.registry.ExtraBlockTags;
import moe.paring.createlogisticsbackport.registry.ExtraItemTags;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;

import static com.simibubi.create.Create.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BuilderTransformersExtra {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> packager() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion())
                .properties(p -> p.isRedstoneConductor(($1, $2, $3) -> false))
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLUE)
                        .sound(SoundType.NETHERITE_BLOCK))
                .transform(pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(new PackagerGenerator()::generate)
                .item()
                .model(AssetLookup::customItemModel)
                .build();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> tableCloth(String name,
                                                                                           NonNullSupplier<? extends Block> initialProps, boolean dyed) {
        return b -> {
            ItemBuilder<TableClothBlockItem, BlockBuilder<B, P>> item = b.initialProperties(initialProps)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                            .withExistingParent(name + "_table_cloth", p.modLoc("block/table_cloth/block"))
                            .texture("0", p.modLoc("block/table_cloth/" + name))))
                    .onRegister(CreateRegistrate.blockModel(() -> TableClothModel::new))
                    .tag(ExtraBlockTags.TABLE_CLOTHS.tag)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.table_cloth"))
                    .item(TableClothBlockItem::new);

            if (dyed)
                item.tag(ExtraItemTags.DYED_TABLE_CLOTHS.tag);

            return item.model((c, p) -> p.withExistingParent(name + "_table_cloth", p.modLoc("block/table_cloth/item"))
                            .texture("0", p.modLoc("block/table_cloth/" + name)))
                    .tag(ExtraItemTags.TABLE_CLOTHS.tag)
                    .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get())
                            .requires(c.get())
                            .unlockedBy("has_" + c.getName(), RegistrateRecipeProvider.has(c.get()))
                            .save(p, Create.asResource("crafting/logistics/" + c.getName() + "_clear")))
                    .build();
        };
    }

    public static ItemBuilder<PackageItem, CreateRegistrate> packageItem(PackageStyles.PackageStyle style) {
        String size = "_" + style.width() + "x" + style.height();
        return REGISTRATE.item(style.getItemId()
                        .getPath(), p -> new PackageItem(p, style))
                .properties(p -> p.stacksTo(1))
                .tag(ExtraItemTags.PACKAGES.tag)
                .model((c, p) -> {
                    if (style.rare())
                        p.withExistingParent(c.getName(), p.modLoc("item/package/custom" + size))
                                .texture("2", p.modLoc("item/package/" + style.type()));
                    else
                        p.withExistingParent(c.getName(), p.modLoc("item/package/" + style.type() + size));
                })
                .lang((style.rare() ? "Rare"
                        : style.type()
                        .substring(0, 1)
                        .toUpperCase(Locale.ROOT)
                        + style.type()
                        .substring(1))
                        + " Package");
    }


}
