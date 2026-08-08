package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.item.ACSmithingTemplateItem;
import com.ratrod.archaion.item.EchoChargeItem;
import com.ratrod.archaion.item.EchoMaceItem;
import com.ratrod.archaion.item.EchosGraceItem;
import com.ratrod.archaion.item.ImpactPearlItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Weapon;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACItems {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Archaion.MODID);

    public static final DeferredItem<Item> ECHO_KEY = ITEM.registerItem("echo_key", Item::new);
    public static final DeferredItem<EchoMaceItem> ECHO_MACE = ITEM.registerItem("echo_mace",
            EchoMaceItem::new, () -> new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .durability(250)
                    .component(DataComponents.TOOL, MaceItem.createToolProperties())
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .repairable(Items.ECHO_SHARD)
                    .attributes(EchoMaceItem.createAttributes())
                    .enchantable(15)
                    .useCooldown(1.5F)
    );

    public static final DeferredItem<Item> SLATED_SPAWN_EGG = ITEM.registerItem("slated_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.SLATED.get()));
    public static final DeferredItem<Item> WIGHT_SPAWN_EGG = ITEM.registerItem("wight_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.WIGHT.get()));
    public static final DeferredItem<Item> BRAVE_SPAWN_EGG = ITEM.registerItem("brave_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.BRAVE.get()));
    public static final DeferredItem<Item> DEEPSLATE_SENTINEL_SPAWN_EGG = ITEM.registerItem("deepslate_sentinel_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.DEEPSLATE_SENTINEL.get()));
    public static final DeferredItem<Item> LAST_OF_DEEPSLATE_SPAWN_EGG = ITEM.registerItem("last_of_deepslate_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.LAST_OF_DEEPSLATE.get()));

    public static final DeferredItem<Item> ECHO_MACE_UPGRADE_SMITHING_TEMPLATE = ITEM.registerItem("echo_mace_upgrade_smithing_template", ACSmithingTemplateItem::new, () -> new Item.Properties().rarity(Rarity.EPIC));

    public static final DeferredItem<Item> BRAVE_ROD = ITEM.registerItem("brave_rod", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> BRAVE_ESSENCE = ITEM.registerItem("brave_essence", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> IMPACT_PEARL = ITEM.registerItem("impact_pearl", ImpactPearlItem::new, () -> new Item.Properties().stacksTo(16).useCooldown(1.0F));
    public static final DeferredItem<Item> ECHO_CHARGE = ITEM.registerItem("echo_charge", EchoChargeItem::new, () -> new Item.Properties().useCooldown(0.2F));
    public static final DeferredItem<EchosGraceItem> ECHOS_GRACE = ITEM.registerItem("echos_grace", EchosGraceItem::new, () -> new Item.Properties().durability(512).rarity(Rarity.EPIC).enchantable(15).repairable(Items.ECHO_SHARD));


}