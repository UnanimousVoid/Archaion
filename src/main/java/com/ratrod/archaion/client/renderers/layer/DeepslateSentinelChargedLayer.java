package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import com.ratrod.archaion.entities.DeepslateSentinel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class DeepslateSentinelChargedLayer extends EnergySwirlLayer<DeepslateSentinel, DeepslateSentinelModel> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/last_of_deepslate_charged.png");
    private final DeepslateSentinelModel model;

    public DeepslateSentinelChargedLayer(RenderLayerParent<DeepslateSentinel, DeepslateSentinelModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new DeepslateSentinelModel(modelSet.bakeLayer(DeepslateSentinelModel.LAYER_LOCATION));
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected DeepslateSentinelModel model() {
        return this.model;
    }
}
