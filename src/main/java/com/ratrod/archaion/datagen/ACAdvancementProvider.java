package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACStructures;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ACAdvancementProvider extends AdvancementProvider {

    public ACAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new ACAdvancements()));
    }

    public static class ACAdvancements implements AdvancementSubProvider {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
            HolderGetter<EntityType<?>> entities = registries.lookupOrThrow(Registries.ENTITY_TYPE);

            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(ACBlocks.REINFORCED_DEEPSLATE_BRICKS.get(), Component.translatable("advancements.root.title"), Component.translatable("advancements.root.description"), Archaion.prefix("block/reinforced_deepslate_bricks"), AdvancementType.TASK, true, false, false)
                    .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                    .save(writer, Archaion.prefix("root").toString());

            AdvancementHolder obtainMap = Advancement.Builder.advancement()
                    .parent(root)
                    .display(Items.FILLED_MAP, Component.translatable("advancements.obtain_map.title"), Component.translatable("advancements.obtain_map.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("get_map", LootTableTrigger.TriggerInstance.lootTableUsed(ACLootTables.ANCIENT_KEEP_MAP))
                    .save(writer, Archaion.prefix("obtain_map").toString());

            AdvancementHolder enterAncientKeep = Advancement.Builder.advancement()
                    .parent(obtainMap)
                    .display(ACBlocks.REINFORCED_DEEPSLATE_BRICKS.get(), Component.translatable("advancements.enter_ancient_keep.title"), Component.translatable("advancements.enter_ancient_keep.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("enter_ancient_keep", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructures(registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(ACStructures.ON_ANCIENT_KEEP_MAPS))))
                    .save(writer, Archaion.prefix("enter_ancient_keep").toString());

            AdvancementHolder obtainBraveRod = Advancement.Builder.advancement()
                    .parent(enterAncientKeep)
                    .display(ACItems.BRAVE_ROD.get(), Component.translatable("advancements.obtain_brave_rod.title"), Component.translatable("advancements.obtain_brave_rod.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_brave_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.BRAVE_ROD.get()))
                    .rewards(AdvancementRewards.Builder.recipe(recipe("brave_essence")))
                    .save(writer, Archaion.prefix("obtain_brave_rod").toString());

            AdvancementHolder craftImpactPearl = Advancement.Builder.advancement()
                    .parent(obtainBraveRod)
                    .display(ACItems.IMPACT_PEARL.get(), Component.translatable("advancements.craft_impact_pearl.title"), Component.translatable("advancements.craft_impact_pearl.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_impact_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.IMPACT_PEARL.get()))
                    .save(writer, Archaion.prefix("craft_impact_pearl").toString());

            AdvancementHolder obtainEchoKey = Advancement.Builder.advancement()
                    .parent(enterAncientKeep)
                    .display(ACItems.ECHO_KEY.get(), Component.translatable("advancements.obtain_echo_key.title"), Component.translatable("advancements.obtain_echo_key.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_echo_key", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.ECHO_KEY.get()))
                    .save(writer, Archaion.prefix("obtain_echo_key").toString());

            AdvancementHolder openDeepslateVault = Advancement.Builder.advancement()
                    .parent(obtainEchoKey)
                    .display(ACItems.ECHO_CHARGE.get(), Component.translatable("advancements.open_deepslate_vault.title"), Component.translatable("advancements.open_deepslate_vault.description"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_echo_charge", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.ECHO_CHARGE.get()))
                    .save(writer, Archaion.prefix("open_deepslate_vault").toString());

            AdvancementHolder craftEchosGrace = Advancement.Builder.advancement()
                    .parent(openDeepslateVault)
                    .display(ACItems.ECHOS_GRACE.get(), Component.translatable("advancements.craft_echos_grace.title"), Component.translatable("advancements.craft_echos_grace.description"), null, AdvancementType.GOAL, true, true, false)
                    .addCriterion("has_echos_grace", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.ECHOS_GRACE.get()))
                    .rewards(AdvancementRewards.Builder.recipe(recipe("echos_grace_upgrade_smithing")))
                    .save(writer, Archaion.prefix("craft_echos_grace").toString());

            AdvancementHolder killLastOfDeepslate = Advancement.Builder.advancement()
                    .parent(openDeepslateVault)
                    .display(ACItems.LAST_OF_DEEPSLATE_SPAWN_EGG.get(), Component.translatable("advancements.kill_last_of_deepslate.title"), Component.translatable("advancements.kill_last_of_deepslate.description"), null, AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("kill_last_of_deepslate", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entities, ACEntityTypes.LAST_OF_DEEPSLATE.get())))
                    .rewards(AdvancementRewards.Builder.experience(100).addRecipe(recipe("echo_mace_upgrade_smithing")))
                    .save(writer, Archaion.prefix("kill_last_of_deepslate").toString());

            AdvancementHolder craftEchoMace = Advancement.Builder.advancement()
                    .parent(killLastOfDeepslate)
                    .display(ACItems.ECHO_MACE.get(), Component.translatable("advancements.craft_echo_mace.title"), Component.translatable("advancements.craft_echo_mace.description"), null, AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("has_echo_mace", InventoryChangeTrigger.TriggerInstance.hasItems(ACItems.ECHO_MACE.get()))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(writer, Archaion.prefix("craft_echo_mace").toString());
        }

        private static ResourceKey<Recipe<?>> recipe(String name) {
            return ResourceKey.create(Registries.RECIPE, Archaion.prefix(name));
        }
    }
}
