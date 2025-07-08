package moe.paring.createlogisticsbackport.content.logistics.packager;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PackagerVisual<T extends PackagerBlockEntity> extends BlockEntityInstance<T> implements DynamicInstance {
    public final ModelData hatch;
    public final ModelData tray;

    public float lastTrayOffset = Float.NaN;
    public PartialModel lastHatchPartial;


    public PackagerVisual(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);

        lastHatchPartial = PackagerRenderer.getHatchModel(blockEntity);
        hatch = materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(lastHatchPartial).createInstance();
        tray = materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(PackagerRenderer.getTrayModel(blockEntity.getBlockState())).createInstance();

        Direction facing = blockState.getValue(PackagerBlock.FACING)
                .getOpposite();

        var lowerCorner = Vec3.atLowerCornerOf(facing.getNormal());
        hatch.loadIdentity()
                .translate(getInstancePosition())
                .translate(lowerCorner
                        .scale(.49999f))
                .centre()
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing))
                .unCentre()
                .markDirty();

        // TODO: I think we need proper ItemVisuals to handle rendering the boxes in here

        animate();
    }

    @Override
    public void beginFrame() {
        animate();
    }

    public void animate() {
        var partialTick = AnimationTickHolder.getPartialTicks();
        var hatchPartial = PackagerRenderer.getHatchModel(blockEntity);

        if (hatchPartial != this.lastHatchPartial) {
            materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(hatchPartial).stealInstance(hatch);

            this.lastHatchPartial = hatchPartial;
        }

        float trayOffset = blockEntity.getTrayOffset(partialTick);

        if (trayOffset != lastTrayOffset) {
            Direction facing = blockState.getValue(PackagerBlock.FACING)
                    .getOpposite();

            var lowerCorner = Vec3.atLowerCornerOf(facing.getNormal());

            tray.loadIdentity()
                    .translate(getInstancePosition())
                    .translate(lowerCorner.scale(trayOffset))
                    .centre()
                    .rotateY(facing.toYRot())
                    .unCentre()
                    .markDirty();

            lastTrayOffset = trayOffset;
        }
    }

    @Override
    public void updateLight() {
        relight(pos, hatch, tray);
    }

    @Override
    protected void remove() {
        hatch.delete();
        tray.delete();
    }
}
