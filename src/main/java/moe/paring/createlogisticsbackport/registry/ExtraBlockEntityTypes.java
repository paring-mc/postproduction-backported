package moe.paring.createlogisticsbackport.registry;

import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorInstance;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorRenderer;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelRenderer;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport.FrogportBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport.FrogportRenderer;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport.FrogportVisual;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxRenderer;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerRenderer;
import moe.paring.createlogisticsbackport.content.logistics.packager.repackager.RepackagerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.PackagerLinkBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static moe.paring.createlogisticsbackport.CreateLogisticsBackport.REGISTRATE;

public class ExtraBlockEntityTypes {
    public static final BlockEntityEntry<ChainConveyorBlockEntity> CHAIN_CONVEYOR = REGISTRATE
            .blockEntity("chain_conveyor", ChainConveyorBlockEntity::new)
            .instance(() -> ChainConveyorInstance::new)
            .validBlocks(ExtraBlocks.CHAIN_CONVEYOR)
            .renderer(() -> ChainConveyorRenderer::new)
            .register();

    public static final BlockEntityEntry<PackagerBlockEntity> PACKAGER = REGISTRATE
            .blockEntity("packager", PackagerBlockEntity::new)
            .validBlocks(ExtraBlocks.PACKAGER)
            .renderer(() -> PackagerRenderer::new)
            .register();

    public static final BlockEntityEntry<RepackagerBlockEntity> REPACKAGER = REGISTRATE
            .blockEntity("repackager", RepackagerBlockEntity::new)
            .validBlocks(ExtraBlocks.REPACKAGER)
            .renderer(() -> PackagerRenderer::new)
            .register();

    public static final BlockEntityEntry<FrogportBlockEntity> PACKAGE_FROGPORT = REGISTRATE
            .blockEntity("package_frogport", FrogportBlockEntity::new)
            .instance(() -> FrogportVisual::new, true)
            .validBlocks(ExtraBlocks.PACKAGE_FROGPORT)
            .renderer(() -> FrogportRenderer::new)
            .register();

    public static final BlockEntityEntry<FactoryPanelBlockEntity> FACTORY_PANEL =
            REGISTRATE.blockEntity("factory_panel", FactoryPanelBlockEntity::new)
                    .validBlocks(ExtraBlocks.FACTORY_GAUGE)
                    .renderer(() -> FactoryPanelRenderer::new)
                    .register();

    public static final BlockEntityEntry<PostboxBlockEntity> PACKAGE_POSTBOX = REGISTRATE
            .blockEntity("package_postbox", PostboxBlockEntity::new)
            .validBlocks(ExtraBlocks.PACKAGE_POSTBOXES.toArray())
            .renderer(() -> PostboxRenderer::new)
            .register();

    public static final BlockEntityEntry<TableClothBlockEntity> TABLE_CLOTH =
            REGISTRATE.blockEntity("table_cloth", TableClothBlockEntity::new)
                    .validBlocks(ExtraBlocks.TABLE_CLOTHS.toArray())
                    .validBlock(ExtraBlocks.ANDESITE_TABLE_CLOTH)
                    .validBlock(ExtraBlocks.BRASS_TABLE_CLOTH)
                    .validBlock(ExtraBlocks.COPPER_TABLE_CLOTH)
                    .renderer(() -> TableClothRenderer::new)
                    .register();

    public static final BlockEntityEntry<PackagerLinkBlockEntity> PACKAGER_LINK = REGISTRATE
            .blockEntity("packager_link", PackagerLinkBlockEntity::new)
            .validBlocks(ExtraBlocks.STOCK_LINK)
            .register();

    public static final BlockEntityEntry<StockTickerBlockEntity> STOCK_TICKER = REGISTRATE
            .blockEntity("stock_ticker", StockTickerBlockEntity::new)
            .validBlocks(ExtraBlocks.STOCK_TICKER)
            .register();

    public static final BlockEntityEntry<RedstoneRequesterBlockEntity> REDSTONE_REQUESTER = REGISTRATE
            .blockEntity("redstone_requester", RedstoneRequesterBlockEntity::new)
            .validBlocks(ExtraBlocks.REDSTONE_REQUESTER)
            .register();

    public static void register() {}
}
