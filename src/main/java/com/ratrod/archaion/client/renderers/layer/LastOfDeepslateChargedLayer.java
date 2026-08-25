package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.entities.LastOfDeepslate;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class LastOfDeepslateChargedLayer extends EnergySwirlLayer<LastOfDeepslate, LastOfDeepslateModel> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/last_of_deepslate_charged.png");
    private final LastOfDeepslateModel model;

    public LastOfDeepslateChargedLayer(RenderLayerParent<LastOfDeepslate, LastOfDeepslateModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new LastOfDeepslateModel(modelSet.bakeLayer(LastOfDeepslateModel.CHARGED_LAYER_LOCATION));
    }

    protected float xOffset(float t) {
        return t * 0.005F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected LastOfDeepslateModel model() {
        return this.model;
    }
}
