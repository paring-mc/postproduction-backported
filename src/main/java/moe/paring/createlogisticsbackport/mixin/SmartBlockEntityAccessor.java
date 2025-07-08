package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SmartBlockEntity.class)
public interface SmartBlockEntityAccessor {
    @Invoker("attachBehaviourLate")
    void invokeAttachBehaviourLate(BlockEntityBehaviour behaviour);
}
