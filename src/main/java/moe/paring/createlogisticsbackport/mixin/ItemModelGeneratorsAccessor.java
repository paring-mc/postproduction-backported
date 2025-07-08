package moe.paring.createlogisticsbackport.mixin;

import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ItemModelGenerators.TrimModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorsAccessor {
	@Accessor("GENERATED_TRIM_MODELS")
	static List<TrimModelData> createlogisticsbackport$getGENERATED_TRIM_MODELS() {
		throw new AssertionError();
	}
}
