package moe.paring.createlogisticsbackport.content.logistics.packager;

import moe.paring.createlogisticsbackport.api.packager.InventoryIdentifier;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * An item inventory, possibly with an associated InventoryIdentifier.
 */
public record IdentifiedInventory(@Nullable InventoryIdentifier identifier, IItemHandler handler) {
}
