package moe.paring.createlogisticsbackport.content.logistics.packagerLink;

import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;

import java.util.Comparator;

public class RequestPromise {
	
	public int ticksExisted;
	public BigItemStack promisedStack;
	
	public RequestPromise(BigItemStack promisedStack) {
		this.promisedStack = promisedStack;
		ticksExisted = 0;
	}
	
	public void tick() {
		ticksExisted++;
	}

	public static Comparator<? super RequestPromise> ageComparator() {
		return (i1, i2) -> Integer.compare(i2.ticksExisted, i1.ticksExisted);
	}
	
}
