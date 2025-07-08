package moe.paring.createlogisticsbackport.mixin;

import moe.paring.createlogisticsbackport.accessor.GlobalStationMixinAccessor;
import moe.paring.createlogisticsbackport.accessor.StationBlockEntityMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlockEntity;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import moe.paring.createlogisticsbackport.polyfill.trains.GlobalPackagePort;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;
import java.util.Objects;

@Mixin(StationBlockEntity.class)
public class StationBlockEntityMixin implements StationBlockEntityMixinAccessor {
    public void create_logistics_backport$attachPackagePort(PackagePortBlockEntity ppbe) {
        var origThis = (StationBlockEntity)(Object)this;
        GlobalStation station = origThis.getStation();
        var stAcc = (GlobalStationMixinAccessor)station;
        if (station == null || Objects.requireNonNull(origThis.getLevel()).isClientSide)
            return;

        var connectedPorts = stAcc.create_logistics_backport$getConnectedPorts();

        if (ppbe instanceof PostboxBlockEntity pbe)
            pbe.trackedGlobalStation = new WeakReference<GlobalStation>(station);

        if (connectedPorts.containsKey(ppbe.getBlockPos()))
            create_logistics_backport$restoreOfflineBuffer(ppbe, connectedPorts.get(ppbe.getBlockPos()));

        GlobalPackagePort globalPackagePort = new GlobalPackagePort();
        globalPackagePort.address = ppbe.addressFilter;
        connectedPorts.put(ppbe.getBlockPos(), globalPackagePort);
    }

    public void create_logistics_backport$removePackagePort(PackagePortBlockEntity ppbe) {
        var origThis = (StationBlockEntity)(Object)this;
        GlobalStation station = origThis.getStation();
        if (station == null)
            return;

        var stAcc = (GlobalStationMixinAccessor)station;
        var connectedPorts = stAcc.create_logistics_backport$getConnectedPorts();

        connectedPorts.remove(ppbe.getBlockPos());
    }

    @Unique
    private void create_logistics_backport$restoreOfflineBuffer(PackagePortBlockEntity ppbe, GlobalPackagePort globalPackagePort) {
        if (!globalPackagePort.primed)
            return;
        for (int i = 0; i < globalPackagePort.offlineBuffer.getSlots(); i++) {
            ppbe.inventory.setStackInSlot(i, globalPackagePort.offlineBuffer.getStackInSlot(i));
            globalPackagePort.offlineBuffer.setStackInSlot(i, ItemStack.EMPTY);
        }
        globalPackagePort.primed = false;
    }
}
