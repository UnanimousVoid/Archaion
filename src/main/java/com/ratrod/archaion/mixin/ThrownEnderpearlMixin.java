package com.ratrod.archaion.mixin;

import com.ratrod.archaion.entities.projectile.ThrownImpactPearl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderpearlMixin {

    @Inject(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
                    shift = At.Shift.BEFORE
            )
    )
    protected void onHit(HitResult hitResult, CallbackInfo ci) {
        if ((Object) this instanceof ThrownImpactPearl pearl && pearl.level() instanceof ServerLevel level) {
            pearl.impact(level);
        }
    }
}
