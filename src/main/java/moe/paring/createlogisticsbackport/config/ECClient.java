package moe.paring.createlogisticsbackport.config;

import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.infrastructure.config.CClient;

public class ECClient extends ConfigBase {
    public final ConfigGroup integration = group(1, "jeiIntegration",
            Comments.integration);
    public final ConfigBool syncJeiSearch = b(true, "syncJeiSearch",
            Comments.syncJeiSearch);

    @Override
    public String getName() {
        return "client";
    }

    private static class Comments {
        static String integration = "Mod Integration and JEI";
        static String syncJeiSearch = "Whether to auto-update the JEI search when searching in the stock keeper UI";
    }
}
