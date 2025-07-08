package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlazeBurnerBlockEntity.class)
public interface BlazeBurnerBlockEntityAccessor {
    @Accessor("headAnimation")
    LerpedFloat getHeadAnimation();

    @Accessor("hat")
    boolean getHat();

    @Accessor("goggles")
    boolean getGoggles();
}
