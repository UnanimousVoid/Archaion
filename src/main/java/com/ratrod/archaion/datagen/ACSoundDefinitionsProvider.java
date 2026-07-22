package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ACSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ACSoundDefinitionsProvider(PackOutput output) {
        super(output, Archaion.MODID);
    }

    @Override
    public void registerSounds() {

    }
}
