package com.ratrod.archaion.mixin;

import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void ac$tickActionManagers(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) return;
        if (self instanceof Mob mob && mob.isNoAi()) return;
        if (self instanceof ACEntity<?> acEntity) {
            acEntity.getActionManagers().forEach(ActionManager::tick);
        }
    }
}
