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
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    private static final Component MACE_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", Archaion.prefix("smithing_template.mace_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component MACE_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", Archaion.prefix("smithing_template.mace_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component MACE_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Archaion.prefix("smithing_template.mace_upgrade.base_slot_description")));
    private static final Component MACE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", Archaion.prefix("smithing_template.mace_upgrade.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_MACE = Archaion.prefix("container/slot/mace");
    private static final Identifier EMPTY_SLOT_ECHO_SHARD = Archaion.prefix("container/slot/echo_shard");

    public ACSmithingTemplateItem(Item.Properties properties) {
        super(MACE_UPGRADE_APPLIES_TO, MACE_UPGRADE_INGREDIENTS, MACE_UPGRADE_BASE_SLOT_DESCRIPTION, MACE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, List.of(EMPTY_SLOT_MACE), List.of(EMPTY_SLOT_ECHO_SHARD), properties);
    }
}
