package moe.paring.createlogisticsbackport.content.trains.schedule.destination;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.foundation.utility.Pair;

public class ExtraSchedules {
    public static void init() {
        Schedule.INSTRUCTION_TYPES.add(Pair.of(Create.asResource("package_delivery"), DeliverPackagesInstruction::new));
        Schedule.INSTRUCTION_TYPES.add(Pair.of(Create.asResource("package_retrieval"), FetchPackagesInstruction::new));
    }
}
