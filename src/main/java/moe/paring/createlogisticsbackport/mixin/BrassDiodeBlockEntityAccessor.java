package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.redstone.diodes.BrassDiodeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrassDiodeBlockEntity.class)
public interface BrassDiodeBlockEntityAccessor {
    @Accessor("maxState")
    ScrollValueBehaviour getMaxState();
}
