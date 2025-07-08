package moe.paring.createlogisticsbackport.polyfill;

import com.google.common.base.Predicates;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.utility.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class NewInvManipulationBehaviour extends InvManipulationBehaviour {
    protected Predicate<BlockEntity> filter;

    public NewInvManipulationBehaviour(SmartBlockEntity be, InterfaceProvider target) {
        super(be, target);

        filter = Predicates.alwaysTrue();
    }

    public NewInvManipulationBehaviour withFilter(Predicate<BlockEntity> filter) {
        return this;
    }

    @Override
    public void findNewCapability() {
        Level world = getWorld();
        BlockFace targetBlockFace = target.getTarget(world, blockEntity.getBlockPos(), blockEntity.getBlockState())
                .getOpposite();
        BlockPos pos = targetBlockFace.getPos();

        targetCapability = LazyOptional.empty();

        if (!world.isLoaded(pos))
            return;
        BlockEntity invBE = world.getBlockEntity(pos);
        if (invBE == null || !filter.test(invBE))
            return;
        Capability<IItemHandler> capability = capability();
        targetCapability =
                bypassSided ? invBE.getCapability(capability) : invBE.getCapability(capability, targetBlockFace.getFace());
    }
}
