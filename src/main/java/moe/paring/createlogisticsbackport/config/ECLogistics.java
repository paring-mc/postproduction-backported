package moe.paring.createlogisticsbackport.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class ECLogistics extends ConfigBase {

    public final ConfigInt packagePortRange = i(5, 1, "packagePortRange", Comments.packagePortRange);
    public final ConfigInt chainConveyorCapacity = i(20, 1, "chainConveyorCapacity", Comments.chainConveyorCapacity);
    public final ConfigInt factoryGaugeTimer = i(100, 5, "factoryGaugeTimer", Comments.factoryGaugeTimer);

    @Override
    public String getName() {
        return "logistics";
    }

    private static class Comments {
        static String packagePortRange = "Maximum distance in blocks a Package Port can be placed at from its target.";
        static String chainConveyorCapacity = "The amount of packages a chain conveyor can carry at a time.";
        static String factoryGaugeTimer = "The amount of ticks a factory gauge waits between requests.";
    }
}
