package moe.paring.createlogisticsbackport.polyfill;

import moe.paring.createlogisticsbackport.content.logistics.box.PackageEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class ItemHelper {
    public static ItemStack fromItemEntity(Entity entityIn) {
        if (!entityIn.isAlive())
            return ItemStack.EMPTY;
        if (entityIn instanceof PackageEntity packageEntity) {
            return packageEntity.getBox();
        }
//        return entityIn instanceof ItemEntity itemEntity ? itemEntity.getItem() : ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }
}

