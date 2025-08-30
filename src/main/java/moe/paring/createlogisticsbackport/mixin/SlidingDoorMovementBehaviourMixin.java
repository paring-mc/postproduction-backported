package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlidingDoorMovementBehaviour.class)
public abstract class SlidingDoorMovementBehaviourMixin {
    @Shadow(remap = false) protected abstract Direction getDoorFacing(MovementContext context);

    @Shadow(remap = false) protected abstract boolean shouldOpen(MovementContext context);

    @Inject(method = "tickOpen", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/slidingDoor/SlidingDoorMovementBehaviour;toggleDoor(Lnet/minecraft/core/BlockPos;Lcom/simibubi/create/content/contraptions/Contraption;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)V"), remap = false)
    private void toggleDoor(MovementContext context, boolean currentlyOpen, CallbackInfo ci) {
        boolean shouldOpen = shouldOpen(context);

        Direction facing = getDoorFacing(context);
        BlockPos inWorldDoor = BlockPos.containing(context.position)
                .relative(facing);
        BlockState inWorldDoorState = context.world.getBlockState(inWorldDoor);
        if (inWorldDoorState.getBlock() instanceof DoorBlock db && inWorldDoorState.hasProperty(DoorBlock.OPEN))
            if (inWorldDoorState.hasProperty(DoorBlock.FACING) && inWorldDoorState.getOptionalValue(DoorBlock.FACING)
                    .orElse(Direction.UP)
                    .getAxis() == facing.getAxis())
                db.setOpen(null, context.world, inWorldDoorState, inWorldDoor, shouldOpen);

    }
}
