package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ACEntityLootTables extends EntityLootSubProvider {
    public ACEntityLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {

        this.add(ACEntityTypes.BRAVE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ACItems.BRAVE_ROD)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );

        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.add(ACEntityTypes.GRIMORAY.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BOOK).setWeight(8))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.SHARPNESS), UniformGenerator.between(1.0F, 5.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.POWER), UniformGenerator.between(1.0F, 5.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.UNBREAKING), UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.MENDING), ConstantValue.exactly(1.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantments.getOrThrow(Enchantments.DENSITY), UniformGenerator.between(1.0F, 5.0F)))
                        )
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );

        this.add(ACEntityTypes.HAUNTER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                )
        );

        this.add(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItems.ECHO_MACE_UPGRADE_SMITHING_TEMPLATE)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ACItems.ECHO_CHARGE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 12)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                ACEntityTypes.BRAVE.get(),
                ACEntityTypes.LAST_OF_DEEPSLATE.get(),
                ACEntityTypes.GRIMORAY.get(),
                ACEntityTypes.HAUNTER.get()
        );
    }
}