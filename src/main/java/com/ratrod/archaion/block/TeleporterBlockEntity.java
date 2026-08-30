package com.ratrod.archaion.block;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACBlockEntities;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TeleporterBlockEntity extends BlockEntity {

    private static final String TAG_COOLDOWN = "cooldown";

    public int maxHeight = 0;
    public int tickCount = 0;
    private int cooldownTicks = 0;

    public TeleporterBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ACBlockEntities.TELEPORTER.get(), worldPosition, blockState);
    }

    public boolean isDisabled() {
        return this.cooldownTicks > 0;
    }

    public void setDisabled(boolean disabled) {
        int newCooldown = disabled ? 1200 : 0;
        if (this.cooldownTicks != newCooldown) {
            this.cooldownTicks = newCooldown;
            this.setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TeleporterBlockEntity blockEntity) {
        blockEntity.tickCount++;
        if (blockEntity.cooldownTicks > 0 && --blockEntity.cooldownTicks == 0) {
            blockEntity.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }

        calculateBeamHeight(level, blockEntity);
        AABB area = new AABB(
                pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5,
                pos.getX() + 0.5, pos.getY() + blockEntity.maxHeight, pos.getZ() + 0.5
        );
        List<Player> entities = level.getEntitiesOfClass(Player.class, area);
        for (Player entity : entities) {
            blockEntity.attemptTeleport((ServerLevel) level, entity);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TeleporterBlockEntity blockEntity) {
        blockEntity.tickCount++;
        if (blockEntity.cooldownTicks > 0) {
            blockEntity.cooldownTicks--;
        }

        calculateBeamHeight(level, blockEntity);
    }

    private static void calculateBeamHeight(Level level, TeleporterBlockEntity blockEntity) {
        int h = 0;
        BlockPos.MutableBlockPos scanPos = blockEntity.getBlockPos().above().mutable();
        while (!level.getBlockState(scanPos).isFaceSturdy(level, scanPos, Direction.DOWN) && h < level.getMaxY()) {
            scanPos.move(Direction.UP);
            h++;
        }
        blockEntity.maxHeight = h + 1;
    }

    public boolean attemptTeleport(ServerLevel level, Entity entity) {
        if (this.isDisabled()) {
            return false;
        }

        TeleporterColor color = this.getBlockState().getValue(TeleporterBlock.COLOR);

        TeleporterBlockEntity target = null;
        double bestDistanceSq = Double.MAX_VALUE;
        for (TeleporterBlockEntity teleporter : collectTeleporters(level)) {
            if (teleporter == this || teleporter.isDisabled()) {
                continue;
            }
            if (teleporter.getBlockState().getValue(TeleporterBlock.COLOR) != color) {
                continue;
            }
            double distanceSq = this.worldPosition.distSqr(teleporter.worldPosition);
            if (distanceSq < bestDistanceSq) {
                bestDistanceSq = distanceSq;
                target = teleporter;
            }
        }

        if (target == null || bestDistanceSq == 0.0) {
            return false;
        }

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("teleporter_teleport"));
        AAALevel.addParticle(level, true, info.position(Vec3.atCenterOf(this.getBlockPos()).add(0, 0.2, 0)).scale(0.2F));

        level.playSound(null, this.getBlockPos(), ACSounds.TELEPORTER_WARPS.get(), SoundSource.BLOCKS, 2.0F, 1.0F);

        Vec3 destination = Vec3.atCenterOf(target.worldPosition.above());
        entity.teleportTo(destination.x, destination.y, destination.z);

        ParticleEmitterInfo info2 = new ParticleEmitterInfo(Archaion.prefix("teleporter_teleport"));
        AAALevel.addParticle(level, true, info2.position(destination.add(0, -1, 0)).scale(0.1F));

        level.playSound(null, BlockPos.containing(destination), ACSounds.TELEPORTER_WARPS.get(), SoundSource.BLOCKS, 2.0F, 1.0F);

        this.setDisabled(true);
        target.setDisabled(true);

        return true;
    }

    private static List<TeleporterBlockEntity> collectTeleporters(ServerLevel level) {
        List<TeleporterBlockEntity> result = new ArrayList<>();
        if (level.getChunkSource() instanceof ServerChunkCache chunkCache) {
            chunkCache.chunkMap.forEachReadyToSendChunk(chunk ->
                    chunk.getBlockEntities().values().forEach(blockEntity -> {
                        if (blockEntity instanceof TeleporterBlockEntity teleporter) {
                            result.add(teleporter);
                        }
                    })
            );
        }
        return result;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.cooldownTicks = input.getIntOr(TAG_COOLDOWN, 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (this.cooldownTicks > 0) {
            output.putInt(TAG_COOLDOWN, this.cooldownTicks);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
