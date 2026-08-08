package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.loot.AddLootTableModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ACLootModifierProvider extends GlobalLootModifierProvider {

    public ACLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Archaion.MODID);
    }

    @Override
    protected void start() {
        this.add("ancient_keep_map_in_ancient_city",
                new AddLootTableModifier(
                        new LootItemCondition[]{
                                AllOfCondition.allOf(
                                        AnyOfCondition.anyOf(
                                            LootTableIdCondition.builder(Identifier.withDefaultNamespace("chests/ancient_city")),
                                            LootTableIdCondition.builder(Identifier.withDefaultNamespace("chests/ancient_city_ice_box"))
                                        ),
                                        LootItemRandomChanceCondition.randomChance(0.4F)
                                ).build()
                        },
                        IGlobalLootModifier.DEFAULT_PRIORITY,
                        ACLootTables.ANCIENT_KEEP_MAP
                )
        );
    }
}
