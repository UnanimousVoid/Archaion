package com.ratrod.archaion.item;

import com.ratrod.archaion.Archaion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ACSmithingTemplateItem extends SmithingTemplateItem {

    public ACSmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<Identifier> baseSlotEmptyIcons, List<Identifier> additionsSlotEmptyIcons, Item.Properties properties) {
        super(appliesTo, ingredients, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionsSlotEmptyIcons, properties);
    }

    public static ACSmithingTemplateItem maceUpgrade(Item.Properties properties) {
        return new ACSmithingTemplateItem(
                description("mace_upgrade", "applies_to"),
                description("mace_upgrade", "ingredients"),
                description("mace_upgrade", "base_slot_description"),
                description("mace_upgrade", "additions_slot_description"),
                List.of(Archaion.prefix("container/slot/mace")),
                List.of(Archaion.prefix("container/slot/echo_shard")),
                properties
        );
    }

    public static ACSmithingTemplateItem echosGraceUpgrade(Item.Properties properties) {
        return new ACSmithingTemplateItem(
                description("echos_grace_upgrade", "applies_to"),
                description("echos_grace_upgrade", "ingredients"),
                description("echos_grace_upgrade", "base_slot_description"),
                description("echos_grace_upgrade", "additions_slot_description"),
                List.of(Archaion.prefix("container/slot/bow")),
                List.of(Archaion.prefix("container/slot/echo_charge")),
                properties
        );
    }

    private static Component description(String upgrade, String suffix) {
        return Component.translatable(Util.makeDescriptionId("item", Archaion.prefix("smithing_template." + upgrade + "." + suffix))).copy().withStyle(ChatFormatting.BLUE);
    }
}
