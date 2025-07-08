package moe.paring.createlogisticsbackport.foundation.block.connected;

import moe.paring.createlogisticsbackport.foundation.block.connected.ConnectedTextureBehaviour2.CTContext;
import moe.paring.createlogisticsbackport.foundation.block.connected.ConnectedTextureBehaviour2.ContextRequirement;
import net.minecraft.resources.ResourceLocation;

public interface CTType2 {
	ResourceLocation getId();

	int getSheetSize();

	ContextRequirement getContextRequirement();

	int getTextureIndex(CTContext context);
}
