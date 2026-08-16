package com.ratrod.archaion.api.trial;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Generic trial-vault block. Reuses the vanilla {@link VaultBlockEntity} (via {@link net.minecraft.world.level.block.entity.BlockEntityType#VAULT}),
 * so every registered vault block must be exposed to that block-entity type through the {@code isValid} hook
 * (see {@code BlockEntityTypeMixin} / {@link ACTrialRegistry#isVaultBlock}).
 *
 * <p>The vault's config (loot table, key item, activation ranges) is supplied per instance; it is applied
 * on placement so each variant carries its own contents. Create instances via
 * {@link ACTrialRegistry#registerVault(String, java.util.function.Function, net.minecraft.resources.ResourceKey, net.neoforged.neoforge.registries.DeferredItem, double, double)}.
 */
public class TrialVaultBlock extends VaultBlock {

    private static final Supplier<VaultConfig> DEFAULT_CONFIG = () -> new VaultConfig(
            BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 4.0, 4.5, new ItemStack(Items.TRIAL_KEY), Optional.empty());

    private final Supplier<VaultConfig> configSupplier;

    /** Codec/registry-only constructor; uses the vanilla trial-chamber defaults. */
    public TrialVaultBlock(Properties properties) {
        this(properties, DEFAULT_CONFIG);
    }

    public TrialVaultBlock(Properties properties, Supplier<VaultConfig> configSupplier) {
        super(properties);
        this.configSupplier = configSupplier;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof VaultBlockEntity vault) {
            VaultConfig config = this.configSupplier.get();
            if (config != null) {
                vault.setConfig(config);
            }
        }
    }
}
