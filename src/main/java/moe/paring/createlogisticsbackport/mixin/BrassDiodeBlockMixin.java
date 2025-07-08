package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlockEntity;
import moe.paring.createlogisticsbackport.registry.ExtraBlockEntityTypes;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrassDiodeBlock.class)
public class BrassDiodeBlockMixin {
    @Inject(method = "getBlockEntityType", remap = false, at = @At("HEAD"), cancellable = true)
    private void getBlockEntityType(CallbackInfoReturnable<BlockEntityType<? extends BrassDiodeBlockEntity>> cir) {
        var self = (BrassDiodeBlock)(Object)this;
        if (ExtraBlocks.PULSE_TIMER.is(self)) {
            cir.setReturnValue(ExtraBlockEntityTypes.PULSE_TIMER.get());
            cir.cancel();
        }
    }
}
