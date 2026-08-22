package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.misc.LODTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class LODSoundInstance extends AbstractTickableSoundInstance {

    private static final float MUSIC_VOLUME = 1.0F;
    private static final float FADE_STEP = 1.0F / 40.0F;

    private static @Nullable LODSoundInstance current;
    private static @Nullable Level startedLevel;
    private static @Nullable LODTheme currentTheme;

    private boolean fadingOut;

    private LODSoundInstance(SoundEvent sound) {
        super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = MUSIC_VOLUME;
        this.relative = true;
    }

    @Override
    public void tick() {
        if (!this.fadingOut) {
            return;
        }
        this.volume = Math.max(0.0F, this.volume - FADE_STEP);
        if (this.volume <= 0.0F) {
            this.stop();
            current = null;
        }
    }

    public static void startPhase(LODTheme theme) {
        currentTheme = theme;
        playTheme(theme.sound());
    }

    public static void fadeOut() {
        if (current != null) {
            current.fadingOut = true;
        }
    }

    public static void tryToRepair() {
        if (current == null || current.fadingOut || currentTheme == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level != startedLevel) {
            return;
        }
        if (!mc.getSoundManager().isActive(current)) {
            startPhase(currentTheme);
        }
    }

    public static boolean isActive() {
        if (current == null) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null && mc.getSoundManager().isActive(current);
    }

    private static void playTheme(SoundEvent sound) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        stopCurrent(mc);
        current = new LODSoundInstance(sound);
        startedLevel = mc.level;
        mc.getSoundManager().play(current);
    }

    private static void stopCurrent(Minecraft mc) {
        if (current != null) {
            mc.getSoundManager().stop(current);
            current = null;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
