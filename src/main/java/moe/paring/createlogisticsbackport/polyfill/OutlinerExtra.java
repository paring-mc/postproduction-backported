package moe.paring.createlogisticsbackport.polyfill;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.outliner.Outline;
import com.simibubi.create.foundation.outliner.Outliner;
import moe.paring.createlogisticsbackport.mixin.OutlinerAccessor;
import moe.paring.createlogisticsbackport.polyfill.behaviour.ValueBox2;

public class OutlinerExtra {
    public static Outline.OutlineParams showValueBox2(Object slot, ValueBox2 box) {
        var outliner = (OutlinerAccessor)CreateClient.OUTLINER;
        outliner.getMutableOutlines().put(slot, new Outliner.OutlineEntry(box));
        return box.getParams();
    }
}
