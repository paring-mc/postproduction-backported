package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmartBlockEntity.class)
public class SmartBlockEntityMixin {
    @Inject(method = "attachBehaviourLate", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private void onAttachBehaviourLate(BlockEntityBehaviour behaviour, CallbackInfo ci) {
        behaviour.blockEntity = (SmartBlockEntity) (Object) this;
    }
}
