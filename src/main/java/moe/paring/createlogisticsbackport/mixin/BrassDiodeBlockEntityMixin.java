package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.redstone.diodes.BrassDiodeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import moe.paring.createlogisticsbackport.content.redstone.diodes.IHaveBrassDiodeDefaultValue;
import moe.paring.createlogisticsbackport.content.redstone.diodes.PulseTimerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BrassDiodeBlockEntity.class)
public class BrassDiodeBlockEntityMixin {
    @Redirect(method = "addBehaviours", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/blockEntity/behaviour/scrollValue/ScrollValueBehaviour;setValue(I)V"), remap = false)
    private void addBehaviours(ScrollValueBehaviour instance, int value) {
        var self = (BrassDiodeBlockEntity)(Object)this;
        if (self instanceof IHaveBrassDiodeDefaultValue defaultValue) {
            instance.setValue(defaultValue.defaultValue());
            return;
        }

        instance.setValue(value);
    }
}
