package moe.paring.createlogisticsbackport;

import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueSettingsClient2;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class CreateLogisticsBackportClient {
    public static final ValueSettingsClient2 VALUE_SETTINGS_HANDLER = new ValueSettingsClient2();

    public static void onInit(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CreateLogisticsBackportClient::clientInit);
    }

    private static void clientInit(final FMLClientSetupEvent event) {
        ExtraPartialModels.init();
    }
}
