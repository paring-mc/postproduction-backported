package moe.paring.createlogisticsbackport.content.logistics.packagePort.frogport;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class FrogportVisual extends BlockEntityInstance<FrogportBlockEntity> implements DynamicInstance {
    private final ModelData body;
    private ModelData head;
    private final ModelData tongue;
    private final ModelData rig;
    private final ModelData box;

    private final PoseStack basePose = new PoseStack();
    private float lastYaw = Float.NaN;
    private float lastHeadPitch = Float.NaN;
    private float lastTonguePitch = Float.NaN;
    private float lastTongueLength = Float.NaN;
    private boolean lastGoggles = false;

    public FrogportVisual(MaterialManager materialManager, FrogportBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        body = materialManager.defaultCutout()
                .material(Materials.TRANSFORMED)
                .getModel(ExtraPartialModels.FROGPORT_BODY)
                .createInstance();

        head = materialManager.defaultCutout()
                .material(Materials.TRANSFORMED)
                .getModel(ExtraPartialModels.FROGPORT_HEAD)
                .createInstance();

        tongue = materialManager.defaultCutout()
                .material(Materials.TRANSFORMED)
                .getModel(ExtraPartialModels.FROGPORT_TONGUE)
                .createInstance();

        rig = materialManager.defaultCutout()
                .material(Materials.TRANSFORMED)
                .getModel(Blocks.AIR.defaultBlockState())
                .createInstance();

        box = materialManager.defaultCutout()
                .material(Materials.TRANSFORMED)
                .getModel(Blocks.AIR.defaultBlockState())
                .createInstance();

        rig.setEmptyTransform();
        box.setEmptyTransform();

        animate();
    }

    @Override
    public void beginFrame() {
        animate();
    }

    private void animate() {
        var partialTicks = AnimationTickHolder.getPartialTicks();

        updateGoggles();

        float yaw = blockEntity.getYaw();

        float headPitch = 80;
        float tonguePitch = 0;
        float tongueLength = 0;
        float headPitchModifier = 1;

        boolean hasTarget = blockEntity.target != null;
        boolean animating = blockEntity.isAnimationInProgress();
        boolean depositing = blockEntity.currentlyDepositing;

        Vec3 diff = Vec3.ZERO;

        if (hasTarget) {
            diff = blockEntity.target
                    .getExactTargetLocation(blockEntity, blockEntity.getLevel(), blockEntity.getBlockPos())
                    .subtract(0, animating && depositing ? 0 : 0.75, 0)
                    .subtract(Vec3.atCenterOf(blockEntity.getBlockPos()));
            tonguePitch = (float) Mth.atan2(diff.y, diff.multiply(1, 0, 1)
                    .length() + (3 / 16f)) * Mth.RAD_TO_DEG;
            tongueLength = Math.max((float) diff.length(), 1);
            headPitch = Mth.clamp(tonguePitch * 2, 60, 100);
        }

        if (animating) {
            float progress = blockEntity.animationProgress.getValue(partialTicks);
            float scale = 1;
            float itemDistance = 0;

            if (depositing) {
                double modifier = Math.max(0, 1 - Math.pow((progress - 0.25) * 4 - 1, 4));
                itemDistance =
                        (float) Math.max(tongueLength * Math.min(1, (progress - 0.25) * 3), tongueLength * modifier);
                tongueLength *= Math.max(0, 1 - Math.pow((progress * 1.25 - 0.25) * 4 - 1, 4));
                headPitchModifier = (float) Math.max(0, 1 - Math.pow((progress * 1.25) * 2 - 1, 4));
                scale = 0.25f + progress * 3 / 4;

            } else {
                tongueLength *= Math.pow(Math.max(0, 1 - progress * 1.25), 5);
                headPitchModifier = 1 - (float) Math.min(1, Math.max(0, (Math.pow(progress * 1.5, 2) - 0.5) * 2));
                scale = (float) Math.max(0.5, 1 - progress * 1.25);
                itemDistance = tongueLength;
            }

            renderPackage(diff, scale, itemDistance);

        } else {
            tongueLength = 0;
            float anticipation = blockEntity.anticipationProgress.getValue(partialTicks);
            headPitchModifier =
                    anticipation > 0 ? (float) Math.max(0, 1 - Math.pow((anticipation * 1.25) * 2 - 1, 4)) : 0;
            rig.setEmptyTransform();
            rig.setEmptyTransform();
        }

        headPitch *= headPitchModifier;

        headPitch = Math.max(headPitch, blockEntity.manualOpenAnimationProgress.getValue(partialTicks) * 60);
        tongueLength = Math.max(tongueLength, blockEntity.manualOpenAnimationProgress.getValue(partialTicks) * 0.25f);

        if (yaw != lastYaw) {
            body.loadIdentity()
                    .translate(getInstancePosition())
                    .centre()
                    .rotateY(yaw)
                    .unCentre()
                    .markDirty();

            // Save the base pose to avoid recalculating it twice every frame
            basePose.setIdentity();
            var ts = TransformStack.cast(basePose);
            ts.translate(getInstancePosition()).centre().rotateY(yaw).unCentre();

            ts.translate(8 / 16f, 10 / 16f, 11 / 16f);

            // I'm not entirely sure that yaw ever changes
            lastYaw = yaw;

            // Force the head and tongue to update
            lastTonguePitch = Float.NaN;
            lastHeadPitch = Float.NaN;
        }

        if (headPitch != lastHeadPitch) {
            head.setTransform(basePose)
                    .rotateX(headPitch)
                    .translateBack(8 / 16f, 10 / 16f, 11 / 16f)
                    .markDirty();

            lastHeadPitch = headPitch;
        }

        if (tonguePitch != lastTonguePitch || tongueLength != lastTongueLength) {
            tongue.setTransform(basePose)
                    .rotateX(tonguePitch)
                    .scale(1f, 1f, tongueLength / (7 / 16f))
                    .translateBack(8 / 16f, 10 / 16f, 11 / 16f)
                    .markDirty();

            lastTonguePitch = tonguePitch;
            lastTongueLength = tongueLength;
        }
    }

    public void updateGoggles() {
        if (blockEntity.goggles && !lastGoggles) {
            head.delete();
            head = materialManager
                    .defaultCutout().material(Materials.TRANSFORMED)
                    .getModel(ExtraPartialModels.FROGPORT_HEAD_GOGGLES)
                    .createInstance();
            lastHeadPitch = -1;
            updateLight();
            lastGoggles = true;
        }

        if (!blockEntity.goggles && lastGoggles) {
            head.delete();
            head = materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.FROGPORT_HEAD).createInstance();
            lastHeadPitch = -1;
            updateLight();
            lastGoggles = false;
        }
    }

    private void renderPackage(Vec3 diff, float scale, float itemDistance) {
        if (blockEntity.animatedPackage == null || scale < 0.45) {
            rig.setEmptyTransform();
            box.setEmptyTransform();
            return;
        }
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(blockEntity.animatedPackage.getItem());
        if (key == null) {
            rig.setEmptyTransform();
            box.setEmptyTransform();
            return;
        }

        boolean animating = blockEntity.isAnimationInProgress();
        boolean depositing = blockEntity.currentlyDepositing;

        materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.PACKAGES.get(key))
                        .stealInstance(box);

        box.loadIdentity()
                .translate(getInstancePosition())
                .translate(0, 3 / 16f, 0)
                .translate(diff.normalize()
                        .scale(itemDistance)
                        .subtract(0, animating && depositing ? 0.75 : 0, 0))
                .centre()
                .scale(scale)
                .unCentre()
                .markDirty();

        if (!depositing) {
            rig.setEmptyTransform();
            return;
        }

        materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.PACKAGE_RIGGING.get(key))
                        .stealInstance(rig);

        rig.loadIdentity()
                .translate(getInstancePosition())
                .translate(0, 3 / 16f, 0)
                .translate(diff.normalize()
                        .scale(itemDistance)
                        .subtract(0, animating && depositing ? 0.75 : 0, 0))
                .centre()
                .scale(scale)
                .unCentre()
                .markDirty();
    }

//    @Override
//    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
//        consumer.accept(body);
//        consumer.accept(head);
//    }

    @Override
    public void updateLight() {
        relight(pos, body, head, tongue, rig, box);
    }

    @Override
    protected void remove() {
        body.delete();
        head.delete();
        tongue.delete();
        rig.delete();
        box.delete();
    }
}
