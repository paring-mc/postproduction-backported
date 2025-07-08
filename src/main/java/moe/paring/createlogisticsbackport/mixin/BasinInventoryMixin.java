package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.processing.basin.BasinInventory;
import moe.paring.createlogisticsbackport.accessor.BasinInventoryMixinAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BasinInventory.class)
public class BasinInventoryMixin implements BasinInventoryMixinAccessor {
    @Unique
    private boolean create_logistics_backport$packagerMode;

    @Override
    public boolean getCreate_logistics_backport$packagerMode() {
        return create_logistics_backport$packagerMode;
    }

    @Override
    public void setCreate_logistics_backport$packagerMode(boolean create_logistics_backport$packagerMode) {
        this.create_logistics_backport$packagerMode = create_logistics_backport$packagerMode;
    }
}
