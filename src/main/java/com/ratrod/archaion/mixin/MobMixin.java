package com.ratrod.archaion.mixin;

import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.misc.mixinhelpers.IMixinMob;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public abstract class MobMixin implements IMixinMob {

    @Unique
    private Pair<Float, ACEntity.Operation> ac$damageModifier;

    @Override
    public void ac$setDamageModifier(Pair<Float, ACEntity.Operation> modifier) {
        this.ac$damageModifier = modifier;
    }

    @Override
    public Pair<Float, ACEntity.Operation> ac$getDamageModifier() {
        return this.ac$damageModifier;
    }

    @ModifyVariable(
            method = "doHurtTarget",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
            name = "dmg"
    )
    private float ac$modifyDamage(float damage, ServerLevel level, Entity target) {
        if (this.ac$damageModifier != null) {
            float modifier = this.ac$damageModifier.first();
            damage = switch (this.ac$damageModifier.second()) {
                case ADD -> damage + modifier;
                case MULTIPLY -> damage * modifier;
            };
            this.ac$damageModifier = null;
        }
        return damage;
    }
}
