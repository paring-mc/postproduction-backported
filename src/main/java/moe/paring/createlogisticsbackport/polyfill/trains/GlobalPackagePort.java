package moe.paring.createlogisticsbackport.polyfill.trains;

import net.minecraftforge.items.ItemStackHandler;

public class GlobalPackagePort {
    public String address = "";
    public ItemStackHandler offlineBuffer = new ItemStackHandler(18);
    public boolean primed = false;
}
