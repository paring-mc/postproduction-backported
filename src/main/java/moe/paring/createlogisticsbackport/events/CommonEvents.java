package moe.paring.createlogisticsbackport.events;

import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.config.ExtraConfigs;
import moe.paring.createlogisticsbackport.content.kinetics.chainConveyor.ServerChainConveyorHandler;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        TickBasedCache.tick();
        ServerChainConveyorHandler.tick();
    }

    @SubscribeEvent
    public static void onServerWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (event.side == LogicalSide.CLIENT) return;

        CreateLogisticsBackport.LOGISTICS.tick(event.level);
    }

    @SubscribeEvent
    public void onLoadWorld(LevelEvent.Load event) {
        CreateLogisticsBackport.LOGISTICS.levelLoaded(event.getLevel());
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (!ExtraConfigs.server().suppressWarningMessage.get())
            if (event.getEntity() instanceof Player player && !player.isLocalPlayer()) {
                player.displayClientMessage(Component.literal("Post Production Backported is installed. Any contents from create 6 may have critical bugs and use at your own risk.").withStyle(ChatFormatting.YELLOW), false);
            }
    }
}
