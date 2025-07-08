package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsPacket;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorConnectionPacket;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainPackageInteractionPacket;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ClientboundChainConveyorRidingPacket;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ServerboundChainConveyorRidingPacket;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageDestroyPacket;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelConfigurationPacket;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelConnectionPacket;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelEffectPacket;
import moe.paring.createlogisticsbackport.content.logistics.filter.FilterScreenPacket2;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortConfigurationPacket;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortPlacementPacket;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.WiFiEffectPacket;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterConfigurationPacket;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterEffectPacket;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.*;
import moe.paring.createlogisticsbackport.content.tool.KnockbackPacket;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueSettingsPacket2;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public enum ExtraPackets {
    PLACE_PACKAGE_PORT(PackagePortPlacementPacket.class, PackagePortPlacementPacket::new, PLAY_TO_SERVER),
    CLIENTBOUND_CHAIN_CONVEYOR(ClientboundChainConveyorRidingPacket.class, ClientboundChainConveyorRidingPacket::new, PLAY_TO_CLIENT),
    PACKAGE_DESTROYED(PackageDestroyPacket.class, PackageDestroyPacket::new, PLAY_TO_CLIENT),
    FACTORY_PANEL_EFFECT(FactoryPanelEffectPacket.class, FactoryPanelEffectPacket::new, PLAY_TO_CLIENT),
    PACKAGER_LINK_EFFECT(WiFiEffectPacket.class, WiFiEffectPacket::new, PLAY_TO_CLIENT),
    PACKAGE_PORT_CONFIGURATION(PackagePortConfigurationPacket.class, PackagePortConfigurationPacket::new, PLAY_TO_SERVER),
    REDSTONE_REQUESTER_EFFECT(RedstoneRequesterEffectPacket.class, RedstoneRequesterEffectPacket::new, PLAY_TO_CLIENT),
    LOGISTICS_STOCK_RESPONSE(LogisticalStockResponsePacket.class, LogisticalStockResponsePacket::new, PLAY_TO_CLIENT),

    LOGISTICS_STOCK_REQUEST(LogisticalStockRequestPacket.class, LogisticalStockRequestPacket::new, PLAY_TO_SERVER),
    LOGISTICS_PACKAGE_REQUEST(PackageOrderRequestPacket.class, PackageOrderRequestPacket::new, PLAY_TO_SERVER),
    CHAIN_CONVEYOR_CONNECT(ChainConveyorConnectionPacket.class, ChainConveyorConnectionPacket::new, PLAY_TO_SERVER),
    CHAIN_CONVEYOR_RIDING(ServerboundChainConveyorRidingPacket.class, ServerboundChainConveyorRidingPacket::new, PLAY_TO_SERVER),
    CHAIN_PACKAGE_INTERACTION(ChainPackageInteractionPacket.class, ChainPackageInteractionPacket::new, PLAY_TO_SERVER),

    CONNECT_FACTORY_PANEL(FactoryPanelConnectionPacket.class, FactoryPanelConnectionPacket::new, PLAY_TO_SERVER),
    CONFIGURE_FACTORY_PANEL(FactoryPanelConfigurationPacket.class, FactoryPanelConfigurationPacket::new, PLAY_TO_SERVER),
    CONFIGURE_REDSTONE_REQUESTER(RedstoneRequesterConfigurationPacket.class, RedstoneRequesterConfigurationPacket::new, PLAY_TO_SERVER),
    CONFIGURE_STOCK_KEEPER_CATEGORIES(StockKeeperCategoryEditPacket.class, StockKeeperCategoryEditPacket::new, PLAY_TO_SERVER),
    REFUND_STOCK_KEEPER_CATEGORY(StockKeeperCategoryRefundPacket.class, StockKeeperCategoryRefundPacket::new, PLAY_TO_SERVER),
    LOCK_STOCK_KEEPER(StockKeeperLockPacket.class, StockKeeperLockPacket::new, PLAY_TO_SERVER),
    STOCK_KEEPER_HIDE_CATEGORY(StockKeeperCategoryHidingPacket.class, StockKeeperCategoryHidingPacket::new, PLAY_TO_SERVER),
    VALUE_SETTINGS(ValueSettingsPacket2.class, ValueSettingsPacket2::new, PLAY_TO_SERVER),
    FILTER_SCREEN(FilterScreenPacket2.class, FilterScreenPacket2::new, PLAY_TO_SERVER),

    KNOCKBACK(KnockbackPacket.class, KnockbackPacket::new, PLAY_TO_CLIENT),

    S_PLACE_PACKAGE_PORT(PackagePortPlacementPacket.ClientBoundRequest.class, PackagePortPlacementPacket.ClientBoundRequest::new,
            PLAY_TO_CLIENT),
    ;

    public static final ResourceLocation CHANNEL_NAME = ResourceLocation.fromNamespaceAndPath(CreateLogisticsBackport.MODID, "main");
    public static final int NETWORK_VERSION = 1;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private PacketType<?> packetType;

    <T extends SimplePacketBase> ExtraPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                              NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        for (ExtraPackets packet : values())
            packet.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        getChannel().send(
                PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
                message);
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}
