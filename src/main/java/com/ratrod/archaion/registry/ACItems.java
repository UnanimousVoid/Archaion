package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.item.ACSmithingTemplateItem;
import com.ratrod.archaion.item.EchoChargeItem;
import com.ratrod.archaion.item.EchoMaceItem;
import com.ratrod.archaion.item.EchosGraceItem;
import com.ratrod.archaion.item.ImpactPearlItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACItems {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Archaion.MODID);

    public static final DeferredItem<Item> ECHO_KEY = ITEM.registerItem("echo_key", Item::new);
    public static final DeferredItem<Item> BRAVE_ROD = ITEM.registerItem("brave_rod", Item::new, new Item.Properties());
    public static final DeferredItem<Item> BRAVE_ESSENCE = ITEM.registerItem("brave_essence", Item::new, new Item.Properties());
    public static final DeferredItem<Item> IMPACT_PEARL = ITEM.registerItem("impact_pearl", ImpactPearlItem::new, new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> ECHO_CHARGE = ITEM.registerItem("echo_charge", EchoChargeItem::new, new Item.Properties());

    public static final DeferredItem<Item> ECHO_MACE_UPGRADE_SMITHING_TEMPLATE = ITEM.registerItem("echo_mace_upgrade_smithing_template", ACSmithingTemplateItem::maceUpgrade, new Item.Properties().rarity(Rarity.EPIC));
    public static final DeferredItem<Item> ECHOS_GRACE_UPGRADE_SMITHING_TEMPLATE = ITEM.registerItem("echos_grace_upgrade_smithing_template", ACSmithingTemplateItem::echosGraceUpgrade, new Item.Properties().rarity(Rarity.EPIC));

    public static final DeferredItem<EchoMaceItem> ECHO_MACE = ITEM.registerItem("echo_mace",
            EchoMaceItem::new, new Item.Properties()
                    .rarity(Rarity.EPIC)
                    .durability(250)
                    .component(DataComponents.TOOL, MaceItem.createToolProperties())
                    .attributes(EchoMaceItem.createAttributes())
    );

    public static final DeferredItem<EchosGraceItem> ECHOS_GRACE = ITEM.registerItem("echos_grace", EchosGraceItem::new, new Item.Properties().durability(512).rarity(Rarity.EPIC));


    public static final DeferredItem<Item> SLATED_SPAWN_EGG = ITEM.registerItem("slated_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.SLATED.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> WIGHT_SPAWN_EGG = ITEM.registerItem("wight_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.WIGHT.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> BRAVE_SPAWN_EGG = ITEM.registerItem("brave_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.BRAVE.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> DEEPSLATE_SENTINEL_SPAWN_EGG = ITEM.registerItem("deepslate_sentinel_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.DEEPSLATE_SENTINEL.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> GRIMORAY_SPAWN_EGG = ITEM.registerItem("grimoray_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.GRIMORAY.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> HAUNTER_SPAWN_EGG = ITEM.registerItem("haunter_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.HAUNTER.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));
    public static final DeferredItem<Item> LAST_OF_DEEPSLATE_SPAWN_EGG = ITEM.registerItem("last_of_deepslate_spawn_egg", properties -> new DeferredSpawnEggItem(() -> ACEntityTypes.LAST_OF_DEEPSLATE.get(), 0xFFFFFFFF, 0xFFFFFFFF, properties));

}
