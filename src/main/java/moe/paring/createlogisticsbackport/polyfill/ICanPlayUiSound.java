package moe.paring.createlogisticsbackport.polyfill;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;

public interface ICanPlayUiSound {
    default void playUiSound(SoundEvent sound, float volume, float pitch) {
        Minecraft.getInstance()
                .getSoundManager()
                .play(SimpleSoundInstance.forUI(sound, pitch, volume * 0.25f));
    }
}
