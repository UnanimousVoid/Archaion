package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.registry.ACItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record ACChestLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

        HolderLookup.RegistryLookup<TrimPattern> trimPatterns = this.registries.lookupOrThrow(Registries.TRIM_PATTERN);
        HolderLookup.RegistryLookup<TrimMaterial> trimMaterials = this.registries.lookupOrThrow(Registries.TRIM_MATERIAL);
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        ArmorTrim flowTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.AMETHYST), trimPatterns.getOrThrow(TrimPatterns.FLOW));
        ArmorTrim boltTrim = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.GOLD), trimPatterns.getOrThrow(TrimPatterns.BOLT));
        ArmorTrim silenceGold = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.GOLD), trimPatterns.getOrThrow(TrimPatterns.SILENCE));
        ArmorTrim silenceAmethyst = new ArmorTrim(trimMaterials.getOrThrow(TrimMaterials.AMETHYST), trimPatterns.getOrThrow(TrimPatterns.SILENCE));

        // DEEPSLATE VAULT DROP
        output.accept(ACLootTables.DEEPSLATE_VAULT, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                )
        );

        // DEEPSLATE SPAWNER AIR THROWABLES
        output.accept(ACLootTables.DEEPSLATE_SPAWNER_THROWABLES, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.SPLASH_POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        )
                        .add(LootItem.lootTableItem(Items.SPLASH_POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.HARMING))
                        )
                        .add(LootItem.lootTableItem(Items.SPLASH_POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.WIND_CHARGED))
                        )
                        .add(LootItem.lootTableItem(Items.LINGERING_POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.POISON))
                        )
                )
        );

        // DEEPSLATE SPAWNER DROPS
        output.accept(ACLootTables.DEEPSLATE_SPAWNER_MISC, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.GOLDEN_CARROT)
                                .setWeight(4)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE)
                                .setWeight(3)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.TURTLE_MASTER))
                        )
                        .add(LootItem.lootTableItem(Items.POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.LONG_REGENERATION))
                        )
                        .add(LootItem.lootTableItem(Items.SPLASH_POTION)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING))
                        )
                )
        );

        output.accept(ACLootTables.DEEPSLATE_SPAWNER_KEY, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ACItems.ECHO_KEY))
                )
        );

        // DEEPSLATE SPAWNER MOBS
        output.accept(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(NestedLootTable.inlineLootTable(deepslateKeepEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_BOOTS, silenceAmethyst, enchantments).build())
                                .setWeight(2)
                        )
                        .add(NestedLootTable.inlineLootTable(deepslateKeepEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_BOOTS, silenceGold, enchantments).build())
                                .setWeight(2)
                        )
                        .add(NestedLootTable.inlineLootTable(deepslateKeepEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_BOOTS, flowTrim, enchantments).build())
                                .setWeight(2)
                        )
                        .add(NestedLootTable.inlineLootTable(deepslateKeepEquipment(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_BOOTS, boltTrim, enchantments).build())
                                .setWeight(2)
                        )
                )
        );

        output.accept(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_RANGED, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(NestedLootTable.lootTableReference(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BOW)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.POWER), UniformGenerator.between(2.0F, 4.0F))
                                )
                        )
                        .add(LootItem.lootTableItem(Items.BOW)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.PUNCH), ConstantValue.exactly(2.0F))
                                )
                        )
                )
        );

        output.accept(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_MELEE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(NestedLootTable.lootTableReference(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.IRON_SWORD)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), UniformGenerator.between(3.0F, 5.0F))
                                )
                        )
                        .add(LootItem.lootTableItem(Items.IRON_AXE)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), UniformGenerator.between(2.0F, 4.0F))
                                )
                        )
                        .add(LootItem.lootTableItem(Items.DIAMOND_SWORD)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), UniformGenerator.between(2.0F, 4.0F))
                                )
                        )
                        .add(LootItem.lootTableItem(Items.DIAMOND_AXE)
                                .apply(new SetEnchantmentsFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), UniformGenerator.between(2.0F, 4.0F))
                                )
                        )
                )
        );

    }

    public static LootTable.Builder deepslateKeepEquipment(Item helmet, Item chestplate, Item boots, ArmorTrim trim, HolderLookup.RegistryLookup<Enchantment> enchantments) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.75F))
                        .add(LootItem.lootTableItem(helmet)
                                .apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim))
                                .apply(deepslateKeepEnchantments(enchantments))
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.75F))
                        .add(LootItem.lootTableItem(chestplate)
                                .apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim))
                                .apply(deepslateKeepEnchantments(enchantments))
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.75F))
                        .add(LootItem.lootTableItem(boots)
                                .apply(SetComponentsFunction.setComponent(DataComponents.TRIM, trim))
                                .apply(deepslateKeepEnchantments(enchantments))
                        )
                );
    }

    private static SetEnchantmentsFunction.Builder deepslateKeepEnchantments(HolderLookup.RegistryLookup<Enchantment> enchantments) {
        return new SetEnchantmentsFunction.Builder()
                .withEnchantment(enchantments.getOrThrow(Enchantments.PROTECTION), UniformGenerator.between(3.0F, 4.0F))
                .withEnchantment(enchantments.getOrThrow(Enchantments.THORNS), ConstantValue.exactly(2.0F));
    }
}
