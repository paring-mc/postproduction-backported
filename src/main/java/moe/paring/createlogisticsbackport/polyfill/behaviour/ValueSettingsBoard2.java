package moe.paring.createlogisticsbackport.polyfill.behaviour;

import net.minecraft.network.chat.Component;

import java.util.List;

public record ValueSettingsBoard2(Component title, int maxValue, int milestoneInterval, List<Component> rows,
                                 ValueSettingsFormatter2 formatter) {
}
