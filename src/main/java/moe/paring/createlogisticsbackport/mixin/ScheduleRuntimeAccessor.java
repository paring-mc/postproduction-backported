package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScheduleRuntime.class)
public interface ScheduleRuntimeAccessor {
    @Accessor("train")
    Train getTrain();

    @Accessor("cooldown")
    void setCooldown(int cooldown);
}
