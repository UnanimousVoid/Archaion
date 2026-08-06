package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Map;

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

        this.add("misc.archaion.filled_map.ancient_keep", "Ancient Keep Map");

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

        Map<String, String> subtitles = Map.ofEntries(
                Map.entry("brave_ambient", "Brave ambient"),
                Map.entry("brave_death", "Brave death"),
                Map.entry("brave_hurt", "Brave hurt"),
                Map.entry("brave_jump", "Brave jump"),
                Map.entry("echo_mace_throw", "Echo mace throw"),
                Map.entry("echo_star_blast", "Echo star blast"),
                Map.entry("lod_action_start", "Last of Deepslate prepares"),
                Map.entry("lod_activate", "Last of Deepslate awakens"),
                Map.entry("lod_activate_smash", "Last of Deepslate crashes down"),
                Map.entry("lod_ambient", "Last of Deepslate groans"),
                Map.entry("lod_death", "Last of Deepslate dies"),
                Map.entry("lod_hurt", "Last of Deepslate hurts"),
                Map.entry("lod_shoot", "Last of Deepslate shoots"),
                Map.entry("lod_smash", "Last of Deepslate smashes"),
                Map.entry("lod_spin", "Last of Deepslate spins"),
                Map.entry("lod_step", "Last of Deepslate steps")
        );
        for (DeferredHolder<SoundEvent, ? extends SoundEvent> sound : ACSounds.SOUND_EVENT.getEntries()) {
            String path = sound.getId().getPath();
            this.add("subtitle." + Archaion.MODID + "." + path, subtitles.getOrDefault(path, toSubtitle(path)));
        }
    }

    private static String toSubtitle(String path) {
        String[] parts = path.split("_");
        StringBuilder subtitle = new StringBuilder(parts[0]);
        subtitle.setCharAt(0, Character.toUpperCase(subtitle.charAt(0)));
        for (int i = 1; i < parts.length; i++) {
            subtitle.append(' ').append(parts[i]);
        }
        return subtitle.toString();
    }
}
