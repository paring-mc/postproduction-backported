package moe.paring.createlogisticsbackport.content.kinetics.chainConveyor;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.instancing.GroupInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainConveyorInstance extends SingleRotatingInstance<ChainConveyorBlockEntity> implements DynamicInstance, TickableInstance {

    private final List<ModelData> guards = new ArrayList<>();

    private final HashMap<ResourceLocation, GroupInstance<ModelData>> boxes;
    private final HashMap<ResourceLocation, GroupInstance<ModelData>> rigging;

    public ChainConveyorInstance(MaterialManager materialManager, ChainConveyorBlockEntity blockEntity) {
        super(materialManager, blockEntity);

//		getTransformMaterial().getModel(ExtraPartialModels.PACKAGES);
        // TODO

        boxes = new HashMap<>();
        rigging = new HashMap<>();

//		boxes = new GroupInstance<>(key -> instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(ExtraPartialModels.PACKAGES.get(key))).createInstance());
//		rigging = new GroupInstance<>(key -> instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(ExtraPartialModels.PACKAGE_RIGGING.get(key))).createInstance());
    }

    @Override
    public void init() {
        super.init();

        setupGuards();

        updateLight();
    }

    private Instancer<ModelData> getBoxModel(ResourceLocation location) {
        return materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.PACKAGES.get(location));
    }

    private Instancer<ModelData> getRigModel(ResourceLocation location) {
        return materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.PACKAGE_RIGGING.get(location));
    }

    private void clearBoxModels() {
        boxes.values().forEach(GroupInstance::clear);
        boxes.clear();
    }

    private void clearRigModels() {
        rigging.values().forEach(GroupInstance::clear);
        rigging.clear();
    }

    @Override
    public void update() {
        super.update();

        setupGuards();
    }

    @Override
    public void tick() {
        blockEntity.tickBoxVisuals();
    }

    @Override
    public void beginFrame() {
        var partialTicks = AnimationTickHolder.getPartialTicks();

        clearBoxModels();
        clearRigModels();

        for (ChainConveyorPackage box : blockEntity.loopingPackages)
            setupBoxVisual(blockEntity, box, partialTicks);

        for (Map.Entry<BlockPos, List<ChainConveyorPackage>> entry : blockEntity.travellingPackages.entrySet())
            for (ChainConveyorPackage box : entry.getValue())
                setupBoxVisual(blockEntity, box, partialTicks);
    }

    InstanceData newBoxModel(ResourceLocation location) {
        if (boxes.containsKey(location)) {
            return boxes.get(location).addInstance();
        }

        var group = new GroupInstance<>(getBoxModel(location));
        boxes.put(location, group);

        return group.addInstance();
    }

    InstanceData newRigModel(ResourceLocation location) {
        if (rigging.containsKey(location)) {
            return rigging.get(location).addInstance();
        }

        var group = new GroupInstance<>(getRigModel(location));
        rigging.put(location, group);

        return group.addInstance();
    }

    private void setupBoxVisual(ChainConveyorBlockEntity be, ChainConveyorPackage box, float partialTicks) {
        if (box.worldPosition == null)
            return;
        if (box.item == null || box.item.isEmpty())
            return;

        ChainConveyorPackage.ChainConveyorPackagePhysicsData physicsData = box.physicsData(be.getLevel());
        if (physicsData.prevPos == null)
            return;

        Vec3 position = physicsData.prevPos.lerp(physicsData.pos, partialTicks);
        Vec3 targetPosition = physicsData.prevTargetPos.lerp(physicsData.targetPos, partialTicks);
        float yaw = AngleHelper.angleLerp(partialTicks, physicsData.prevYaw, physicsData.yaw);
        Vec3 offset =
                new Vec3(targetPosition.x - this.pos.getX(), targetPosition.y - this.pos.getY(), targetPosition.z - this.pos.getZ());

        BlockPos containingPos = BlockPos.containing(position);

        if (physicsData.modelKey == null) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(box.item.getItem());
            if (key == null)
                return;
            physicsData.modelKey = key;
        }

        var rigBuffer = (ModelData) newRigModel(physicsData.modelKey);
        var boxBuffer = (ModelData) newBoxModel(physicsData.modelKey);

        Vec3 dangleDiff = VecHelper.rotate(targetPosition.add(0, 0.5, 0)
                .subtract(position), -yaw, Direction.Axis.Y);
        float zRot = Mth.wrapDegrees((float) Mth.atan2(-dangleDiff.x, dangleDiff.y) * Mth.RAD_TO_DEG) / 2;
        float xRot = Mth.wrapDegrees((float) Mth.atan2(dangleDiff.z, dangleDiff.y) * Mth.RAD_TO_DEG) / 2;
        zRot = Mth.clamp(zRot, -25, 25);
        xRot = Mth.clamp(xRot, -25, 25);

        relight(containingPos, rigBuffer, boxBuffer);

        for (ModelData buf : new ModelData[]{rigBuffer, boxBuffer}) {
            buf.loadIdentity();
            buf.translate(getInstancePosition());
            buf.translate(offset);
            buf.translate(0, 10 / 16f, 0);
            buf.rotateY(yaw);

            buf.rotateZ(zRot);
            buf.rotateX(xRot);

            if (physicsData.flipped && buf == rigBuffer)
                buf.rotateY(180);

            buf.unCentre();
            buf.translate(0, -PackageItem.getHookDistance(box.item) + 7 / 16f, 0);

            buf.markDirty();
        }
    }

    private void deleteGuards() {
        for (ModelData guard : guards) {
            guard.delete();
        }
        guards.clear();
    }

    private void setupGuards() {
        deleteGuards();

        var guardInstancer = materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.CHAIN_CONVEYOR_GUARD);

        for (BlockPos blockPos : blockEntity.connections) {
            ChainConveyorBlockEntity.ConnectionStats stats = blockEntity.connectionStats.get(blockPos);
            if (stats == null) {
                continue;
            }

            Vec3 diff = stats.end()
                    .subtract(stats.start());
            double yaw = Mth.RAD_TO_DEG * Mth.atan2(diff.x, diff.z);

            ModelData guard = guardInstancer.createInstance();
            guard.translate(getInstancePosition())
                    .centre()
                    .rotateY((float) yaw)
                    .unCentre()
                    .markDirty();

            relight(pos, guard);

            guards.add(guard);
        }
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return materialManager.defaultCutout().material(AllMaterialSpecs.ROTATING).getModel(ExtraPartialModels.CHAIN_CONVEYOR_SHAFT);
//        return materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(ExtraPartialModels.CHAIN_CONVEYOR_SHAFT);
//		return Models.partial(AllPartialModels.CHAIN_CONVEYOR_SHAFT);
    }


    @Override
    public void updateLight() {
        super.updateLight();
        for (var guard : guards) {
            relight(pos, guard);
        }

        boxes.values().forEach((modelData) -> {
            relight(pos, modelData.stream());
        });
    }

    @Override
    public void remove() {
        super.remove();
        deleteGuards();
        boxes.forEach((resourceLocation, modelData) -> modelData.clear());
        rigging.forEach((resourceLocation, modelData) -> modelData.clear());
        boxes.clear();
        rigging.clear();
    }
}
