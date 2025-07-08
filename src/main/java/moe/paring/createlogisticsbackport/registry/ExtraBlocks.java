package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.*;
import com.simibubi.create.content.contraptions.behaviour.BellMovementBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.redstone.diodes.AbstractDiodeGenerator;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.content.redstone.diodes.BrassDiodeGenerator;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import moe.paring.createlogisticsbackport.BuilderTransformersExtra;
import moe.paring.createlogisticsbackport.content.decoration.CardboardBlock;
import moe.paring.createlogisticsbackport.content.decoration.CardboardBlockItem;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorBlock;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelBlock;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelBlockItem;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelModel;
import moe.paring.createlogisticsbackport.content.logistics.itemHatch.ItemHatchBlock;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortItem;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport.FrogportBlock;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlock;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlock;
import moe.paring.createlogisticsbackport.content.logistics.packager.repackager.RepackagerBlock;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticallyLinkedBlockItem;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.PackagerLinkBlock;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.PackagerLinkGenerator;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterBlock;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterBlockItem;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlock;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlock;
import moe.paring.createlogisticsbackport.content.redstone.deskBell.DeskBellBlock;
import moe.paring.createlogisticsbackport.content.redstone.displayLink.source.FactoryGaugeDisplaySource;
import moe.paring.createlogisticsbackport.foundation.data.BuilderTransformers2;
import moe.paring.createlogisticsbackport.mixin.BlockLootSubProviderAccessor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;
import static moe.paring.createlogisticsbackport.CreateLogisticsBackport.REGISTRATE;

public class ExtraBlocks {
    static {
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final BlockEntry<ChainConveyorBlock> CHAIN_CONVEYOR =
            REGISTRATE.block("chain_conveyor", ChainConveyorBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.PODZOL))
                    .transform(axeOrPickaxe())
                    .transform(BlockStressDefaults.setImpact(1))
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<PackagerBlock> PACKAGER = REGISTRATE.block("packager", PackagerBlock::new)
            .transform(BuilderTransformersExtra.packager())
            .register();

    public static final BlockEntry<RepackagerBlock> REPACKAGER = REGISTRATE.block("repackager", RepackagerBlock::new)
            .transform(BuilderTransformersExtra.packager())
            .lang("Re-Packager")
            .register();

    public static final BlockEntry<FrogportBlock> PACKAGE_FROGPORT =
            REGISTRATE.block("package_frogport", FrogportBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLUE)
                            .sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .item(PackagePortItem::new)
                    .model(AssetLookup::customItemModel)
                    .build()
                    .register();

