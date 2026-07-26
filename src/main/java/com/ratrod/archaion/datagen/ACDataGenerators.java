package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ACLanguageProvider(packOutput, "en_us"));
        generator.addProvider(true, new ACSoundDefinitionsProvider(packOutput));
        generator.addProvider(true, new ACTagProvider.EntityTypesProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACModelProvider(packOutput));

    }
}
