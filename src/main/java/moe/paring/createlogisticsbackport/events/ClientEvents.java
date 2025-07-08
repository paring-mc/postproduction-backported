package moe.paring.createlogisticsbackport.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import moe.paring.createlogisticsbackport.CreateLogisticsBackportClient;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorConnectionHandler;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ChainConveyorRidingHandler;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelConnectionHandler;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortTargetSelectionHandler;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.LogisticallyLinkedClientHandler;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothOverlayRenderer;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import moe.paring.createlogisticsbackport.polyfill.behaviour.filtering.FilteringRenderer2;
import moe.paring.createlogisticsbackport.polyfill.misc.LogisticsHatArmorLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jozufozu.flywheel.backend.Backend.isGameActive;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        Level world = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        ChainConveyorInteractionHandler.clientTick();
        ChainConveyorRidingHandler.clientTick();
        ChainConveyorConnectionHandler.clientTick();
        PackagePortTargetSelectionHandler.tick();
        LogisticallyLinkedClientHandler.tick();
        TableClothOverlayRenderer.tick();
//        CardboardArmorStealthOverlay.clientTick();
        FactoryPanelConnectionHandler.clientTick();
        TickBasedCache.clientTick();
        FilteringRenderer2.tick();
        CreateLogisticsBackportClient.VALUE_SETTINGS_HANDLER.tick();
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            return;

        PoseStack ms = event.getPoseStack();
        ms.pushPose();
        SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.getInstance();
//        float partialTicks = AnimationTickHolder.getPartialTicks();
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera()
                .getPosition();

        ChainConveyorInteractionHandler.drawCustomBlockSelection(ms, buffer, camera);

        buffer.draw();
        RenderSystem.enableCull();
        ms.popPose();
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void addEntityRenderLayers(EntityRenderersEvent.AddLayers event) {
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            LogisticsHatArmorLayer.registerOnAll(dispatcher);
        }

//        @SubscribeEvent
//        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
//            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "value_settings", CreateLogisticsBackportClient.VALUE_SETTINGS_HANDLER);
//        }
    }
}
