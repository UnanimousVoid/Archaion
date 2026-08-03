package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // CLIENT
        generator.addProvider(true, new ACLanguageProvider(packOutput, "en_us"));
        generator.addProvider(true, new ACSoundDefinitionsProvider(packOutput));
        generator.addProvider(true, new ACModelProvider(packOutput));

        // SERVER
        ACTagProvider.BlocksProvider blockTagsProvider = new ACTagProvider.BlocksProvider(packOutput, lookupProvider);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ACTagProvider.ItemsProvider(packOutput, lookupProvider, blockTagsProvider));
        generator.addProvider(true, new ACTagProvider.EntityTypesProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACRecipeProvider.Runner(packOutput, lookupProvider));

        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.TRIAL_SPAWNER_CONFIG, ACTrialSpawnerConfigProvider::bootstrap);

        generator.addProvider(true, new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, registrySetBuilder, Set.of(Archaion.MODID)));
    }
}
