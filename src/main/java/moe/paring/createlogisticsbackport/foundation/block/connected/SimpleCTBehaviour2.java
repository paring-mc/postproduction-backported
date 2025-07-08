package moe.paring.createlogisticsbackport.foundation.block.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleCTBehaviour2 extends ConnectedTextureBehaviour2.Base {

	protected CTSpriteShiftEntry2 shift;

	public SimpleCTBehaviour2(CTSpriteShiftEntry2 shift) {
		this.shift = shift;
	}

	@Override
	public CTSpriteShiftEntry2 getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		return shift;
	}

}
