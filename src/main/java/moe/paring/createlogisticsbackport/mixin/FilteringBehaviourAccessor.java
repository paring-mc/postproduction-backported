package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FilteringBehaviour.class)
public interface FilteringBehaviourAccessor {
    @Accessor
    FilterItemStack getFilter();

    @Accessor
    void setFilter(FilterItemStack filter);
}
