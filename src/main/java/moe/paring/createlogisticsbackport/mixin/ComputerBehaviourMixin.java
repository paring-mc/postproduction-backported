package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import moe.paring.createlogisticsbackport.compat.computercraft.peripherals.*;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport.FrogportBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packagePort.postbox.PostboxBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packager.PackagerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.packager.repackager.RepackagerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.redstoneRequester.RedstoneRequesterBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockEntity;
import net.minecraftforge.common.util.NonNullSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComputerBehaviour.class)
public class ComputerBehaviourMixin {
    @Inject(method = "getPeripheralFor", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getPeripheralFor(SmartBlockEntity be, CallbackInfoReturnable<NonNullSupplier<IPeripheral>> cir) {
        if (be instanceof FrogportBlockEntity fpbe) {
            cir.setReturnValue(() -> new FrogportPeripheral(fpbe));
            return;
        }
        if (be instanceof PostboxBlockEntity pbbe) {
            cir.setReturnValue(() -> new PostboxPeripheral(pbbe));
            return;
        }
        if (be instanceof StockTickerBlockEntity sgbe) {
            cir.setReturnValue(() -> new StockTickerPeripheral(sgbe));
            return;
        }
        // Has to be before PackagerBlockEntity as it's a subclass
        if (be instanceof RepackagerBlockEntity rpbe) {
            cir.setReturnValue(() -> new RepackagerPeripheral(rpbe));
            return;
        }
        if (be instanceof PackagerBlockEntity pgbe) {
            cir.setReturnValue(() -> new PackagerPeripheral(pgbe));
            return;
        }
        if (be instanceof RedstoneRequesterBlockEntity rrbe) {
            cir.setReturnValue(() -> new RedstoneRequesterPeripheral(rrbe));
            return;
        }
        if (be instanceof TableClothBlockEntity tcbe) {
            cir.setReturnValue(() -> new TableClothShopPeripheral(tcbe));
//            return;
        }
    }
}
