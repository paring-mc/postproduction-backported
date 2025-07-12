package moe.paring.createlogisticsbackport.mixin;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import moe.paring.createlogisticsbackport.polyfill.BlazeBurnerRendererExtra;
import moe.paring.createlogisticsbackport.polyfill.behaviour.BlazeBurnerStockKeeperBehaviour;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(BlazeBurnerRenderer.class)
public class BlazeBurnerRendererMixin {
    @Unique
    private static WeakReference<BlazeBurnerBlockEntity> create_logistics_backport$blockEntity = new WeakReference<>(null);

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "HEAD"), remap = false)
    private void renderSafe(BlazeBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay, CallbackInfo ci) {
        create_logistics_backport$blockEntity = new WeakReference<>(be);
    }

    @Inject(method = "renderShared", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/render/CachedBufferer;partial(Lcom/jozufozu/flywheel/core/PartialModel;Lnet/minecraft/world/level/block/state/BlockState;)Lcom/simibubi/create/foundation/render/SuperByteBuffer;"), remap = false)
    private static void renderShared(PoseStack ms, PoseStack modelTransform, MultiBufferSource bufferSource, Level level, BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel, float animation, float horizontalAngle, boolean canDrawFlame, boolean drawGoggles, boolean drawHat, int hashCode, CallbackInfo ci) {
        var entity = create_logistics_backport$blockEntity.get();
        var isStockKeeper = false;
        if (entity != null) {
            isStockKeeper = entity.getBehaviour(BlazeBurnerStockKeeperBehaviour.TYPE).stockKeeper;
        }
        PartialModel hatModel = drawHat ? AllPartialModels.TRAIN_HAT : isStockKeeper ? ExtraPartialModels.LOGISTICS_HAT : null;

        BlazeBurnerRendererExtra.renderShared(ms, modelTransform, bufferSource, level, blockState, heatLevel, animation, horizontalAngle, canDrawFlame, drawGoggles, hatModel, hashCode);
    }
}
