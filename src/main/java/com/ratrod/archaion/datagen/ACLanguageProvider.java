package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.item.EchosGraceItem;
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

        this.add("effect.archaion.armor_break", "Armor Break");

        this.advancement("root", "Archaion", "Descend into the deep, where the Last sleeps.");
        this.advancement("obtain_map", "Whispers in the Ruins", "Find an Ancient Keep map in an Ancient City.");
        this.advancement("enter_ancient_keep", "Breaching the Keep", "Step inside the Ancient Keep.");

        this.advancement("obtain_brave_rod", "The not-so Brave", "Collect a Brave Rod from a fallen Brave.");
        this.advancement("craft_impact_pearl", "A Pearl of Courage", "Craft an Impact Pearl.");

        this.advancement("obtain_echo_key", "Unlocking the Deep", "Claim an Echo Key from a Deepslate Spawner.");
        this.advancement("open_deepslate_vault", "Riches of the Forgotten", "Unlock a Deepslate Vault and take its Echo Charge.");
        this.advancement("craft_echos_grace", "Grace of the Echoes", "Forge Echo's Grace from a Bow, an Echo Charge, and its template.");

        this.advancement("kill_last_of_deepslate", "The Last of Deepslate", "Defeat the Last of Deepslate, guardian of the Ancient Keep.");
        this.advancement("craft_echo_mace", "Echoing Wrath", "Forge the Echo Mace from a Mace, an Echo Shard, and its template.");

        this.add(ACItems.ECHOS_GRACE.get(), "Echo's Grace");

        this.add(ACItems.ECHO_MACE_UPGRADE_SMITHING_TEMPLATE.get(), "Echo Mace Upgrade");
        this.add("item.archaion.smithing_template.mace_upgrade.applies_to", "Mace");
        this.add("item.archaion.smithing_template.mace_upgrade.ingredients", "Echo Shard");
        this.add("item.archaion.smithing_template.mace_upgrade.base_slot_description", "Add a Mace");
        this.add("item.archaion.smithing_template.mace_upgrade.additions_slot_description", "Add an Echo Shard");

        this.add(ACItems.ECHOS_GRACE_UPGRADE_SMITHING_TEMPLATE.get(), "Echo's Grace Upgrade");
        this.add("item.archaion.smithing_template.echos_grace_upgrade.applies_to", "Bow");
        this.add("item.archaion.smithing_template.echos_grace_upgrade.ingredients", "Echo Charge");
        this.add("item.archaion.smithing_template.echos_grace_upgrade.base_slot_description", "Add a Bow");
        this.add("item.archaion.smithing_template.echos_grace_upgrade.additions_slot_description", "Add an Echo Charge");

        this.add("misc.archaion.last_of_deepslate.echo_charge_required", "%s Echo Charges required to activate boss");
        this.add("misc.archaion.last_of_deepslate.phase_1_notifier", "You feel a deep chill.");
        this.add("misc.archaion.last_of_deepslate.phase_2_notifier", "The cold is becoming unbearable...");
        this.add("misc.archaion.last_of_deepslate.phase_3_notifier", "A faint warmth pierces the cold...");
        this.add("misc.archaion.filled_map.ancient_keep", "Ancient Keep Map");

        this.add("misc.archaion.hologram.ancient_keep_0", this.hologram(
                "Once, we built wonders.",
                "Cities. Thrones. Monuments that touched the sky.",
                "Then came the ruin.",
                "And all our wonders became graves."
        ));

        this.add("misc.archaion.hologram.ancient_keep_1", this.hologram(
                "We fled beneath the stone.",
                "It followed.",
                "We sealed the deep.",
                "It found us.",
                "",
                "There was nowhere left to run."
        ));

        this.add("misc.archaion.hologram.ancient_keep_2", this.hologram(
                "Then we remembered the Firsts.",
                "Guardians older than our oldest ruins.",
                "Made by hands that were not ours.",
                "",
                "We never knew how they were made.",
                "We only knew what they were made for."
        ));

        this.add("misc.archaion.hologram.ancient_keep_3", this.hologram(
                "Not a First.",
                "But made from their memory.",
                "Made to stand against the end.",
                "",
                "The First came before us.",
                "This one came after.",
                "",
                "Our Last."
        ));

        this.addEntityType(ACEntityTypes.LAST_OF_DEEPSLATE, "Last of Deepslate");

        for (DeferredHolder<Item, ? extends Item> item : ACItems.ITEM.getEntries()) {
            if (!(item.get() instanceof BlockItem) && !(item.get() instanceof SmithingTemplateItem) && !(item.get() instanceof EchosGraceItem)) {
                this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
            }
        }

        for (DeferredHolder<Block, ? extends Block> block : ACBlocks.BLOCK.getEntries()) {
            this.addBlock(block, WordUtils.capitalize(block.getId().getPath().replace("_", " ")));
        }

        for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> en : ACEntityTypes.ENTITY_TYPE.getEntries()) {
            if (!en.equals(ACEntityTypes.LAST_OF_DEEPSLATE)) {
                this.addEntityType(en, WordUtils.capitalize(en.getId().getPath().replace("_", " ")));
            }
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
                Map.entry("lod_step", "Last of Deepslate steps"),
                Map.entry("lod_boss_theme_phase_1", "Last of Deepslate theme"),
                Map.entry("lod_boss_theme_phase_2", "Last of Deepslate theme"),
                Map.entry("lod_boss_theme_phase_3", "Last of Deepslate theme"),
                Map.entry("lod_echo_charge_interact", "Last of Deepslate interaction"),
                Map.entry("sentinel_ambient", "Deepslate Sentinel hums"),
                Map.entry("sentinel_death", "Deepslate Sentinel dies"),
                Map.entry("sentinel_hurt", "Deepslate Sentinel hurts"),
                Map.entry("sentinel_start_charging", "Deepslate Sentinel starts charging")
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

    private String hologram(String... lines) {
        return String.join("\n", lines);
    }

    private void advancement(String path, String title, String description) {
        this.add("advancements." + path + ".title", title);
        this.add("advancements." + path + ".description", description);
    }
}
