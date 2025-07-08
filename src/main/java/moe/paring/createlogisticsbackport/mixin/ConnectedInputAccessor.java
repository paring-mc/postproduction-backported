package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.kinetics.crafter.ConnectedInputHandler.ConnectedInput;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ConnectedInput.class)
public interface ConnectedInputAccessor {
    @Accessor("isController")
    boolean getIsController();

    @Accessor("data")
    List<BlockPos> getData();
}
