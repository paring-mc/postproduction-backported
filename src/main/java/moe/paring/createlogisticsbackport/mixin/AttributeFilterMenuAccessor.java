package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.filter.AttributeFilterMenu;
import com.simibubi.create.content.logistics.filter.AttributeFilterMenu.WhitelistMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeFilterMenu.class)
public interface AttributeFilterMenuAccessor {
    @Accessor("whitelistMode")
    void setWhitelistMode(WhitelistMode whitelistMode);
}
