package com.ratrod.archaion.client.models;

import com.ratrod.archaion.client.ACLivingEntityRenderState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.function.Supplier;

public abstract class ACAnimatedModel<S extends ACLivingEntityRenderState> extends EntityModel<S> {

    protected ACAnimatedModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    public void animateManager(S state, float ageInTicks) {
        if (state.animationManager != null) {
            state.animationManager.getAnimationStateMap().forEach((_, pair) -> {
                Supplier<?> second = pair.second();
                if (second.get() instanceof AnimationDefinition definition) {
                    definition.bake(this.root()).apply(pair.first(), ageInTicks);
                }
            });
        }
    }

    protected void animateScaled(AnimationDefinition animationDefinition, float ageInTicks, float speed, float scale) {
        KeyframeAnimation animation = animationDefinition.bake(this.root());
        animation.apply((long) (ageInTicks * 50.0F * speed), Math.max(0.0F, scale));
    }
}
