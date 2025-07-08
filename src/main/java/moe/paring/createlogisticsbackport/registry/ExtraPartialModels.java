package moe.paring.createlogisticsbackport.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageStyles;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ExtraPartialModels {
    public static final PartialModel
            FROGPORT_BODY = block("package_frogport/body"), FROGPORT_HEAD = block("package_frogport/head"),
            FROGPORT_TONGUE = block("package_frogport/tongue"),
            POSTBOX_FLAG = block("package_postbox/flag"),
            PACKAGER_TRAY_REGULAR = block("packager/tray"), PACKAGER_TRAY_DEFRAG = block("repackager/tray"),
            PACKAGER_HATCH_OPEN = block("packager/hatch_open"), PACKAGER_HATCH_CLOSED = block("packager/hatch_closed"),
            TABLE_CLOTH_NW = block("table_cloth/north_west"),
            TABLE_CLOTH_NE = block("table_cloth/north_east"),
            TABLE_CLOTH_SW = block("table_cloth/south_west"),
            TABLE_CLOTH_SE = block("table_cloth/south_east"),
            CHAIN_CONVEYOR_GUARD = block("chain_conveyor/guard"),
            CHAIN_CONVEYOR_SHAFT = block("chain_conveyor/shaft"),
            FACTORY_PANEL = block("factory_gauge/panel"),
            FACTORY_PANEL_WITH_BULB = block("factory_gauge/panel_with_bulb"),
            FACTORY_PANEL_RESTOCKER = block("factory_gauge/panel_restocker"),
            FACTORY_PANEL_RESTOCKER_WITH_BULB = block("factory_gauge/panel_restocker_with_bulb"),
            FACTORY_PANEL_LIGHT = block("factory_gauge/bulb_light"),
            FACTORY_PANEL_RED_LIGHT = block("factory_gauge/bulb_red"),
            TABLE_CLOTH_PRICE_SIDE = block("table_cloth/price_tag_side"),
            TABLE_CLOTH_PRICE_TOP = block("table_cloth/price_tag_top"),
            LOGISTICS_HAT = entity("logistics_hat");

    public static final Map<Direction, PartialModel> FACTORY_PANEL_ARROWS = new EnumMap<>(Direction.class);
    public static final Map<Direction, PartialModel> FACTORY_PANEL_LINES = new EnumMap<>(Direction.class);
    public static final Map<Direction, PartialModel> FACTORY_PANEL_DOTTED = new EnumMap<>(Direction.class);


    public static final Map<ResourceLocation, PartialModel> PACKAGES = new HashMap<>();
    public static final List<PartialModel> PACKAGES_TO_HIDE_AS = new ArrayList<>();
    public static final Map<ResourceLocation, PartialModel> PACKAGE_RIGGING = new HashMap<>();

    static {
        for (Direction d : Iterate.horizontalDirections) {
            FACTORY_PANEL_ARROWS.put(d, block("factory_gauge/connections/arrow_" + Lang.asId(d.name())));
            FACTORY_PANEL_LINES.put(d, block("factory_gauge/connections/line_" + Lang.asId(d.name())));
            FACTORY_PANEL_DOTTED.put(d, block("factory_gauge/connections/dotted_" + Lang.asId(d.name())));
        }

        for (PackageStyles.PackageStyle style : PackageStyles.STYLES) {
            ResourceLocation key = style.getItemId();
            PartialModel model = new PartialModel(Create.asResource("item/" + key.getPath()));
            PACKAGES.put(key, model);
            if (!style.rare())
                PACKAGES_TO_HIDE_AS.add(model);
            PACKAGE_RIGGING.put(key, new PartialModel(style.getRiggingModel()));
        }
    }

    private static PartialModel block(String path) {
        return new PartialModel(Create.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(Create.asResource("entity/" + path));
    }

    public static void init() {}
}
