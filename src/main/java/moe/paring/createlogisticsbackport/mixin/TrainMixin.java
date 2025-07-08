package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.trains.entity.Train;
import moe.paring.createlogisticsbackport.accessor.GlobalStationMixinAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Train.class)
public class TrainMixin {
    @Unique
    int create_logistics_backport$ticksSinceLastMailTransfer;

    @Inject(method = "tick", at = @At(value = "HEAD"), remap = false)
    private void onTick(CallbackInfo ci) {
        var self = (Train) (Object) this;

        var currentStation = self.getCurrentStation();
        if (currentStation != null) {
            create_logistics_backport$ticksSinceLastMailTransfer++;
            if (create_logistics_backport$ticksSinceLastMailTransfer > 20) {
                ((GlobalStationMixinAccessor)currentStation).create_logistics_backport$runMailTransfer();
                create_logistics_backport$ticksSinceLastMailTransfer = 0;
            }
        }
    }
}
