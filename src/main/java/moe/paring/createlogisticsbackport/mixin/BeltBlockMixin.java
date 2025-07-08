package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.transport.BeltTunnelInteractionHandler;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageEntity;
import moe.paring.createlogisticsbackport.polyfill.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeltBlock.class)
public class BeltBlockMixin {
    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/belt/BeltHelper;getSegmentBE(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lcom/simibubi/create/content/kinetics/belt/BeltBlockEntity;"), cancellable = true)
    private void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        BeltBlockEntity belt = BeltHelper.getSegmentBE(worldIn, pos);
        if (belt == null)
        {
            ci.cancel();
            return;
        }

        var self = (BeltBlock)(Object)this;

        ItemStack asItem = ItemHelper.fromItemEntity(entityIn);
        if (!asItem.isEmpty()) {
            ci.cancel();
            if (worldIn.isClientSide)
                return;
            if (entityIn.getDeltaMovement().y > 0)
                return;
            Vec3 targetLocation = VecHelper.getCenterOf(pos)
                    .add(0, 5 / 16f, 0);
            if (!PackageEntity.centerPackage(entityIn, targetLocation))
                return;
            if (BeltTunnelInteractionHandler.getTunnelOnPosition(worldIn, pos) != null)
                return;
            self.withBlockEntityDo(worldIn, pos, be -> {
                IItemHandler handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElse(null);
                if (handler == null)
                    return;
                ItemStack remainder = handler.insertItem(0, asItem, false);
                if (remainder.isEmpty())
                    entityIn.discard();
                else if (entityIn instanceof ItemEntity itemEntity && remainder.getCount() != itemEntity.getItem().getCount())
                    itemEntity.setItem(remainder);
            });
            return;
        }
    }
}
