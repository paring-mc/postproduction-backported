package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageEntity;
import moe.paring.createlogisticsbackport.polyfill.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractChuteBlock.class)
public class AbstractChuteBlockMixin {
    @Inject(method = "updateEntityAfterFallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;updateEntityAfterFallOn(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;)V"), cancellable = true)
    private void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn, CallbackInfo ci) {
        ItemStack stack = ItemHelper.fromItemEntity(entityIn);
        if (stack.isEmpty())
            return;
        ci.cancel();
        if (entityIn.level().isClientSide)
            return;
        if (!entityIn.isAlive())
            return;
        BlockPos pos = BlockPos.containing(entityIn.position()
                        .add(0, 0.5f, 0))
                .below();
        DirectBeltInputBehaviour input = BlockEntityBehaviour.get(entityIn.level(), pos, DirectBeltInputBehaviour.TYPE);
        if (input == null)
            return;
        if (!input.canInsertFromSide(Direction.UP))
            return;
        if (!PackageEntity.centerPackage(entityIn, Vec3.atBottomCenterOf(pos.above())))
            return;
        ItemStack remainder = input.handleInsertion(stack, Direction.UP, false);
        if (remainder.isEmpty()) {
            entityIn.discard();
        } else if (remainder.getCount() < stack.getCount() && entityIn instanceof ItemEntity)
            ((ItemEntity) entityIn).setItem(remainder);
    }
}
