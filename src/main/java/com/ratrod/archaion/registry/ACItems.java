package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.item.ACSmithingTemplateItem;
import com.ratrod.archaion.items.EchoMaceItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Weapon;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACItems {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Archaion.MODID);

    public static final DeferredItem<Item> ECHO_KEY = ITEM.registerItem("echo_key", Item::new);
    public static final DeferredItem<EchoMaceItem> ECHO_MACE = ITEM.registerItem("echo_mace",
            EchoMaceItem::new,
            () -> new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .durability(250)
                    .component(DataComponents.TOOL, MaceItem.createToolProperties())
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .repairable(Items.ECHO_SHARD)
                    .attributes(EchoMaceItem.createAttributes())
                    .enchantable(15)
    );

    public static final DeferredItem<Item> SLATED_SPAWN_EGG = ITEM.registerItem("slated_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.SLATED.get()));
    public static final DeferredItem<Item> WIGHT_SPAWN_EGG = ITEM.registerItem("wight_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.WIGHT.get()));
    public static final DeferredItem<Item> BRAVE_SPAWN_EGG = ITEM.registerItem("brave_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.BRAVE.get()));
    public static final DeferredItem<Item> LAST_OF_DEEPSLATE_SPAWN_EGG = ITEM.registerItem("last_of_deepslate_spawn_egg", SpawnEggItem::new, () -> new Item.Properties().spawnEgg(ACEntityTypes.LAST_OF_DEEPSLATE.get()));

    public static final DeferredItem<Item> ECHO_MACE_UPGRADE_SMITHING_TEMPLATE = ITEM.registerItem("echo_mace_upgrade_smithing_template", ACSmithingTemplateItem::new, () -> new Item.Properties().rarity(Rarity.EPIC));

}