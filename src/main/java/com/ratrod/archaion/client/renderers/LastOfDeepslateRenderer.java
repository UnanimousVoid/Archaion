package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.ACLivingEntityRenderState;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslateEntity, ACLivingEntityRenderState, LastOfDeepslateModel<ACLivingEntityRenderState>> {

    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate.png");

    public LastOfDeepslateRenderer(EntityRendererProvider.Context context) {
        super(context, new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)), 5F);
    }

    @Override
    public ACLivingEntityRenderState createRenderState() {
        return new ACLivingEntityRenderState();
    }

    @Override
    public void extractRenderState(LastOfDeepslateEntity entity, ACLivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
    }

    @Override
    public Identifier getTextureLocation(ACLivingEntityRenderState state) {
        return TEXTURE;
    }
}
