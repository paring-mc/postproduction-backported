package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.Create;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Create.class)
public class CreateMixin {
    @Inject(method = "onCtor", at = @At("HEAD"), remap = false)
    private static void onCtor(CallbackInfo ci) {
        while (!CreateLogisticsBackport.INITIALIZED) {
            Thread.yield();
        }
    }
}
