package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.BraveModel;
import com.ratrod.archaion.entities.Brave;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class BraveChargedLayer extends EnergySwirlLayer<Brave, BraveModel> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final BraveModel model;

    public BraveChargedLayer(RenderLayerParent<Brave, BraveModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BraveModel(modelSet.bakeLayer(BraveModel.CHARGED_LAYER_LOCATION));
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected BraveModel model() {
        return this.model;
    }
}
