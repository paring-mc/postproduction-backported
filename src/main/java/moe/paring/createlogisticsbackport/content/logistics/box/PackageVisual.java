package moe.paring.createlogisticsbackport.content.logistics.box;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.entity.EntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import moe.paring.createlogisticsbackport.polyfill.EntityVisualExtras;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class PackageVisual extends EntityInstance<PackageEntity> implements DynamicInstance {
    public final ModelData instance;

    public PackageVisual(MaterialManager materialManager, PackageEntity entity) {
        super(materialManager, entity);

        ItemStack box = entity.box;
        if (box.isEmpty() || !PackageItem.isPackage(box))
            box = ExtraBlocks.CARDBOARD_BLOCK.asStack();
        PartialModel model = ExtraPartialModels.PACKAGES.get(ForgeRegistries.ITEMS.getKey(box.getItem()));

        instance = materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel(model).createInstance();

        animate();
    }

    @Override
    public void beginFrame() {
        animate();
    }

    private void animate() {
        var partialTick = AnimationTickHolder.getPartialTicks();
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());

        Vec3 pos = PackageVisual.this.entity.position();
        var renderOrigin = renderOrigin();
        var x = (float) (Mth.lerp(partialTick, this.entity.xo, pos.x) - renderOrigin.getX());
        var y = (float) (Mth.lerp(partialTick, this.entity.yo, pos.y) - renderOrigin.getY());
        var z = (float) (Mth.lerp(partialTick, this.entity.zo, pos.z) - renderOrigin.getZ());

        long randomBits = (long) entity.getId() * 31L * 493286711L;
        randomBits = randomBits * randomBits * 4392167121L + randomBits * 98761L;
        float xNudge = (((float) (randomBits >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float yNudge = (((float) (randomBits >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float zNudge = (((float) (randomBits >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        instance.loadIdentity()
                .translate(x - 0.5 + xNudge, y + yNudge, z - 0.5 + zNudge)
                .centre()
                .rotateY(-yaw - 90)
                .unCentre()
                .setBlockLight(EntityVisualExtras.computePackedLight(partialTick, entity, world))
                .markDirty();
    }

    @Override
    protected void remove() {
        instance.delete();
    }
}
