package com.ratrod.archaion.block;

import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.registry.ACBlockEntities;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;

public class DeepslateTrialSpawnerBlockEntity extends BlockEntity implements TrialSpawner.StateAccessor, Spawner {

    private static SpawnData spawnDataOf(EntityType<?> type) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        return new SpawnData(tag, Optional.empty(), Optional.empty());
    }

    private final TrialSpawner trialSpawner = this.createDefaultSpawner();

    public DeepslateTrialSpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ACBlockEntities.DEEPSLATE_TRIAL_SPAWNER.get(), worldPosition, blockState);
    }

    private TrialSpawner createDefaultSpawner() {
        PlayerDetector playerDetector = SharedConstants.DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : PlayerDetector.NO_CREATIVE_PLAYERS;
        PlayerDetector.EntitySelector entitySelector = PlayerDetector.EntitySelector.SELECT_FROM_LEVEL;

        TrialSpawnerConfig config = TrialSpawnerConfig.builder()
                .spawnRange(4)
                .totalMobs(1.0F)
                .simultaneousMobs(1.0F)
                .totalMobsAddedPerPlayer(0)
                .simultaneousMobsAddedPerPlayer(1.0F)
                .ticksBetweenSpawn(0)
                .spawnPotentialsDefinition(defaultSpawnPotentials())
                .lootTablesToEject(WeightedList.<ResourceKey<LootTable>>builder().add(ACLootTables.DEEPSLATE_SPAWNER, 1).build())
                .build();

        TrialSpawner.FullConfig fullConfig = new TrialSpawner.FullConfig(Holder.direct(config), Holder.direct(config), 36000, 14);

        return new TrialSpawner(fullConfig, this, playerDetector, entitySelector);
    }

    private static WeightedList<SpawnData> defaultSpawnPotentials() {
        return WeightedList.<SpawnData>builder()
                .add(spawnDataOf(EntityType.WARDEN), 1)
                .build();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.trialSpawner.load(input);
        if (this.level != null) {
            this.markUpdated();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.trialSpawner.store(output);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.trialSpawner.getStateData().getUpdateTag(
            this.getBlockState().getValue(DeepslateTrialSpawnerBlock.STATE)
        );
    }

    @Override
    public void setEntityId(EntityType<?> type, RandomSource random) {
        if (this.level == null) {
            return;
        }
        this.trialSpawner.overrideEntityToSpawn(type, this.level);
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
