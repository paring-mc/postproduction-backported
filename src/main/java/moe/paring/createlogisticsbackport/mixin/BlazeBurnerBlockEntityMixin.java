package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import moe.paring.createlogisticsbackport.polyfill.behaviour.BlazeBurnerStockKeeperBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlazeBurnerBlockEntity.class)
public class BlazeBurnerBlockEntityMixin {
    @Inject(method = "addBehaviours", at = @At("HEAD"), remap = false)
    private void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        behaviours.add(new BlazeBurnerStockKeeperBehaviour((BlazeBurnerBlockEntity) (Object) this));
    }
}
