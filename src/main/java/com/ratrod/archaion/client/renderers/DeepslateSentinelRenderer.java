package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.ACLivingEntityRenderState;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import com.ratrod.archaion.entities.DeepslateSentinelEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class DeepslateSentinelRenderer extends MobRenderer<DeepslateSentinelEntity, ACLivingEntityRenderState, DeepslateSentinelModel<ACLivingEntityRenderState>> {

    private static final Identifier TEXTURE_LOCATION = Archaion.prefix("textures/entity/deepslate_sentinel.png");

    public DeepslateSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new DeepslateSentinelModel<>(context.bakeLayer(DeepslateSentinelModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public Identifier getTextureLocation(ACLivingEntityRenderState state) {
        return TEXTURE_LOCATION;
    }

    @Override
    public ACLivingEntityRenderState createRenderState() {
        return new ACLivingEntityRenderState();
    }

    @Override
    public void extractRenderState(DeepslateSentinelEntity entity, ACLivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
    }
}
