package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;

public class ExtraSpriteShifts {
    public static final SpriteShiftEntry FACTORY_PANEL_CONNECTIONS =
            get("block/factory_panel_connections", "block/factory_panel_connections_animated");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), Create.asResource(targetLocation));
    }

    public static void register() {}
}
