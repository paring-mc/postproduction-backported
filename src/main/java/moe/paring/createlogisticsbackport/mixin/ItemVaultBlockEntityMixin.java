package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import moe.paring.createlogisticsbackport.accessor.ItemVaultBlockEntityMixinAccessor;
import moe.paring.createlogisticsbackport.api.packager.InventoryIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemVaultBlockEntity.class)
public abstract class ItemVaultBlockEntityMixin implements ItemVaultBlockEntityMixinAccessor {
    @Shadow public abstract boolean isController();
    @Shadow protected LazyOptional<IItemHandler> itemCapability;

    @Shadow protected int radius;
    @Shadow protected int length;
    @Unique
    private InventoryIdentifier create_logistics_backport$invId;

    @Override
    public InventoryIdentifier create_logistics_backport$getInventoryIdentifier() {
        return create_logistics_backport$invId;
    }

    @Inject(method = "initCapability", at = @At("TAIL"), remap = false)
    private void onInitCapability(CallbackInfo ci) {
        var self = (ItemVaultBlockEntity)(Object)this;
        boolean alongZ = ItemVaultBlock.getVaultBlockAxis(self.getBlockState()) == Direction.Axis.Z;
        var worldPosition = self.worldPosition;
        BlockPos farCorner = alongZ
                ? worldPosition.offset(radius, radius, length)
                : worldPosition.offset(length, radius, radius);
        BoundingBox bounds = BoundingBox.fromCorners(worldPosition, farCorner);
        create_logistics_backport$invId = new InventoryIdentifier.Bounds(bounds);
    }

}
