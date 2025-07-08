package moe.paring.createlogisticsbackport.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class ECKinetics extends ConfigBase {
    public final ConfigInt maxChainConveyorLength = i(32, 5, "maxChainConveyorLength", Comments.maxChainConveyorLength);
    public final ConfigInt maxChainConveyorConnections = i(4, 1, "maxChainConveyorConnections", Comments.maxChainConveyorConnections);

    @Override
    public String getName() {
        return "kinetics";
    }

    private static class Comments {
        static String maxChainConveyorLength = "Maximum length in blocks of chain conveyor connections.";
        static String maxChainConveyorConnections = "Maximum amount of connections each chain conveyor can have.";
    }
}
