package com.ratrod.archaion.entities;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LODFallingBlock extends Entity {

    private BlockState blockState = Blocks.SAND.defaultBlockState();
    private Entity owner;
    private boolean dealtDamage;
    private int time;
    private double gravity = 0.06;

    public LODFallingBlock(EntityType<? extends LODFallingBlock> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public static void spawn(ServerLevel level, BlockState state, Vec3 pos, Entity owner) {
        LODFallingBlock entity = ACEntityTypes.LOD_FALLING_BLOCK.get().create(level, EntitySpawnReason.TRIGGERED);
        entity.blockState = state;
        entity.owner = owner;
        entity.gravity = 0.04 + level.getRandom().nextDouble() * 0.15;
        entity.setPos(pos);
        entity.setDeltaMovement(Vec3.ZERO);
        entity.xo = pos.x;
        entity.yo = pos.y;
        entity.zo = pos.z;
        level.addFreshEntity(entity);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    protected double getDefaultGravity() {
        return this.gravity;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.blockState = input.read("BlockState", BlockState.CODEC).orElse(Blocks.SAND.defaultBlockState());
        this.time = input.getIntOr("Time", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("BlockState", BlockState.CODEC, this.blockState);
        output.putInt("Time", this.time);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity, Block.getId(this.getBlockState()));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.blockState = Block.stateById(packet.getData());
        this.blocksBuilding = true;
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            this.time++;
            this.applyGravity();
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.applyEffectsFromBlocks();

            if (!this.level().isClientSide() && (this.onGround() || this.time > 600)) {
                this.damageArea();
                this.discard();
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    private void damageArea() {
        if (this.dealtDamage) return;
        this.dealtDamage = true;
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        Entity cause = this.owner != null ? this.owner : this;
        this.playSound(ACSounds.LOD_BLOCK_FALL.get(), 1.0F, 0.4F + random.nextFloat() * 0.2F);

        AABB area = this.getBoundingBox().inflate(2, 1.0, 2);
        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != cause && e.isAlive() && canTarget(e))) {
            target.hurtServer(serverLevel, serverLevel.damageSources().explosion(cause, cause), 25.0F);
        }

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_falling_block"));
        AAALevel.addParticle(serverLevel, info.position(position().add(0, 1, 0)).scale(2.5F));
    }

    private boolean canTarget(LivingEntity target) {
        if (this.owner instanceof Mob mob) {
            return mob.canAttack(target);
        }
        return true;
    }
}
