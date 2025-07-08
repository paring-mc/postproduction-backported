package moe.paring.createlogisticsbackport.events;

import com.simibubi.create.CreateClient;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorConnectionHandler;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainPackageInteractionHandler;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelConnectionHandler;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortTargetSelectionHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null)
            return;

        KeyMapping key = event.getKeyMapping();

        if (key == mc.options.keyUse
                && (FactoryPanelConnectionHandler.onRightClick() || ChainConveyorConnectionHandler.onRightClick())) {
            event.setCanceled(true);
            return;
        }

        if (!event.isUseItem())
            return;

        if (ChainConveyorInteractionHandler.onUse()) {
            event.setCanceled(true);
            return;
        } else if (PackagePortTargetSelectionHandler.onUse()) {
            event.setCanceled(true);
            return;
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (ChainPackageInteractionHandler.onUse())
                event.setCanceled(true);
        });
    }
}
