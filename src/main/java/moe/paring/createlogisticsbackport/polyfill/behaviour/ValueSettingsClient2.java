package moe.paring.createlogisticsbackport.polyfill.behaviour;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.gui.ScreenOpener;
import moe.paring.createlogisticsbackport.registry.ExtraPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class ValueSettingsClient2 {
    private final Minecraft mc;

    public int interactHeldTicks = -1;
    public BlockPos interactHeldPos = null;
    public BehaviourType<?> interactHeldBehaviour = null;
    public InteractionHand interactHeldHand = null;
    public Direction interactHeldFace = null;

    public List<MutableComponent> lastHoverTip;
    public int hoverTicks;
    public int hoverWarmup;

    public ValueSettingsClient2() {
        mc = Minecraft.getInstance();
    }

    public void cancelIfWarmupAlreadyStarted(PlayerInteractEvent.RightClickBlock event) {
        if (interactHeldTicks != -1 && event.getPos()
                .equals(interactHeldPos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }

    public void startInteractionWith(BlockPos pos, BehaviourType<?> behaviourType, InteractionHand hand,
                                     Direction side) {
        interactHeldTicks = 0;
        interactHeldPos = pos;
        interactHeldBehaviour = behaviourType;
        interactHeldHand = hand;
        interactHeldFace = side;
    }

    public void cancelInteraction() {
        interactHeldTicks = -1;
    }

    public void tick() {
        if (hoverWarmup > 0)
            hoverWarmup--;
        if (hoverTicks > 0)
            hoverTicks--;

        if (interactHeldTicks == -1)
            return;
        Player player = mc.player;

        if (!ValueSettingsInputHandler2.canInteract(player) || AllBlocks.CLIPBOARD.isIn(player.getMainHandItem())) {
            cancelInteraction();
            return;
        }
        HitResult hitResult = mc.hitResult;

        if (!(hitResult instanceof BlockHitResult blockHitResult) || !blockHitResult.getBlockPos()
                .equals(interactHeldPos)) {
            cancelInteraction();
            return;
        }
        BlockEntityBehaviour behaviour = BlockEntityBehaviour.get(mc.level, interactHeldPos, interactHeldBehaviour);
        if (!(behaviour instanceof ValueSettingsBehaviour2 valueSettingBehaviour)
                || valueSettingBehaviour.bypassesInput(player.getMainHandItem())
                || !valueSettingBehaviour.testHit(blockHitResult.getLocation())) {
            cancelInteraction();
            return;
        }
        if (!mc.options.keyUse.isDown()) {
            ExtraPackets.getChannel()
                    .sendToServer(new ValueSettingsPacket2(interactHeldPos, 0, 0, interactHeldHand, blockHitResult,
                            interactHeldFace, false, valueSettingBehaviour.netId()));
            valueSettingBehaviour.onShortInteract(player, interactHeldHand, interactHeldFace, blockHitResult);
            cancelInteraction();
            return;
        }

        if (interactHeldTicks > 3)
            player.swinging = false;
        if (interactHeldTicks++ < 5)
            return;
        ScreenOpener.open(new ValueSettingsScreen2(interactHeldPos,
                valueSettingBehaviour.createBoard(player, blockHitResult), valueSettingBehaviour.getValueSettings(),
                valueSettingBehaviour::newSettingHovered, valueSettingBehaviour.netId()));
        interactHeldTicks = -1;
    }
}
