package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

public class ACLanguageProvider extends LanguageProvider {
    public ACLanguageProvider(PackOutput output, String locale) {
        super(output, Archaion.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("creativetab.archaion_tab", "Archaion");

        this.add(ACItems.ECHO_MACE_UPGRADE_SMITHING_TEMPLATE.get(), "Echo Mace Upgrade");
        this.add("item.archaion.smithing_template.mace_upgrade.applies_to", "Mace");
        this.add("item.archaion.smithing_template.mace_upgrade.ingredients", "Echo Shard");
        this.add("item.archaion.smithing_template.mace_upgrade.base_slot_description", "Add a Mace");
        this.add("item.archaion.smithing_template.mace_upgrade.additions_slot_description", "Add an Echo Shard");

        for (DeferredHolder<Item, ? extends Item> item : ACItems.ITEM.getEntries()) {
            if (!(item.get() instanceof BlockItem) && !(item.get() instanceof SmithingTemplateItem)) {
                this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
            }
        }
        for (DeferredHolder<Block, ? extends Block> block : ACBlocks.BLOCK.getEntries()) {
            this.addBlock(block, WordUtils.capitalize(block.getId().getPath().replace("_", " ")));
        }

        for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> en : ACEntityTypes.ENTITY_TYPE.getEntries()) {
            this.addEntityType(en, WordUtils.capitalize(en.getId().getPath().replace("_", " ")));
        }
    }
}
