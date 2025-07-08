package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageEntity;
import moe.paring.createlogisticsbackport.polyfill.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock.getFunnelFacing;
import static com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock.tryInsert;

@Mixin(FunnelBlock.class)
public abstract class FunnelBlockMixin {
    @Shadow protected abstract boolean canInsertIntoFunnel(BlockState state);

    @Inject(method = "entityInside", cancellable = true, at = @At("HEAD"))
    private void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        if (worldIn.isClientSide)
            return;
        ItemStack stack = ItemHelper.fromItemEntity(entityIn);
        if (stack.isEmpty())
            return;
        ci.cancel();
        if (!canInsertIntoFunnel(state))
            return;

        Direction direction = getFunnelFacing(state);
        Vec3 openPos = VecHelper.getCenterOf(pos)
                .add(Vec3.atLowerCornerOf(direction.getNormal())
                        .scale(entityIn instanceof ItemEntity ? -.25f : -.125f));
        Vec3 diff = entityIn.position()
                .subtract(openPos);
        double projectedDiff = direction.getAxis()
                .choose(diff.x, diff.y, diff.z);
        if (projectedDiff < 0 == (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE))
            return;
        float yOffset = direction == Direction.UP ? 0.25f : direction == Direction.DOWN ? -0.5f : -0.5f;
        if (!PackageEntity.centerPackage(entityIn, openPos.add(0, yOffset, 0)))
            return;

        ItemStack remainder = tryInsert(worldIn, pos, stack, false);
        if (remainder.isEmpty())
            entityIn.discard();
        if (remainder.getCount() < stack.getCount() && entityIn instanceof ItemEntity)
            ((ItemEntity) entityIn).setItem(remainder);
    }
}
