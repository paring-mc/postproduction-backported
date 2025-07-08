package moe.paring.createlogisticsbackport.content.logistics.packager;

import com.simibubi.create.AllBlocks;

public class AllUnpackingHandlers {
	public static void registerDefaults() {
		UnpackingHandler.REGISTRY.register(AllBlocks.BASIN.get(), BasinUnpackingHandler.INSTANCE);
		UnpackingHandler.REGISTRY.register(AllBlocks.CREATIVE_CRATE.get(), VoidingUnpackingHandler.INSTANCE);
		UnpackingHandler.REGISTRY.register(AllBlocks.MECHANICAL_CRAFTER.get(), CrafterUnpackingHandler.INSTANCE);
	}
}
