package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.kinetics.crafter.ConnectedInputHandler;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MechanicalCrafterBlockEntity.class)
public interface MechanicalCrafterBlockEntityAccessor {
    @Invoker("checkCompletedRecipe")
    void invokeCheckCompletedRecipe(boolean poweredStart);

    @Accessor("input")
    ConnectedInputHandler.ConnectedInput getInput();
}
