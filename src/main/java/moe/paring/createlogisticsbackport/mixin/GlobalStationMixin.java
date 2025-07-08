package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.GlobalStation;
import moe.paring.createlogisticsbackport.accessor.GlobalStationMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlockEntity;
import moe.paring.createlogisticsbackport.polyfill.trains.GlobalPackagePort;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(GlobalStation.class)
public class GlobalStationMixin implements GlobalStationMixinAccessor {
    @Unique
    public Map<BlockPos, GlobalPackagePort> create_logistics_backport$connectedPorts = new HashMap<>();

    @Override
    public Map<BlockPos, GlobalPackagePort> create_logistics_backport$getConnectedPorts() {
        return create_logistics_backport$connectedPorts;
    }

    @Unique
    public void create_logistics_backport$runMailTransfer() {
        var self = (GlobalStation)(Object)this;
        Train train = self.getPresentTrain();
        if (train == null || create_logistics_backport$connectedPorts.isEmpty())
            return;
        Level level = null;

        for (Carriage carriage : train.carriages) {
            if (level == null) {
                CarriageContraptionEntity entity = carriage.anyAvailableEntity();
                if (entity != null && entity.level() instanceof ServerLevel sl)
                    level = sl.getServer()
                            .getLevel(self.getBlockEntityDimension());
            }

            IItemHandlerModifiable carriageInventory = carriage.storage.getItems();
            if (carriageInventory == null)
                continue;

            // Import from station
            for (Map.Entry<BlockPos, GlobalPackagePort> entry : create_logistics_backport$connectedPorts.entrySet()) {
                GlobalPackagePort port = entry.getValue();
                BlockPos pos = entry.getKey();
                PostboxBlockEntity box = null;

                IItemHandlerModifiable postboxInventory = port.offlineBuffer;
                if (level != null && level.isLoaded(pos)
                        && level.getBlockEntity(pos) instanceof PostboxBlockEntity ppbe) {
                    postboxInventory = ppbe.inventory;
                    box = ppbe;
                }

                for (int slot = 0; slot < postboxInventory.getSlots(); slot++) {
                    ItemStack stack = postboxInventory.getStackInSlot(slot);
                    if (!PackageItem.isPackage(stack))
                        continue;
                    if (PackageItem.matchAddress(stack, port.address))
                        continue;

                    ItemStack result = ItemHandlerHelper.insertItemStacked(carriageInventory, stack, false);
                    if (!result.isEmpty())
                        continue;

                    postboxInventory.setStackInSlot(slot, ItemStack.EMPTY);
                    Create.RAILWAYS.markTracksDirty();
                    if (box != null)
                        box.spawnParticles();
                }
            }

            // Export to station
            for (int slot = 0; slot < carriageInventory.getSlots(); slot++) {
                ItemStack stack = carriageInventory.getStackInSlot(slot);
                if (!PackageItem.isPackage(stack))
                    continue;

                for (Map.Entry<BlockPos, GlobalPackagePort> entry : create_logistics_backport$connectedPorts.entrySet()) {
                    GlobalPackagePort port = entry.getValue();
                    BlockPos pos = entry.getKey();
                    PostboxBlockEntity box = null;

                    if (!PackageItem.matchAddress(stack, port.address))
                        continue;

                    IItemHandler postboxInventory = port.offlineBuffer;
                    if (level != null && level.isLoaded(pos)
                            && level.getBlockEntity(pos) instanceof PostboxBlockEntity ppbe) {
                        postboxInventory = ppbe.inventory;
                        box = ppbe;
                    }

                    ItemStack result = ItemHandlerHelper.insertItemStacked(postboxInventory, stack, false);
                    if (!result.isEmpty())
                        continue;

                    Create.RAILWAYS.markTracksDirty();
                    carriageInventory.setStackInSlot(slot, ItemStack.EMPTY);
                    if (box != null)
                        box.spawnParticles();

                    break;
                }
            }

        }
    }
}
