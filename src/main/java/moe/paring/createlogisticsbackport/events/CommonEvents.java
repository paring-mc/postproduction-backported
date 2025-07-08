package moe.paring.createlogisticsbackport.events;

import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.foundation.utility.TickBasedCache;
import net.minecraftforge.event.TickEvent;
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
}
