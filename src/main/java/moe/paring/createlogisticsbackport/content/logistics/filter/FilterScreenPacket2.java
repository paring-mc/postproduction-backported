package moe.paring.createlogisticsbackport.content.logistics.filter;

import com.simibubi.create.content.logistics.filter.AttributeFilterMenu;
import com.simibubi.create.content.logistics.filter.AttributeFilterMenu.WhitelistMode;
import com.simibubi.create.content.logistics.filter.FilterMenu;
import com.simibubi.create.content.logistics.filter.ItemAttribute;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import moe.paring.createlogisticsbackport.mixin.AttributeFilterMenuAccessor;
import moe.paring.createlogisticsbackport.mixin.FilterMenuAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class FilterScreenPacket2 extends SimplePacketBase {

    public enum Option {
        WHITELIST, WHITELIST2, BLACKLIST, RESPECT_DATA, IGNORE_DATA, UPDATE_FILTER_ITEM, ADD_TAG, ADD_INVERTED_TAG, UPDATE_ADDRESS;
    }

    private final Option option;
    private final CompoundTag data;

    public FilterScreenPacket2(Option option) {
        this(option, new CompoundTag());
    }

    public FilterScreenPacket2(Option option, CompoundTag data) {
        this.option = option;
        this.data = data;
    }

    public FilterScreenPacket2(FriendlyByteBuf buffer) {
        option = Option.values()[buffer.readInt()];
        data = buffer.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(option.ordinal());
        buffer.writeNbt(data);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;

            if (player.containerMenu instanceof FilterMenu c) {
                var acc = (FilterMenuAccessor) c;
                if (option == Option.WHITELIST)
                    acc.setBlacklist(false);
                if (option == Option.BLACKLIST)
                    acc.setBlacklist(true);
                if (option == Option.RESPECT_DATA)
                    acc.setRespectNBT(true);
                if (option == Option.IGNORE_DATA)
                    acc.setRespectNBT(false);
                if (option == Option.UPDATE_FILTER_ITEM)
                    c.ghostInventory.setStackInSlot(
                            data.getInt("Slot"),
                            net.minecraft.world.item.ItemStack.of(data.getCompound("Item")));
            }

            if (player.containerMenu instanceof AttributeFilterMenu c) {
                if (option == Option.WHITELIST)
                    ((AttributeFilterMenuAccessor) c).setWhitelistMode(WhitelistMode.WHITELIST_DISJ);
                ;
                if (option == Option.WHITELIST2)
                    ((AttributeFilterMenuAccessor) c).setWhitelistMode(WhitelistMode.WHITELIST_CONJ);
                ;
                if (option == Option.BLACKLIST)
                    ((AttributeFilterMenuAccessor) c).setWhitelistMode(WhitelistMode.BLACKLIST);
                ;
                if (option == Option.ADD_TAG)
                    c.appendSelectedAttribute(ItemAttribute.fromNBT(data), false);
                if (option == Option.ADD_INVERTED_TAG)
                    c.appendSelectedAttribute(ItemAttribute.fromNBT(data), true);
            }

            if (player.containerMenu instanceof PackageFilterMenu c) {
                if (option == Option.UPDATE_ADDRESS)
                    c.address = data.getString("Address");
            }

        });
        return true;
    }

}
