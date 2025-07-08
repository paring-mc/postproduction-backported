package moe.paring.createlogisticsbackport.mixin;

import moe.paring.createlogisticsbackport.accessor.DisplayLinkBlockEntityMixinAccessor;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelPosition;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelSupportBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DisplayLinkBlockEntity.class)
public class DisplayLinkBlockEntityMixin implements DisplayLinkBlockEntityMixinAccessor {
    @Unique
    private FactoryPanelSupportBehaviour create_logistics_backport$factoryPanelSupport;

    @Inject(method = "addBehaviours", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
    private void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        var be = (DisplayLinkBlockEntity) (Object) this;

        behaviours.add(create_logistics_backport$factoryPanelSupport = new FactoryPanelSupportBehaviour(be, () -> false, () -> false, be::updateGatheredData));
    }

    @Inject(method = "getSourcePosition", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void getSourcePosition(CallbackInfoReturnable<BlockPos> cir) {
        for (FactoryPanelPosition position : create_logistics_backport$factoryPanelSupport.getLinkedPanels()) {
            cir.setReturnValue(position.pos());
            cir.cancel();
            return;
        }
    }

    @Override
    public FactoryPanelSupportBehaviour create_logistics_backport$getPanelSupport() {
        return create_logistics_backport$factoryPanelSupport;
    }
}
