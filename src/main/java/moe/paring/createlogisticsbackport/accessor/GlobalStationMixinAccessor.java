package moe.paring.createlogisticsbackport.accessor;

import moe.paring.createlogisticsbackport.polyfill.trains.GlobalPackagePort;
import net.minecraft.core.BlockPos;

import java.util.Map;

public interface GlobalStationMixinAccessor {
    Map<BlockPos, GlobalPackagePort> create_logistics_backport$getConnectedPorts();
    void create_logistics_backport$runMailTransfer();
}
