package com.ratrod.archaion.entities.ai.systems;

import com.ratrod.archaion.entities.Archaic;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.network.BossBarDataOutput;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.function.Predicate;

public class ArchaicRaid {
    private final LastOfDeepslate entity;

    private int phasesTriggered = 0;
    private int archaicsIntended = 0;
    private int lastRaidAlive = -1;
    private int lastRaidTotal = -1;
    private int lastPhase = -1;

    public ArchaicRaid(LastOfDeepslate entity) {
        this.entity = entity;
    }

    public int getPhasesTriggered() {
        return this.phasesTriggered;
    }

    public void updatePhase() {
        this.phasesTriggered++;
        entity.setPhase(this.phasesTriggered);
    }

    public int getArchaicsIntended() {
        return this.archaicsIntended;
    }

    public void setArchaicsIntended(int archaicsIntended) {
        this.archaicsIntended = archaicsIntended;
    }

    public int countChargedArchaics() {
        return this.countNearby(Monster.class, 80.0, m -> m instanceof Archaic a && a.isCharged() && entity.getUUID().equals(a.getOwnerUUID()));
    }

    public int countNearbyPlayers() {
        return this.countNearby(Player.class, 80.0, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
    }

    private <T extends Entity> int countNearby(Class<T> entityClass, double radius, Predicate<? super T> filter) {
        if (entity.level().isClientSide()) return 0;
        return entity.level().getEntitiesOfClass(entityClass, entity.getBoundingBox().inflate(radius), filter).size();
    }

    public boolean isOwnedArchaic(LivingEntity target) {
        return target instanceof Archaic archaic && entity.getUUID().equals(archaic.getOwnerUUID());
    }

    public float getArchaicProtectionMultiplier(int aliveArchaics) {
        if (this.archaicsIntended <= 0 || aliveArchaics <= 0) return 1.0F;
        float ratio = Mth.clamp(aliveArchaics / (float) this.archaicsIntended, 0.0F, 1.0F);
        float baseRed = 0.3F;
        float maxRed = 0.95F;
        float reduction = baseRed + (maxRed - baseRed) * ratio;
        return 1.0F - reduction;
    }

    public void tick() {
        int aliveArchaics = this.countChargedArchaics();
        int raidTotal = this.archaicsIntended;
        int phase = entity.getPhase();
        if (aliveArchaics != this.lastRaidAlive || raidTotal != this.lastRaidTotal || phase != this.lastPhase) {
            this.lastRaidAlive = aliveArchaics;
            this.lastRaidTotal = raidTotal;
            this.lastPhase = phase;
            entity.setHasChargedArchaics(aliveArchaics > 0);
            entity.syncBossBarData(entity.getBossEvent(), 0);
        }
    }

    public void writeBossBarData(BossBarDataOutput output) {
        output.add("hasChargedArchaics", entity.hasChargedArchaics() ? 1 : 0);
        output.add("archaicRaidAlive", this.countChargedArchaics());
        output.add("archaicRaidTotal", this.archaicsIntended);
        output.add("archaicPhase", entity.getPhase());
    }

    public void load(ValueInput input) {
        this.phasesTriggered = input.getIntOr("hurlArchaicsCount", 0);
        this.archaicsIntended = input.getIntOr("hurlArchaicsIntended", 0);
    }

    public void save(ValueOutput output) {
        output.putInt("hurlArchaicsCount", this.phasesTriggered);
        output.putInt("hurlArchaicsIntended", this.archaicsIntended);
    }
}
