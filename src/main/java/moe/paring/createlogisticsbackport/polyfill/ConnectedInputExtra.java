package moe.paring.createlogisticsbackport.polyfill;

import com.simibubi.create.content.kinetics.crafter.ConnectedInputHandler.ConnectedInput;
import com.simibubi.create.content.kinetics.crafter.CrafterHelper;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import moe.paring.createlogisticsbackport.mixin.ConnectedInputAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ConnectedInputExtra {
    static List<MechanicalCrafterBlockEntity.Inventory> getInventories(ConnectedInput self, Level world, BlockPos pos) {
        var selfAcc = (ConnectedInputAccessor) self;
        if (!selfAcc.getIsController()) {
            BlockPos controllerPos = pos.offset(selfAcc.getData().get(0));
            ConnectedInput input = CrafterHelper.getInput(world, controllerPos);
            var inputAcc = (ConnectedInputAccessor) input;
            if (input == self || input == null || !inputAcc.getIsController())
                return List.of();
            return getInventories(input, world, controllerPos);
        }

        Direction facing = Direction.SOUTH;
        BlockState blockState = world.getBlockState(pos);
        if (blockState.hasProperty(MechanicalCrafterBlock.HORIZONTAL_FACING))
            facing = blockState.getValue(MechanicalCrafterBlock.HORIZONTAL_FACING);
        Direction.AxisDirection axisDirection = facing.getAxisDirection();
        Direction.Axis compareAxis = facing.getClockWise()
                .getAxis();

        Comparator<BlockPos> invOrdering = (p1, p2) -> {
            int compareY = -Integer.compare(p1.getY(), p2.getY());
            int modifier = axisDirection.getStep() * (compareAxis == Direction.Axis.Z ? -1 : 1);
            int c1 = compareAxis.choose(p1.getX(), p1.getY(), p1.getZ());
            int c2 = compareAxis.choose(p2.getX(), p2.getY(), p2.getZ());
            return compareY != 0 ? compareY : modifier * Integer.compare(c1, c2);
        };

        return selfAcc.getData().stream()
                .sorted(invOrdering)
                .map(l -> CrafterHelper.getCrafter(world, pos.offset(l)))
                .filter(Objects::nonNull)
                .map(MechanicalCrafterBlockEntity::getInventory)
                .collect(Collectors.toList());
    }
}
