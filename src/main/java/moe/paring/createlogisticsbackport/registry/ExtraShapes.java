package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;

public class ExtraShapes {
    public static final VoxelShape
            PACKAGE_PORT = shape(0, 0, 0, 16, 4, 16).add(2, 2, 2, 14, 14, 14).build(),
            CHAIN_CONVEYOR_INTERACTION = shape(-10, 2, 0, 26, 14, 16).add(0, 2, -10, 16, 14, 26).add(-5, 2, -5, 21, 14, 21).add(Shapes.block()).build(),
            TABLE_CLOTH = shape(-1, -9, -1, 17, 1, 17).build(),
            TABLE_CLOTH_OCCLUSION = shape(0, 0, 0, 16, 1, 16).build(),
            STOCK_TICKER = shape(1, 0, 1, 15, 4, 15).add(2, 0, 2, 14, 16, 14)
                    .build();
    public static final VoxelShaper
            STOCK_LINK = shape(1, 0, 1, 15, 6, 15).forDirectional(),
            FACTORY_PANEL_FALLBACK = shape(0, 0, 0, 16, 2, 16).forDirectional(UP),
            ITEM_HATCH = shape(1, 0, 0, 15, 16, 2).add(2, 2, 0, 14, 13, 3.8)
                    .add(2, 4, 0, 14, 11, 5.8)
                    .add(2, 6, 0, 14, 9, 7.8)
                    .forHorizontal(SOUTH),
            POSTBOX = shape(2, 0, 0, 14, 14, 16).forHorizontal(SOUTH),
            DESK_BELL = shape(3, 0, 3, 13, 3, 13).add(4, 0, 4, 12, 9, 12)
                    .forDirectional(UP);

    private static AllShapes.Builder shape(VoxelShape shape) {
        return new AllShapes.Builder(shape);
    }

    private static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}
