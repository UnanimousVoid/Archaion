package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ACSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public ACSoundDefinitionsProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, Archaion.MODID, efh);
    }

    @Override
    public void registerSounds() {
        for (DeferredHolder<SoundEvent, ? extends SoundEvent> sound : ACSounds.SOUND_EVENT.getEntries()) {

            SoundDefinition.Sound definition = sound(sound.getId());

            if (sound.getId().getPath().startsWith("lod_boss_theme_phase")) {
                definition = definition.stream();
            }

            add(sound.value(), definition().with(definition).subtitle("subtitle." + Archaion.MODID + "." + sound.getId().getPath()));
        }
    }
}
