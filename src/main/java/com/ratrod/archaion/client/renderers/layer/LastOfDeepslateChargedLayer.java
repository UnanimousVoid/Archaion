package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.LastOfDeepslateRenderState;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class LastOfDeepslateChargedLayer extends EnergySwirlLayer<LastOfDeepslateRenderState, LastOfDeepslateModel<LastOfDeepslateRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/last_of_deepslate_charged.png");
    private final LastOfDeepslateModel<LastOfDeepslateRenderState> model;

    public LastOfDeepslateChargedLayer(RenderLayerParent<LastOfDeepslateRenderState, LastOfDeepslateModel<LastOfDeepslateRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new LastOfDeepslateModel<>(modelSet.bakeLayer(LastOfDeepslateModel.CHARGED_LAYER_LOCATION));
    }

    protected boolean isPowered(LastOfDeepslateRenderState state) {
        return state.hasChargedBraves;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected LastOfDeepslateModel<LastOfDeepslateRenderState> model() {
        return this.model;
    }
}
