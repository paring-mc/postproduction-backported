package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.foundation.outliner.Outliner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Outliner.class)
public interface OutlinerAccessor {
    @Accessor("outlines")
    Map<Object, Outliner.OutlineEntry> getMutableOutlines();
}
