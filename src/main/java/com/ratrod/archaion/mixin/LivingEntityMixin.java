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
        if (self instanceof ACEntity<?> acEntity) {
            boolean isNoAi = self instanceof Mob mob && mob.isNoAi();
            boolean serverSide = !self.level().isClientSide();
            if (serverSide && !isNoAi || !serverSide && !isNoAi) {
                acEntity.getActionManagers().forEach(ActionManager::tick);
            }

            if (serverSide && self.isDeadOrDying()) {
                acEntity.getActionManagers().forEach(ActionManager::stopCurrentAction);
            }
        }
    }
}