    public static final DyedBlockList<PostboxBlock> PACKAGE_POSTBOXES = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_postbox", p -> new PostboxBlock(p, colour))
                .initialProperties(SharedProperties::wooden)
                .properties(p -> p.mapColor(colour))
                .transform(axeOnly())
                .blockstate((c, p) -> {
                    p.horizontalBlock(c.get(), s -> {
                        String suffix = s.getValue(PostboxBlock.OPEN) ? "open" : "closed";
                        return p.models()
                                .withExistingParent(colourName + "_postbox_" + suffix,
                                        p.modLoc("block/package_postbox/block_" + suffix))
                                .texture("0", p.modLoc("block/post_box/post_box_" + colourName))
                                .texture("1", p.modLoc("block/post_box/post_box_" + colourName + "_" + suffix));
                    });
                })
                .tag(ExtraBlockTags.POSTBOXES.tag)
                .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.package_postbox"))
                .item(PackagePortItem::new)
                .recipe((c, p) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get())
                            .define('D', colour.getTag())
                            .define('B', Items.BARREL)
                            .define('A', AllItems.ANDESITE_ALLOY)
                            .pattern("D")
                            .pattern("B")
                            .pattern("A")
                            .unlockedBy("has_barrel", RegistrateRecipeProvider.has(Items.BARREL))
                            .save(p, Create.asResource("crafting/logistics/" + c.getName()));
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get())
                            .requires(colour.getTag())
                            .requires(ExtraItemTags.POSTBOXES.tag)
                            .unlockedBy("has_postbox", RegistrateRecipeProvider.has(ExtraItemTags.POSTBOXES.tag))
                            .save(p, Create.asResource("crafting/logistics/" + c.getName() + "_from_other_postbox"));
                })
                .model((c, p) -> p.withExistingParent(colourName + "_postbox", p.modLoc("block/package_postbox/item"))
                        .texture("0", p.modLoc("block/post_box/post_box_" + colourName))
                        .texture("1", p.modLoc("block/post_box/post_box_" + colourName + "_closed")))
                .tag(ExtraItemTags.POSTBOXES.tag)
                .build()
                .register();
    });

    public static final BlockEntry<Block> WEATHERED_IRON_BLOCK = REGISTRATE.block("weathered_iron_block", Block::new)
            .transform(BuilderTransformers2.palettesIronBlock())
            .lang("Block of Weathered Iron")
            .register();

    public static final BlockEntry<PackagerLinkBlock> STOCK_LINK =
            REGISTRATE.block("stock_link", PackagerLinkBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLUE)
                            .sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate(new PackagerLinkGenerator()::generate)
                    .item(LogisticallyLinkedBlockItem::new)
                    .transform(customItemModel("_", "block_vertical"))
                    .register();

    public static final BlockEntry<StockTickerBlock> STOCK_TICKER =
            REGISTRATE.block("stock_ticker", StockTickerBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.GLASS))
                    .transform(axeOrPickaxe())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> p.horizontalBlock(c.get(), AssetLookup.standardModel(c, p)))
                    .item(LogisticallyLinkedBlockItem::new)
                    .build()
                    .register();

    public static final BlockEntry<RedstoneRequesterBlock> REDSTONE_REQUESTER =
            REGISTRATE.block("redstone_requester", RedstoneRequesterBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.WOOD))
                    .properties(p -> p.noOcclusion())
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, AssetLookup.forPowered(c, p)))
                    .item(RedstoneRequesterBlockItem::new)
                    .transform(customItemModel("_", "block"))
                    .register();

    public static final BlockEntry<FactoryPanelBlock> FACTORY_GAUGE =
            REGISTRATE.block("factory_gauge", FactoryPanelBlock::new)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.forceSolidOn())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
                    .onRegister(CreateRegistrate.blockModel(() -> FactoryPanelModel::new))
                    .onRegister(assignDataBehaviour(new FactoryGaugeDisplaySource(), "gauge_status"))
                    .item(FactoryPanelBlockItem::new)
                    .model(AssetLookup::customItemModel)
                    .build()
                    .register();

    public static final DyedBlockList<TableClothBlock> TABLE_CLOTHS = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_table_cloth", p -> new TableClothBlock(p, colour))
                .transform(BuilderTransformersExtra.tableCloth(colourName, () -> Blocks.BLACK_CARPET, true))
                .properties(p -> p.mapColor(colour))
                .recipe((c, p) -> {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
                            .requires(DyeHelper.getWoolOfDye(colour))
                            .requires(AllItems.ANDESITE_ALLOY)
                            .unlockedBy("has_wool", RegistrateRecipeProvider.has(ItemTags.WOOL))
                            .save(p, Create.asResource("crafting/logistics/" + c.getName()));
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get())
                            .requires(colour.getTag())
                            .requires(ExtraItemTags.DYED_TABLE_CLOTHS.tag)
                            .unlockedBy("has_postbox", RegistrateRecipeProvider.has(ExtraItemTags.DYED_TABLE_CLOTHS.tag))
                            .save(p, Create.asResource("crafting/logistics/" + c.getName() + "_from_other_table_cloth"));
                })
                .register();
    });

    public static final BlockEntry<TableClothBlock> ANDESITE_TABLE_CLOTH =
            REGISTRATE.block("andesite_table_cloth", p -> new TableClothBlock(p, "andesite"))
                    .transform(BuilderTransformersExtra.tableCloth("andesite", SharedProperties::stone, false))
                    .properties(p -> p.mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(AllItems.ANDESITE_ALLOY.get()),
                            RecipeCategory.DECORATIONS, c::get, 2))
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<TableClothBlock> BRASS_TABLE_CLOTH =
            REGISTRATE.block("brass_table_cloth", p -> new TableClothBlock(p, "brass"))
                    .transform(BuilderTransformersExtra.tableCloth("brass", SharedProperties::softMetal, false))
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW)
                            .requiresCorrectToolForDrops())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")),
                            RecipeCategory.DECORATIONS, c::get, 2))
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<TableClothBlock> COPPER_TABLE_CLOTH =
            REGISTRATE.block("copper_table_cloth", p -> new TableClothBlock(p, "copper"))
                    .transform(BuilderTransformersExtra.tableCloth("copper", SharedProperties::copperMetal, false))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")),
                            RecipeCategory.DECORATIONS, c::get, 2))
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<CardboardBlock> CARDBOARD_BLOCK =
            REGISTRATE.block("cardboard_block", CardboardBlock::new)
                    .initialProperties(() -> Blocks.MUSHROOM_STEM)
                    .properties(p -> p.mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.CHISELED_BOOKSHELF)
                            .ignitedByLava())
                    .transform(axeOnly())
                    .blockstate(BlockStateGen.horizontalAxisBlockProvider(false))
                    .tag(Tags.Blocks.STORAGE_BLOCKS)
                    .tag(AllTags.forgeBlockTag("storage_blocks/cardboard"))
                    .item(CardboardBlockItem::new)
                    .tag(AllTags.forgeItemTag("storage_blocks/cardboard"))
                    .tag(Tags.Items.STORAGE_BLOCKS)
                    .build()
                    .lang("Block of Cardboard")
                    .register();

    public static final BlockEntry<ItemHatchBlock> ITEM_HATCH = REGISTRATE.block("item_hatch", ItemHatchBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLUE)
                    .sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> p.horizontalBlock(c.get(),
                    s -> AssetLookup.partialBaseModel(c, p, s.getValue(ItemHatchBlock.OPEN) ? "open" : "closed")))
            .item()
            .transform(customItemModel("_", "block_closed"))
            .register();

    public static final BlockEntry<BrassDiodeBlock> PULSE_TIMER = REGISTRATE.block("pulse_timer", BrassDiodeBlock::new)
            .initialProperties(() -> Blocks.REPEATER)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(new BrassDiodeGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .model(AbstractDiodeGenerator::diodeItemModel)
            .build()
            .register();

    public static final BlockEntry<CardboardBlock> BOUND_CARDBOARD_BLOCK =
            REGISTRATE.block("bound_cardboard_block", CardboardBlock::new)
                    .initialProperties(() -> Blocks.MUSHROOM_STEM)
                    .properties(p -> p.mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.CHISELED_BOOKSHELF)
                            .ignitedByLava())
                    .transform(axeOnly())
                    .blockstate(BlockStateGen.horizontalAxisBlockProvider(false))
                    .loot((r, b) -> r.add(b, LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(b)
                                            .when(BlockLootSubProviderAccessor.create$HAS_SILK_TOUCH())
                                            .otherwise(r.applyExplosionCondition(b, LootItem.lootTableItem(Items.STRING)))))
                            .withPool(r.applyExplosionCondition(b, LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(ExtraBlocks.CARDBOARD_BLOCK.asItem()))
                                    .when(BlockLootSubProviderAccessor.create$HAS_SILK_TOUCH().invert())))))
                    .item(CardboardBlockItem::new)
                    .build()
                    .lang("Bound Block of Cardboard")
                    .register();

    public static final BlockEntry<DeskBellBlock> DESK_BELL = REGISTRATE.block("desk_bell", DeskBellBlock::new)
            .properties(p -> p.mapColor(MapColor.SAND))
            .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.forPowered(c, p)))
            .item()
            .transform(customItemModel("_", "block"))
            .onRegister(movementBehaviour(new BellMovementBehaviour()))
            .register();

    public static void register() {
    }
}
