package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlockEntity;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerInteractionHandler;
import moe.paring.createlogisticsbackport.polyfill.behaviour.BlazeBurnerStockKeeperBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeBurnerBlock.class)
public class BlazeBurnerBlockMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/ItemEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    private void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult, CallbackInfoReturnable<InteractionResult> cir) {
        var self = (BlazeBurnerBlock) (Object) this;
        BlazeBurnerBlockEntity be = self.getBlockEntity(world, pos);
        if (be != null) {
            StockTickerBlockEntity stockTicker = BlazeBurnerStockKeeperBehaviour.getStockTicker(world, pos);
            if (stockTicker != null)
            {
                StockTickerInteractionHandler.interactWithLogisticsManagerAt(player, world, stockTicker.getBlockPos());
                cir.setReturnValue(InteractionResult.SUCCESS);
                cir.cancel();
            }
        }
    }
}
