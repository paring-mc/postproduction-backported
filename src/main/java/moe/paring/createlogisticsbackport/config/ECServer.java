package moe.paring.createlogisticsbackport.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class ECServer extends ConfigBase {
    public final ECKinetics kinetics = nested(0, ECKinetics::new);
    public final ECLogistics logistics = nested(0, ECLogistics::new);

    @Override
    public String getName() {
        return "server";
    }
}
