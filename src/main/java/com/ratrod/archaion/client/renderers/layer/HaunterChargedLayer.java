package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.HaunterModel;
import com.ratrod.archaion.entities.Haunter;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class HaunterChargedLayer extends EnergySwirlLayer<Haunter, HaunterModel> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final HaunterModel model;

    public HaunterChargedLayer(RenderLayerParent<Haunter, HaunterModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new HaunterModel(modelSet.bakeLayer(HaunterModel.CHARGED_LAYER_LOCATION));
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected HaunterModel model() {
        return this.model;
    }
}
