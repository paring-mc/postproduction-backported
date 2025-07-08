package moe.paring.createlogisticsbackport.polyfill.behaviour;

import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public interface ValueSettingsBehaviour2 extends ClipboardCloneable {

    public static record ValueSettings2(int row, int value) {

        public MutableComponent format() {
            return Lang.number(value)
                    .component();
        }

    };

    public boolean testHit(Vec3 hit);

    public boolean isActive();

    default boolean onlyVisibleWithWrench() {
        return false;
    }

    default void newSettingHovered(ValueSettings2 valueSetting) {}

    public ValueBoxTransform2 getSlotPositioning();

    public ValueSettingsBoard2 createBoard(Player player, BlockHitResult hitResult);

    public void setValueSettings(Player player, ValueSettings2 valueSetting, boolean ctrlDown);

    public ValueSettings2 getValueSettings();

    default boolean acceptsValueSettings() {
        return true;
    }

    @Override
    default String getClipboardKey() {
        return "Settings";
    }

    @Override
    default boolean writeToClipboard(CompoundTag tag, Direction side) {
        if (!acceptsValueSettings())
            return false;
        ValueSettings2 valueSettings = getValueSettings();
        tag.putInt("Value", valueSettings.value());
        tag.putInt("Row", valueSettings.row());
        return true;
    }

    @Override
    default boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!acceptsValueSettings())
            return false;
        if (!tag.contains("Value") || !tag.contains("Row"))
            return false;
        if (simulate)
            return true;
        setValueSettings(player, new ValueSettings2(tag.getInt("Row"), tag.getInt("Value")), false);
        return true;
    }

    default void playFeedbackSound(BlockEntityBehaviour origin) {
        origin.getWorld()
                .playSound(null, origin.getPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.25f, 2f);
        origin.getWorld()
                .playSound(null, origin.getPos(), SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.get(), SoundSource.BLOCKS, 0.03f,
                        1.125f);
    }

    default void onShortInteract(Player player, InteractionHand hand, Direction side, BlockHitResult hitResult) {}

    default boolean bypassesInput(ItemStack mainhandItem) {
        return false;
    }

    default boolean mayInteract(Player player) {
        return true;
    }

    default int netId() {
        return 0;
    }

}

