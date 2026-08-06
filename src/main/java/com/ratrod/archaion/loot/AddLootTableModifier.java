package com.ratrod.archaion.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddLootTableModifier extends LootModifier {

    public static final MapCodec<AddLootTableModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst)
            .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(m -> m.lootTable))
            .apply(inst, AddLootTableModifier::new));

    private final ResourceKey<LootTable> lootTable;

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public AddLootTableModifier(LootItemCondition[] conditions, int priority, ResourceKey<LootTable> lootTable) {
        super(conditions, priority);
        this.lootTable = lootTable;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable table = context.getResolver().lookupOrThrow(Registries.LOOT_TABLE).getOrThrow(this.lootTable).value();
        table.getRandomItemsRaw(context, generatedLoot::add);
        return generatedLoot;
    }
}
