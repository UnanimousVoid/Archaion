package com.ratrod.archaion.block;

import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class DeepslateVaultBlock extends VaultBlock {

    public DeepslateVaultBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof VaultBlockEntity vault) {
            vault.setConfig(new VaultConfig(ACLootTables.DEEPSLATE_VAULT, 4.0, 4.5, new ItemStack(ACItems.ECHO_KEY.get()), Optional.empty()));
        }
    }
}