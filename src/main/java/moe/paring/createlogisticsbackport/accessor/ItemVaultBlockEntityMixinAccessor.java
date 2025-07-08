package moe.paring.createlogisticsbackport.accessor;

import moe.paring.createlogisticsbackport.api.packager.InventoryIdentifier;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface ItemVaultBlockEntityMixinAccessor {
    InventoryIdentifier create_logistics_backport$getInventoryIdentifier();
}
