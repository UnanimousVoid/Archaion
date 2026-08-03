package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ACSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ACSoundDefinitionsProvider(PackOutput output) {
        super(output, Archaion.MODID);
    }

    @Override
    public void registerSounds() {
        for (DeferredHolder<SoundEvent, ? extends SoundEvent> sound : ACSounds.SOUND_EVENT.getEntries()) {
            add(sound.value(), definition()
                    .with(sound(sound.getId()))
                    .subtitle("subtitle." + Archaion.MODID + "." + sound.getId().getPath()));
        }
    }
}
