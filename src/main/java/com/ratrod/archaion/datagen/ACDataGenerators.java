package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper efh = event.getExistingFileHelper();

        // CLIENT
        generator.addProvider(true, new ACLanguageProvider(packOutput, "en_us"));
        generator.addProvider(true, new ACSoundDefinitionsProvider(packOutput, efh));

        // SERVER
        ACTagProvider.BlocksProvider blockTagsProvider = new ACTagProvider.BlocksProvider(packOutput, lookupProvider, efh);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ACTagProvider.ItemsProvider(packOutput, lookupProvider, blockTagsProvider, efh));
        generator.addProvider(true, new ACTagProvider.EntityTypesProvider(packOutput, lookupProvider, efh));
        generator.addProvider(true, new ACTagProvider.StructuresProvider(packOutput, lookupProvider, efh));
        generator.addProvider(true, new ACLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ACAdvancementProvider(packOutput, lookupProvider));
    }
}
