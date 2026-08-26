package com.ratrod.archaion.api.trial;

import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;

public class ACTrialSpawnerBlockEntity extends BlockEntity implements TrialSpawner.StateAccessor, Spawner {
    private static final Logger LOGGER = LogUtils.getLogger();

    private TrialSpawner trialSpawner;

    public ACTrialSpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ACTrialRegistry.TRIAL_SPAWNER.get(), worldPosition, blockState);
        PlayerDetector playerDetector = SharedConstants.DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : PlayerDetector.NO_CREATIVE_PLAYERS;
        TrialSpawnerConfig config = ACTrialRegistry.configFor(blockState.getBlock());
        this.trialSpawner = new TrialSpawner(config, config, new TrialSpawnerData(), 36000, 14, this, playerDetector, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL);
    }

    private void applyVariantConfig() {
        TrialSpawnerConfig config = ACTrialRegistry.configFor(this.getBlockState().getBlock());
        this.trialSpawner = new TrialSpawner(
            config, config, this.trialSpawner.getData(), this.trialSpawner.getTargetCooldownLength(), this.trialSpawner.getRequiredPlayerRange(),
            this, this.trialSpawner.getPlayerDetector(), this.trialSpawner.getEntitySelector()
        );
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider registries) {
        super.loadAdditional(input, registries);
        this.trialSpawner = this.trialSpawner.codec().parse(NbtOps.INSTANCE, input).resultOrPartial(LOGGER::error).orElse(this.trialSpawner);
        this.applyVariantConfig();
        if (this.level != null) {
            this.markUpdated();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag output, HolderLookup.Provider registries) {
        super.saveAdditional(output, registries);
        this.trialSpawner.codec().encodeStart(NbtOps.INSTANCE, this.trialSpawner).ifSuccess(p_ -> output.merge((CompoundTag) p_));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.trialSpawner.getData().getUpdateTag(this.getBlockState().getValue(ACTrialSpawnerBlock.STATE));
    }

    @Override
    public void setEntityId(EntityType<?> type, RandomSource random) {
        this.trialSpawner.getData().setEntityId(this.trialSpawner, random, type);
        this.setChanged();
    }

    public TrialSpawner getTrialSpawner() {
        return this.trialSpawner;
    }

    @Override
    public TrialSpawnerState getState() {
        return !this.getBlockState().hasProperty(BlockStateProperties.TRIAL_SPAWNER_STATE) ? TrialSpawnerState.INACTIVE : this.getBlockState().getValue(BlockStateProperties.TRIAL_SPAWNER_STATE);
    }

    @Override
    public void setState(Level level, TrialSpawnerState state) {
        this.setChanged();
        level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(BlockStateProperties.TRIAL_SPAWNER_STATE, state));
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
}
