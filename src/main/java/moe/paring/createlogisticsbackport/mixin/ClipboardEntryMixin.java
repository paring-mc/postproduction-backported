package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import moe.paring.createlogisticsbackport.accessor.ClipboardEntryMixinAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ClipboardEntry.class, remap = false)
public abstract class ClipboardEntryMixin implements ClipboardEntryMixinAccessor {
    @Unique
    private int create_logistics_backport$itemAmount;

    @Unique
    public void create_logistics_backport$displayItem(ItemStack icon, int amount) {
        var orig = (ClipboardEntry) (Object) this;
        orig.icon = icon;
        create_logistics_backport$itemAmount = amount;
    }

    @Inject(method = "writeNBT", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void create_logistics_backport$writeNBT(CallbackInfoReturnable<CompoundTag> cir, CompoundTag nbt) {
        nbt.putInt("ItemAmount", create_logistics_backport$itemAmount);
    }

    @Inject(method = "readNBT", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/equipment/clipboard/ClipboardEntry;displayItem(Lnet/minecraft/world/item/ItemStack;)Lcom/simibubi/create/content/equipment/clipboard/ClipboardEntry;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void readNBT(CompoundTag tag, CallbackInfoReturnable<ClipboardEntry> cir, @NonNull ClipboardEntry clipboardEntry) {
        var e = (ClipboardEntryMixin) (Object) clipboardEntry;
        e.create_logistics_backport$displayItem(ItemStack.of(tag.getCompound("Icon")), tag.getInt("ItemAmount"));
    }

    // TODO: MaterialChecklist displayItem
}
