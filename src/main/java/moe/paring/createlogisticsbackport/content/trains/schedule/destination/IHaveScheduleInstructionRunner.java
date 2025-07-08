package moe.paring.createlogisticsbackport.content.trains.schedule.destination;

import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import moe.paring.createlogisticsbackport.mixin.ScheduleRuntimeAccessor;

public interface IHaveScheduleInstructionRunner {
    DiscoveredPath start(ScheduleRuntime runtime);

    default void startCooldown(ScheduleRuntime runtime) {
        ((ScheduleRuntimeAccessor)runtime).setCooldown(40);
    }
}
