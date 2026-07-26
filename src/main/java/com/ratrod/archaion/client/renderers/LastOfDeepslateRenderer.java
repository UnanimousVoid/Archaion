package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.ACLivingEntityRenderState;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslateEntity, ACLivingEntityRenderState, LastOfDeepslateModel<ACLivingEntityRenderState>> {

    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate_glow.png");

    public LastOfDeepslateRenderer(EntityRendererProvider.Context context) {
        super(context, new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)), 5F);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        renderState -> GLOW_TEXTURE,
                        (entity, ageInTicks) -> Mth.clamp(1F + Mth.sin(ageInTicks * 0.25F), 0.0F, 1.0F),
                        new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
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
