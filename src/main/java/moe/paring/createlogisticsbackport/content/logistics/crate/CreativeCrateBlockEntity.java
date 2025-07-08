package moe.paring.createlogisticsbackport.content.logistics.crate;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.logistics.crate.BottomlessItemHandler;
import com.simibubi.create.content.logistics.crate.CrateBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBoxTransform2;
import moe.paring.createlogisticsbackport.polyfill.behaviour.filtering.FilteringBehaviour2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class CreativeCrateBlockEntity extends CrateBlockEntity {

	public CreativeCrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		inv = new com.simibubi.create.content.logistics.crate.BottomlessItemHandler(filtering::getFilter);
		itemHandler = LazyOptional.of(() -> inv);
	}

	FilteringBehaviour2 filtering;
	LazyOptional<IItemHandler> itemHandler;
	private BottomlessItemHandler inv;

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(filtering = createFilter());
		filtering.setLabel(Lang.translateDirect("logistics.creative_crate.supply"));
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (itemHandler != null)
			itemHandler.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER)
			return itemHandler.cast();
		return super.getCapability(cap, side);
	}

	public FilteringBehaviour2 createFilter() {
		return new FilteringBehaviour2(this, new ValueBoxTransform2() {

			@Override
			public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
				TransformStack.cast(ms)
					.rotateX(90);
			}

			@Override
			public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
				return new Vec3(0.5, 13.5 / 16d, 0.5);
			}

			public float getScale() {
				return super.getScale();
			};

		});
	}

}
