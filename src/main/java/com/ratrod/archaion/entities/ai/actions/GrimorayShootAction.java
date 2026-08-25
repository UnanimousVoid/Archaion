package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.Grimoray;
import com.ratrod.archaion.entities.GrimorayType;
import com.ratrod.archaion.entities.projectile.GrimoraySpellProjectile;
import com.ratrod.archaion.registry.ACEntityTypes;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GrimorayShootAction extends ManagedAction<Grimoray> {

    public GrimorayShootAction(Grimoray entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        double distance = entity.distanceTo(target);
        return entity.getSpellCooldown() <= 0 && distance <= Grimoray.SHOOT_RANGE && distance >= Grimoray.MIN_SHOOT_RANGE;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.shootAnim.start();
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 20) {
            LivingEntity target = entity.getTarget();
            if (target != null && entity.level() instanceof ServerLevel serverLevel) {

                if (entity.getGrimorayType() == GrimorayType.HEALING) {

                    entity.playSound(SoundEvents.PLAYER_LEVELUP, 1.5f, 1.5f);

                    AABB area = entity.getBoundingBox().inflate(16.0);
                    for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e instanceof Enemy && e.isAlive())) {
                        if (living.is(entity)) {
                            living.heal(4.0F);
                            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("grimoray_spell_healing_core"));
                            AAALevel.addParticle(serverLevel, info.bindOnEntity(living).scale(1.5F));
                        } else {
                            living.heal(3.0F);
                            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("grimoray_spell_healing"));
                            AAALevel.addParticle(serverLevel, info.bindOnEntity(living).scale(1F));
                        }
                    }

                } else {

                    Vec3 pos = entity.position().add(0, entity.getBbHeight() * 0.6, 0);

                    entity.playSound(SoundEvents.SQUID_SQUIRT, 1.5f, 0.8f);

                    GrimoraySpellProjectile spell = ACEntityTypes.GRIMORAY_SPELL.get().create(serverLevel);
                    spell.moveTo(pos);
                    spell.setOwner(entity);
                    spell.setGrimorayType(entity.getGrimorayType());

                    float dx = (float) (target.getX() - pos.x);
                    float dz = (float) (target.getZ() - pos.z);
                    float dy = (float) (pos.y - target.getY());
                    float dist = (float) Math.sqrt(dx * dx + dz * dz);
                    float timeGuess = (float) Math.sqrt(2 * dy / spell.getGravity());
                    float speed = Float.isFinite(timeGuess) ? Math.min(dist / timeGuess, 0.9f) : 0.5f;
                    Vec3 dir = dist < 1.0E-4 ? Vec3.ZERO : new Vec3(dx / dist, 0, dz / dist);
                    spell.setDeltaMovement(dir.scale(speed).add(0, 0.1, 0));

                    serverLevel.addFreshEntity(spell);
                }
            }
        }

        return timer <= 30;
    }

    @Override
    public void onStop() {
        entity.shootAnim.stop();
        entity.setSpellCooldown(entity.getSpellCDDuration());
    }
}
