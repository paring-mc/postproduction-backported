package moe.paring.createlogisticsbackport.polyfill.behaviour;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Function;

public class ValueSettingsFormatter2 {
    private Function<ValueSettingsBehaviour2.ValueSettings2, MutableComponent> formatter;

    public ValueSettingsFormatter2(Function<ValueSettingsBehaviour2.ValueSettings2, MutableComponent> formatter) {
        this.formatter = formatter;
    }

    public MutableComponent format(ValueSettingsBehaviour2.ValueSettings2 valueSettings) {
        return formatter.apply(valueSettings);
    }

    public static class ScrollOptionSettingsFormatter extends ValueSettingsFormatter2 {

        private INamedIconOptions[] options;

        public ScrollOptionSettingsFormatter(INamedIconOptions[] options) {
            super(v -> Lang.translateDirect(options[v.value()].getTranslationKey()));
            this.options = options;
        }

        public AllIcons getIcon(ValueSettingsBehaviour2.ValueSettings2 valueSettings) {
            return options[valueSettings.value()].getIcon();
        }

    }
}
