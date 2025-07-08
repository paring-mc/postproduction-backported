package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;
import moe.paring.createlogisticsbackport.foundation.block.connected.AllCTTypes2;
import moe.paring.createlogisticsbackport.foundation.block.connected.CTSpriteShiftEntry2;
import moe.paring.createlogisticsbackport.foundation.block.connected.CTSpriteShifter2;
import moe.paring.createlogisticsbackport.foundation.block.connected.CTType2;

public class ExtraSpriteShifts {
    public static final SpriteShiftEntry FACTORY_PANEL_CONNECTIONS =
            get("block/factory_panel_connections", "block/factory_panel_connections_animated");

    public static final CTSpriteShiftEntry2
            OLD_FACTORY_WINDOW_1 = getCT(AllCTTypes2.RECTANGLE, "palettes/weathered_iron_window", "palettes/weathered_iron_window_1"),
            OLD_FACTORY_WINDOW_2 = getCT(AllCTTypes2.RECTANGLE, "palettes/weathered_iron_window", "palettes/weathered_iron_window_2"),
            OLD_FACTORY_WINDOW_3 = getCT(AllCTTypes2.RECTANGLE, "palettes/weathered_iron_window", "palettes/weathered_iron_window_3"),
            OLD_FACTORY_WINDOW_4 = getCT(AllCTTypes2.RECTANGLE, "palettes/weathered_iron_window", "palettes/weathered_iron_window_4");

    private static CTSpriteShiftEntry2 getCT(CTType2 type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter2.getCT(type, Create.asResource("block/" + blockTextureName),
                Create.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), Create.asResource(targetLocation));
    }

    public static void register() {
    }
}
