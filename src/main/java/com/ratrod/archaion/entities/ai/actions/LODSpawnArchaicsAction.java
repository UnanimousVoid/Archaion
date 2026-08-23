package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.entities.Archaic;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.Slated;
import com.ratrod.archaion.entities.Wight;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.misc.LODTheme;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODSpawnArchaicsAction extends ManagedAction<LastOfDeepslate> {

    private float statMultiplier = 1.2F;
    private final int[] batchSizes = new int[3];
    private int batchIndex = 0;
    private int smashIdx = 0;
    private int regenTicksLeft = 0;
    private float regenPerTick = 0.0F;

    public LODSpawnArchaicsAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        if (entity.getArchaicSystem().getPhasesTriggered() >= 2) return false;
        float hpRatio = entity.getHealth() / entity.getMaxHealth();
        return hpRatio <= 0.5F;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        this.smashIdx = 0;
        entity.getArchaicSystem().updatePhase();
        entity.sendPhaseSwitchMessage();

        int summonPhase = entity.getArchaicSystem().getPhasesTriggered();
        entity.sendBossMusic(LODTheme.STOP);

        this.statMultiplier = summonPhase >= 2 ? 1.2F : 1.1F;

        if (summonPhase == 1) {
            float maxHealth = entity.getMaxHealth();
            this.regenPerTick = maxHealth / 40.0F;
            this.regenTicksLeft = 40;
        } else {
            this.regenTicksLeft = 0;
        }
        int players = entity.getArchaicSystem().countNearbyPlayers();

        int singleSpawn = switch (entity.level().getDifficulty()) {
            case HARD -> 6;
            case NORMAL -> 5;
            default -> 4;
        };
        int singleSpawnP2 = singleSpawn + 1;

        int spawnCount = Math.max(singleSpawn, singleSpawn + 6 * (players - 1));
        if (summonPhase >= 2) {
            spawnCount = Math.max(singleSpawnP2, singleSpawnP2 + 7 * (players - 1));
        }

        int base = spawnCount / 3;
        int rem = spawnCount % 3;
        this.batchSizes[0] = base + (rem >= 1 ? 1 : 0);
        this.batchSizes[1] = base + (rem >= 2 ? 1 : 0);
        this.batchSizes[2] = base;
        this.batchIndex = 0;
        int alivePhase = entity.getArchaicSystem().countChargedArchaics();
        int existingTotal = Math.max(alivePhase, 0);
        entity.getArchaicSystem().setArchaicsIntended(existingTotal + spawnCount);

        entity.spawnArchaicsAnim.forceStart();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
        entity.playSound(ACSounds.LOD_WARN_ARCHAICS.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (this.regenTicksLeft > 0) {
            this.regenTicksLeft--;
            entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + this.regenPerTick));
        }

        if (timer >= 45 && smashIdx == 0) {
            spawnArchaicBatch();
            applySmashDamage();
            smashIdx++;

            int summonPhase = entity.getArchaicSystem().getPhasesTriggered();
            if (summonPhase == 1) {
                entity.musicPhase = LODTheme.PHASE_2;
                entity.sendBossMusic(LODTheme.PHASE_2);
            } else if (summonPhase == 2) {
                entity.musicPhase = LODTheme.PHASE_3;
                entity.sendBossMusic(LODTheme.PHASE_3);
            }

        } else if (timer >= 50 && smashIdx == 1) {
            spawnArchaicBatch();
            applySmashDamage();
            smashIdx++;
        } else if (timer >= 64 && smashIdx == 2) {
            spawnArchaicBatch();
            applySmashDamage();
            smashIdx++;
        }

        return timer <= 70;
    }

    private void applySmashDamage() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        entity.playSound(ACSounds.LOD_SMASH.get(), 3.0F, 1.0F);

        float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
        Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
        Vec3 center = entity.position().add(flatLook.yRot(7.5F * Mth.DEG_TO_RAD).scale(7.5));
        if (smashIdx == 1) {
            center = entity.position().add(flatLook.yRot(-7.5F * Mth.DEG_TO_RAD).scale(7.5));
        } else if (smashIdx == 2) {
            center = entity.position().add(flatLook.scale(7.5));
        }

        float scaleMult = smashIdx == 2 ? 1.5F : 1.0F;
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_ground"));
        AAALevel.addParticle(serverLevel, info.position(center.add(0, 0.2, 0)).scale(3.0F * scaleMult));

        AABB area = AABB.ofSize(center, 12 * scaleMult, 6, 12 * scaleMult);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            entity.attackTarget(serverLevel, target, 1.2F * scaleMult, ACEntity.Operation.MULTIPLY);
            Vec3 knockback = target.position().subtract(center).normalize().scale(1.5 * scaleMult).add(0, 0.35, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }

    private void spawnArchaicBatch() {
        if (entity.level().isClientSide()) return;
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;
        if (this.batchIndex >= this.batchSizes.length) return;

        int count = this.batchSizes[this.batchIndex];
        this.batchIndex++;
        for (int i = 0; i < count; i++) {
            this.spawnArchaic(serverLevel);
        }
    }

    private EntityType<? extends Monster> pickArchaicType() {
        boolean phaseTwo = entity.getArchaicSystem().getPhasesTriggered() >= 2;
        WeightedList<EntityType<? extends Monster>> choices = WeightedList.<EntityType<? extends Monster>>builder()
                .add(ACEntityTypes.BRAVE.get(), phaseTwo ? 1 : 0)
                .add(ACEntityTypes.WIGHT.get(), 4)
                .add(ACEntityTypes.SLATED.get(), 3)
                .add(ACEntityTypes.DEEPSLATE_SENTINEL.get(), 4)
                .build();
        return choices.getRandom(entity.getRandom()).orElse(ACEntityTypes.BRAVE.get());
    }

    private void spawnArchaic(ServerLevel serverLevel) {
        Monster mob = this.pickArchaicType().create(serverLevel, EntitySpawnReason.TRIGGERED);
        Vec3 center = entity.position();

        for (int attempt = 0; attempt < 20; attempt++) {
            double angle = entity.getRandom().nextDouble() * Math.PI * 2;
            double radius = 4.0 + entity.getRandom().nextDouble() * 7.0;
            double x = center.x + Math.cos(angle) * radius;
            double z = center.z + Math.sin(angle) * radius;
            double y = center.y;

            mob.snapTo(x, y, z, entity.getRandom().nextFloat() * 360.0F, 0.0F);

            if (serverLevel.noCollision(mob, mob.getBoundingBox())) {
                ((Archaic) mob).setCharged(true);
                ((Archaic) mob).setOwnerUUID(entity.getUUID());

                if (mob instanceof Wight) {
                    mob.equip(new EquipmentTable(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_RANGED, 0.0F));
                } else if (mob instanceof Slated) {
                    mob.equip(new EquipmentTable(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_MELEE, 0.0F));
                }

                this.scaleStats(mob);
                serverLevel.addFreshEntity(mob);

                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_archaic_summon"));
                AAALevel.addParticle(serverLevel, info.position(mob.position()).scale(0.2F));

                mob.playSound(ACSounds.LOD_SPAWN_ARCHAICS.get(), 3.0F, 1.0F);

                return;
            }
        }

        mob.discard();
    }

    private void scaleStats(Monster mob) {
        if (this.statMultiplier <= 1.0F) return;
        AttributeInstance maxHealth = mob.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance armor = mob.getAttribute(Attributes.ARMOR);
        if (maxHealth != null) maxHealth.setBaseValue(maxHealth.getBaseValue() * this.statMultiplier);
        if (damage != null) damage.setBaseValue(damage.getBaseValue() * this.statMultiplier);
        if (armor != null) armor.setBaseValue(armor.getBaseValue() * this.statMultiplier);
        mob.setHealth(mob.getMaxHealth());
    }

    @Override
    public void onStop() {
        entity.spawnArchaicsAnim.stop();
    }
}
