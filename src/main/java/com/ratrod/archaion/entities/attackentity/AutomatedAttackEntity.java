package com.ratrod.archaion.entities.attackentity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class AutomatedAttackEntity extends Entity implements TraceableEntity {
    public LivingEntity owner;
    private AttackBehavior behavior;
    private int lifetime;
    private boolean hasStarted = false;

    public AutomatedAttackEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void setBehavior(LivingEntity owner, int lifetime, AttackBehavior behavior) {
        this.setOwner(owner);
        this.lifetime = lifetime;
        this.behavior = behavior;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.behavior == null || this.getOwner() == null) {
                this.discard();
                return;
            }
            if (!this.hasStarted) {
                this.behavior.onStart(this);
                this.hasStarted = true;
            }
            this.behavior.onTick(this);
            if (this.tickCount >= this.lifetime) {
                this.behavior.onEnd(this);
                this.discard();
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setOwner(LivingEntity entity) {
        this.owner = entity;
    }

    public interface AttackBehavior {
        default void onStart(AutomatedAttackEntity entity) {}
        default void onTick(AutomatedAttackEntity entity) {}
        default void onEnd(AutomatedAttackEntity entity) {}
    }
}
