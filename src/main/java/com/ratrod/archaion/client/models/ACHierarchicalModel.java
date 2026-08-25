package com.ratrod.archaion.client.models;

import com.ratrod.archaion.client.animations.ClientAnimationRegistry;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public abstract class ACHierarchicalModel<E extends LivingEntity & ACEntity> extends HierarchicalModel<E> {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private final ModelPart root;

    protected ACHierarchicalModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void animateManager(E entity, float ageInTicks) {
        entity.getAnimationManager().getAnimationMap().forEach((id, animation) -> {
            AnimationDefinition definition = ClientAnimationRegistry.get(entity.getType(), id);
            if (definition != null) {
                AnimationState state = animation.getState();
                if (state.isStarted() && !definition.looping() && state.getAccumulatedTime() / 1000.0F >= definition.lengthInSeconds()) {
                    state.stop();
                }
                this.animate(state, definition, ageInTicks);
            }
        });
    }

    protected void animateScaled(AnimationDefinition animationDefinition, float ageInTicks, float speed, float scale) {
        long i = (long) (ageInTicks * 50.0F * speed);
        float f = Math.max(0.0F, scale);
        KeyframeAnimations.animate(this, animationDefinition, i, f, ANIMATION_VECTOR_CACHE);
    }
}
