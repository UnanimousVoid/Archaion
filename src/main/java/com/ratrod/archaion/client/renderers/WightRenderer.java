package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.WightRenderState;
import com.ratrod.archaion.client.renderers.layer.WightChargedLayer;
import com.ratrod.archaion.entities.Wight;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class WightRenderer extends AbstractSkeletonRenderer<Wight, WightRenderState> {
    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/wight.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/wight_eyes.png");

    public WightRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_ARMOR);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        renderState -> GLOW_TEXTURE,
                        (entity, ageInTicks) -> 1.0F,
                        new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
        this.addLayer(new WightChargedLayer(this, context.getModelSet()));
    }

    public Identifier getTextureLocation(WightRenderState state) {
        return TEXTURE;
    }

    public WightRenderState createRenderState() {
        return new WightRenderState();
    }

    @Override
    public void extractRenderState(Wight entity, WightRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isCharged = entity.isCharged();
    }
}
