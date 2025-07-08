package moe.paring.createlogisticsbackport.content.decoration.palettes;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import moe.paring.createlogisticsbackport.foundation.block.connected.AllCTTypes2;
import moe.paring.createlogisticsbackport.foundation.block.connected.CTSpriteShiftEntry2;
import moe.paring.createlogisticsbackport.foundation.block.connected.CTType2;
import moe.paring.createlogisticsbackport.foundation.block.connected.GlassPaneCTBehaviour2;
import moe.paring.createlogisticsbackport.registry.ExtraSpriteShifts;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WeatheredIronWindowPaneCTBehaviour extends GlassPaneCTBehaviour2 {

    private List<CTSpriteShiftEntry2> shifts;

    public WeatheredIronWindowPaneCTBehaviour() {
        super(null);
        this.shifts = List.of(ExtraSpriteShifts.OLD_FACTORY_WINDOW_1, ExtraSpriteShifts.OLD_FACTORY_WINDOW_2,
                ExtraSpriteShifts.OLD_FACTORY_WINDOW_3, ExtraSpriteShifts.OLD_FACTORY_WINDOW_4);
    }

    @Override
    public @Nullable CTSpriteShiftEntry2 getShift(BlockState state, RandomSource rand, Direction direction,
                                                 @NotNull TextureAtlasSprite sprite) {
        if (direction.getAxis() == Axis.Y || sprite == null)
            return null;
        CTSpriteShiftEntry2 entry = shifts.get(rand.nextInt(shifts.size()));
        if (entry.getOriginal() == sprite)
            return entry;
        return super.getShift(state, rand, direction, sprite);
    }

    @Override
    public @Nullable CTSpriteShiftEntry2 getShift(BlockState state, Direction direction,
                                                  @Nullable TextureAtlasSprite sprite) {
        return null;
    }

    @Override
    public @Nullable CTType2 getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        return AllCTTypes2.RECTANGLE;
    }

}
