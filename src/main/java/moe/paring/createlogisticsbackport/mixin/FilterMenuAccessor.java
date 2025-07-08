package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.logistics.filter.FilterMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FilterMenu.class)
public interface FilterMenuAccessor {
    @Accessor("blacklist")
    void setBlacklist(boolean blacklist);

    @Accessor("respectNBT")
    void setRespectNBT(boolean respectNBT);
}
