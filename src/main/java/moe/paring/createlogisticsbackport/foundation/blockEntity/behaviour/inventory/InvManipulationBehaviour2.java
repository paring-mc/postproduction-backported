package moe.paring.createlogisticsbackport.foundation.blockEntity.behaviour.inventory;

import com.google.common.base.Predicates;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.CapManipulationBehaviourBase;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import moe.paring.createlogisticsbackport.api.packager.InventoryIdentifier;
import moe.paring.createlogisticsbackport.content.logistics.packager.IdentifiedInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class InvManipulationBehaviour2 extends CapManipulationBehaviourBase<IItemHandler, InvManipulationBehaviour2> {
    protected com.google.common.base.Predicate<BlockEntity> filter;

    // Extra types available for multibehaviour
    public static final BehaviourType<InvManipulationBehaviour2>

            TYPE = new BehaviourType<>(), EXTRACT = new BehaviourType<>(), INSERT = new BehaviourType<>();

    private BehaviourType<InvManipulationBehaviour2> behaviourType;

    public static InvManipulationBehaviour2 forExtraction(SmartBlockEntity be, InterfaceProvider target) {
        return new InvManipulationBehaviour2(EXTRACT, be, target);
    }

    public static InvManipulationBehaviour2 forInsertion(SmartBlockEntity be, InterfaceProvider target) {
        return new InvManipulationBehaviour2(INSERT, be, target);
    }

    public InvManipulationBehaviour2(SmartBlockEntity be, InterfaceProvider target) {
        this(TYPE, be, target);
    }

    private InvManipulationBehaviour2(BehaviourType<InvManipulationBehaviour2> type, SmartBlockEntity be,
                                      InterfaceProvider target) {
        super(be, target);
        behaviourType = type;
    }

    @Nullable
    public IdentifiedInventory getIdentifiedInventory() {
        IItemHandler inventory = this.getInventory();
        if (inventory == null)
            return null;

        InventoryIdentifier identifier = InventoryIdentifier.get(this.getWorld(), this.getTarget().getOpposite());
        return new IdentifiedInventory(identifier, inventory);
    }

    public BlockFace getTarget() {
        return this.target.getTarget(this.getWorld(), this.blockEntity.getBlockPos(), this.blockEntity.getBlockState());
    }

    @Override
    protected Capability<IItemHandler> capability() {
        return ForgeCapabilities.ITEM_HANDLER;
    }

    public ItemStack extract() {
        return extract(getModeFromFilter(), getAmountFromFilter());
    }

    public ItemStack extract(ItemHelper.ExtractionCountMode mode, int amount) {
        return extract(mode, amount, Predicates.alwaysTrue());
    }

    public InvManipulationBehaviour2 withFilter(com.google.common.base.Predicate<BlockEntity> filter) {
        this.filter = filter;
        return this;
    }

    public ItemStack extract(ItemHelper.ExtractionCountMode mode, int amount, Predicate<ItemStack> filter) {
        boolean shouldSimulate = simulateNext;
        simulateNext = false;

        if (getWorld().isClientSide)
            return ItemStack.EMPTY;
        IItemHandler inventory = targetCapability.orElse(null);
        if (inventory == null)
            return ItemStack.EMPTY;

        Predicate<ItemStack> test = getFilterTest(filter);
        ItemStack simulatedItems = ItemHelper.extract(inventory, test, mode, amount, true);
        if (shouldSimulate || simulatedItems.isEmpty())
            return simulatedItems;
        return ItemHelper.extract(inventory, test, mode, amount, false);
    }

    public ItemStack insert(ItemStack stack) {
        boolean shouldSimulate = simulateNext;
        simulateNext = false;
        IItemHandler inventory = targetCapability.orElse(null);
        if (inventory == null)
            return stack;
        return ItemHandlerHelper.insertItemStacked(inventory, stack, shouldSimulate);
    }

    protected Predicate<ItemStack> getFilterTest(Predicate<ItemStack> customFilter) {
        Predicate<ItemStack> test = customFilter;
        FilteringBehaviour filter = blockEntity.getBehaviour(FilteringBehaviour.TYPE);
        if (filter != null)
            test = customFilter.and(filter::test);
        return test;
    }

    @Override
    public BehaviourType<?> getType() {
        return behaviourType;
    }

}

