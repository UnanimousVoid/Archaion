package com.ratrod.archaion.block;

import com.mojang.serialization.MapCodec;
import com.ratrod.archaion.registry.ACBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class DeepslateSpawnerBlock extends BaseEntityBlock {
    public static final MapCodec<DeepslateSpawnerBlock> CODEC = simpleCodec(DeepslateSpawnerBlock::new);
    public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;

    public DeepslateSpawnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, TrialSpawnerState.INACTIVE));
    }

    @Override
    public MapCodec<DeepslateSpawnerBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new DeepslateSpawnerBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
            ? createTickerHelper(type, ACBlockEntities.DEEPSLATE_TRIAL_SPAWNER.get(), (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickServer(serverLevel, pos, true))
            : createTickerHelper(type, ACBlockEntities.DEEPSLATE_TRIAL_SPAWNER.get(), (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickClient(innerLevel, pos, false));
    }
}
