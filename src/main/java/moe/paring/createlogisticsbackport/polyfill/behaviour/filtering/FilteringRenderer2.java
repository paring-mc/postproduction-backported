package moe.paring.createlogisticsbackport.polyfill.behaviour.filtering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.CreateLogisticsBackportClient;
import moe.paring.createlogisticsbackport.polyfill.OutlinerExtra;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBox2;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBoxTransform2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FilteringRenderer2 {
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        HitResult target = mc.hitResult;
        if (target == null || !(target instanceof BlockHitResult))
            return;

        BlockHitResult result = (BlockHitResult) target;
        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (mc.player.isShiftKeyDown())
            return;
        if (!(world.getBlockEntity(pos) instanceof SmartBlockEntity sbe))
            return;

        ItemStack mainhandItem = mc.player.getItemInHand(InteractionHand.MAIN_HAND);

        for (BlockEntityBehaviour b : sbe.getAllBehaviours()) {
            if (!(b instanceof FilteringBehaviour2 behaviour))
                continue;

            if (behaviour instanceof SidedFilteringBehaviour2) {
                behaviour = ((SidedFilteringBehaviour2) behaviour).get(result.getDirection());
                if (behaviour == null)
                    continue;
            }

            if (!behaviour.isActive())
                continue;
            if (behaviour.slotPositioning instanceof ValueBoxTransform2.Sided)
                ((ValueBoxTransform2.Sided) behaviour.slotPositioning).fromSide(result.getDirection());
            if (!behaviour.slotPositioning.shouldRender(world, pos, state))
                continue;
            if (!behaviour.mayInteract(mc.player))
                continue;

            ItemStack filter = behaviour.getFilter();
            boolean isFilterSlotted = filter.getItem() instanceof FilterItem;
            boolean showCount = behaviour.isCountVisible();
            Component label = behaviour.getLabel();
            boolean hit = behaviour.slotPositioning.testHit(world, pos, state, target.getLocation()
                    .subtract(Vec3.atLowerCornerOf(pos)));

            AABB emptyBB = new AABB(Vec3.ZERO, Vec3.ZERO);
            AABB bb = isFilterSlotted ? emptyBB.inflate(.45f, .31f, .2f) : emptyBB.inflate(.25f);

            ValueBox2 box = new ValueBox2.ItemValueBox(label, bb, pos, filter, behaviour.getCountLabelForValueBox());
            box.passive(!hit || behaviour.bypassesInput(mainhandItem));

            OutlinerExtra
                    .showValueBox2(Pair.of("filter" + behaviour.netId(), pos), box.transform(behaviour.slotPositioning))
                    .lineWidth(1 / 64f)
                    .withFaceTexture(hit ? AllSpecialTextures.THIN_CHECKERED : null)
                    .highlightFace(result.getDirection());

            if (!hit)
                continue;

            List<MutableComponent> tip = new ArrayList<>();
            tip.add(label.copy());
            tip.add(behaviour.getTip());
            if (showCount)
                tip.add(behaviour.getAmountTip());

            CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip);
        }
    }

    public static void renderOnBlockEntity(SmartBlockEntity be, float partialTicks, PoseStack ms,
                                           MultiBufferSource buffer, int light, int overlay) {

        if (be == null || be.isRemoved())
            return;

        Level level = be.getLevel();
        BlockPos blockPos = be.getBlockPos();

        for (BlockEntityBehaviour b : be.getAllBehaviours()) {
            if (!(b instanceof FilteringBehaviour2 behaviour))
                continue;

            if (!be.isVirtual()) {
                Entity cameraEntity = Minecraft.getInstance().cameraEntity;
                if (cameraEntity != null && level == cameraEntity.level()) {
                    float max = behaviour.getRenderDistance();
                    if (cameraEntity.position()
                            .distanceToSqr(VecHelper.getCenterOf(blockPos)) > (max * max)) {
                        continue;
                    }
                }
            }

            if (!behaviour.isActive())
                continue;
            if (behaviour.getFilter()
                    .isEmpty() && !(behaviour instanceof SidedFilteringBehaviour2))
                continue;

            ValueBoxTransform2 slotPositioning = behaviour.slotPositioning;
            BlockState blockState = be.getBlockState();

            if (slotPositioning instanceof ValueBoxTransform2.Sided) {
                ValueBoxTransform2.Sided sided = (ValueBoxTransform2.Sided) slotPositioning;
                Direction side = sided.getSide();
                for (Direction d : Iterate.directions) {
                    ItemStack filter = behaviour.getFilter(d);
                    if (filter.isEmpty())
                        continue;

                    sided.fromSide(d);
                    if (!slotPositioning.shouldRender(level, blockPos, blockState))
                        continue;

                    ms.pushPose();
                    slotPositioning.transform(level, blockPos, blockState, ms);
                    if (AllBlocks.CONTRAPTION_CONTROLS.has(blockState))
                        ValueBoxRenderer.renderFlatItemIntoValueBox(filter, ms, buffer, light, overlay);
                    else
                        ValueBoxRenderer.renderItemIntoValueBox(filter, ms, buffer, light, overlay);
                    ms.popPose();
                }
                sided.fromSide(side);
                continue;
            } else if (slotPositioning.shouldRender(level, blockPos, blockState)) {
                ms.pushPose();
                slotPositioning.transform(level, blockPos, blockState, ms);
                ValueBoxRenderer.renderItemIntoValueBox(behaviour.getFilter(), ms, buffer, light, overlay);
                ms.popPose();
            }
        }
    }


}
