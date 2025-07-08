package moe.paring.createlogisticsbackport.accessor;

import moe.paring.createlogisticsbackport.content.logistics.packagePort.PackagePortBlockEntity;

public interface StationBlockEntityMixinAccessor {
    void create_logistics_backport$attachPackagePort(PackagePortBlockEntity ppbe);
    void create_logistics_backport$removePackagePort(PackagePortBlockEntity ppbe);
}
