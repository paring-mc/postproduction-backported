package moe.paring.createlogisticsbackport.mixin;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockLootSubProvider.class)
public interface BlockLootSubProviderAccessor {
	@Accessor("HAS_SILK_TOUCH")
	static LootItemCondition.Builder create$HAS_SILK_TOUCH() {
		throw new AssertionError();
	}
}
