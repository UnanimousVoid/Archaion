package com.ratrod.archaion.entities.ai.controls.move;

import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class ACMoveControl<T extends Mob & ACEntity> extends MoveControl {

    protected final T entity;

    public ACMoveControl(T entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    protected float rotlerp(float sourceAngle, float targetAngle, float maxChange) {
        return super.rotlerp(sourceAngle, targetAngle, maxChange * entity.getRotationFreedom());
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            double dx = this.wantedX - this.mob.getX();
            double dz = this.wantedZ - this.mob.getZ();
            float targetYaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;
            float yawDiff = Math.abs(Mth.wrapDegrees(targetYaw - this.mob.getYRot()));

            // Scale speed by how aligned the entity is with target direction.
            //  0° off → 100% speed
            // 45° off →  50% speed
            // 90°+ off →  0% speed (rotate in place)
            float alignment = Math.max(0.0F, 1.0F - yawDiff / 90.0F);
            double savedModifier = this.speedModifier;
            this.speedModifier *= alignment;
            super.tick();
            this.speedModifier = savedModifier;
        } else {
            super.tick();
        }
    }
}
