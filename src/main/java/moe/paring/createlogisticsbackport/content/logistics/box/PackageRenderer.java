package moe.paring.createlogisticsbackport.content.logistics.box;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import moe.paring.createlogisticsbackport.registry.ExtraPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class PackageRenderer extends EntityRenderer<PackageEntity> {

	public PackageRenderer(Context pContext) {
		super(pContext);
		shadowRadius = 0.5f;
	}

	@Override
	public void render(PackageEntity entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light) {
		if (!VisualizationManager.supportsVisualization(entity.level())) {
			ItemStack box = entity.box;
			if (box.isEmpty() || !PackageItem.isPackage(box)) box = ExtraBlocks.CARDBOARD_BLOCK.asStack();
			PartialModel model = ExtraPartialModels.PACKAGES.get(ForgeRegistries.ITEMS.getKey(box.getItem()));
			renderBox(entity, yaw, ms, buffer, light, model);
		}
		super.render(entity, yaw, pt, ms, buffer, light);
	}

	public static void renderBox(Entity entity, float yaw, PoseStack ms, MultiBufferSource buffer, int light,
		PartialModel model) {
		if (model == null)
			return;
		SuperByteBuffer sbb = CachedBufferer.partial(model, Blocks.AIR.defaultBlockState());
		sbb.translate(-.5, 0, -.5)
			.rotateCentered(Direction.UP, -AngleHelper.rad(yaw + 90))
			.light(light)
			.nudge(entity.getId());
		sbb.renderInto(ms, buffer.getBuffer(RenderType.solid()));
	}

	@Override
	public ResourceLocation getTextureLocation(PackageEntity pEntity) {
		return null;
	}

}
