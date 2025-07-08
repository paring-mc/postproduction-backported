package moe.paring.createlogisticsbackport.content.logistics.packager;

import com.simibubi.create.AllBlocks;
import moe.paring.createlogisticsbackport.api.packager.unpacking.BasinUnpackingHandler;
import moe.paring.createlogisticsbackport.api.packager.unpacking.CrafterUnpackingHandler;
import moe.paring.createlogisticsbackport.api.packager.unpacking.UnpackingHandler;
import moe.paring.createlogisticsbackport.api.packager.unpacking.VoidingUnpackingHandler;

public class AllUnpackingHandlers {
	public static void registerDefaults() {
		UnpackingHandler.REGISTRY.register(AllBlocks.BASIN.get(), BasinUnpackingHandler.INSTANCE);
		UnpackingHandler.REGISTRY.register(AllBlocks.CREATIVE_CRATE.get(), VoidingUnpackingHandler.INSTANCE);
		UnpackingHandler.REGISTRY.register(AllBlocks.MECHANICAL_CRAFTER.get(), CrafterUnpackingHandler.INSTANCE);
	}
}
