package moe.paring.createlogisticsbackport.polyfill.behaviour;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import moe.paring.createlogisticsbackport.content.logistics.stockTicker.StockTickerBlockEntity;
import moe.paring.createlogisticsbackport.registry.ExtraBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlazeBurnerStockKeeperBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<BlazeBurnerStockKeeperBehaviour> TYPE = new BehaviourType<>();

    public boolean stockKeeper;

    public BlazeBurnerStockKeeperBehaviour(SmartBlockEntity be) {
        super(be);

        stockKeeper = false;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        stockKeeper = getStockTicker(blockEntity.getLevel(), blockEntity.worldPosition) != null;
    }

    @Nullable
    public static StockTickerBlockEntity getStockTicker(LevelAccessor level, BlockPos pos) {
        for (Direction direction : Iterate.horizontalDirections) {
            if (level instanceof Level l && !l.isLoaded(pos))
                return null;
            BlockState blockState = level.getBlockState(pos.relative(direction));
            if (!ExtraBlocks.STOCK_TICKER.has(blockState))
                continue;
            if (level.getBlockEntity(pos.relative(direction)) instanceof StockTickerBlockEntity stbe)
                return stbe;
        }
        return null;
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock() {
        return BlazeBurnerBlock.getHeatLevelOf(blockEntity.getBlockState());
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevelForRender() {
        BlazeBurnerBlock.HeatLevel heatLevel = getHeatLevelFromBlock();
        if (!heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) && stockKeeper)
            return BlazeBurnerBlock.HeatLevel.FADING;
        return heatLevel;
    }
}
