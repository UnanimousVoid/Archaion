package com.ratrod.archaion.entities.ai.controls.look;

import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.actions.LODSmashGroundAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;

public class LastOfDeepslateLookControl extends LookControl {

    private static final float MAX_YAW_PER_TICK = 60.0F;
    private static final float MAX_PITCH_PER_TICK = 40.0F;
    private static final float YAW_EASING = 0.4F;
    private static final float PITCH_EASING = 0.25F;
    private static final float IDLE_RETURN_PER_TICK = 40.0F;

    public LastOfDeepslateLookControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive()) {

            if (mob instanceof LastOfDeepslate lod && lod.attackManager.getCurrentAction() instanceof LODSmashGroundAction action) {
                if (action.getTimer() <= 27) return;
            }

            Vec3 lookDir = target.getEyePosition().subtract(this.mob.getEyePosition()).normalize();

            float targetYaw = (float) (Mth.atan2(-lookDir.x, lookDir.z) * Mth.RAD_TO_DEG);
            float targetPitch = (float) (-(Mth.atan2(lookDir.y, Math.hypot(lookDir.x, lookDir.z)) * Mth.RAD_TO_DEG));

            float easedYaw = Mth.rotLerp(YAW_EASING, this.mob.yHeadRot, targetYaw);
            this.mob.yHeadRot += Mth.clamp(Mth.degreesDifference(this.mob.yHeadRot, easedYaw), -MAX_YAW_PER_TICK, MAX_YAW_PER_TICK);
            this.mob.setYRot(this.mob.getYHeadRot());

            float easedPitch = Mth.rotLerp(PITCH_EASING, this.mob.getXRot(), targetPitch);
            this.mob.setXRot(this.mob.getXRot() + Mth.clamp(Mth.degreesDifference(this.mob.getXRot(), easedPitch), -MAX_PITCH_PER_TICK, MAX_PITCH_PER_TICK));
        } else {
            this.mob.yHeadRot += Mth.clamp(Mth.degreesDifference(this.mob.yHeadRot, this.mob.yBodyRot), -IDLE_RETURN_PER_TICK, IDLE_RETURN_PER_TICK);
        }
    }
}
