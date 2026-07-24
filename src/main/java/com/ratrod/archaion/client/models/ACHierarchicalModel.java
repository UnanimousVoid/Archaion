package com.ratrod.archaion.client.models;

import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public abstract class ACHierarchicalModel<E extends LivingEntity & ACEntity> extends Model<E> {

    protected ACHierarchicalModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    public void animateManager(E entity, float ageInTicks) {
        entity.getAnimationManager().getAnimationStateMap().forEach((name, pair) -> {
            Supplier<?> second = pair.second();
            if (second.get() instanceof AnimationDefinition definition) {
                definition.bake(this.root()).apply(pair.first(), ageInTicks);
            }
        });
    }

    protected void animateScaled(AnimationDefinition animationDefinition, float ageInTicks, float speed, float scale) {
        KeyframeAnimation animation = animationDefinition.bake(this.root());
        animation.apply((long) (ageInTicks * 50.0F * speed), Math.max(0.0F, scale));
    }
}
