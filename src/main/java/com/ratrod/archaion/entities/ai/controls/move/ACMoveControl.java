package com.ratrod.archaion.entities.ai.controls.move;

import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class ACMoveControl<T extends Mob & ACEntity> extends MoveControl {

    protected final T entity;

    public ACMoveControl(T entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
        return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * entity.getRotationFreedom());
    }
}
