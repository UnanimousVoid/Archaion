package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;

public class LODSoundInstance extends AbstractTickableSoundInstance {

    private static final float MUSIC_VOLUME = 1.0F;
    private static final float FADE_STEP = 0.5F / 20.0F;

    private static @Nullable LODSoundInstance current;

    private boolean fadingOut;

    private LODSoundInstance() {
        super(ACSounds.LOD_THEME.get(), SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.volume = MUSIC_VOLUME;
        this.relative = true;
    }

    @Override
    public void tick() {
        if (this.fadingOut) {
            this.volume = Math.max(0.0F, this.volume - FADE_STEP);
            if (this.volume <= 0.0F) {
                this.stop();
                LODSoundInstance.current = null;
            }
        }
    }

    public static void start() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        if (current != null) {
            mc.getSoundManager().stop(current);
            current = null;
        }
        current = new LODSoundInstance();
        mc.getSoundManager().play(current);
    }

    public static void fadeOut() {
        if (current != null) {
            current.fadingOut = true;
        }
    }

    public static boolean isActive() {
        if (current == null) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null && mc.getSoundManager().isActive(current);
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
