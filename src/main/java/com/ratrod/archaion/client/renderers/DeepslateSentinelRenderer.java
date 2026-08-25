package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import com.ratrod.archaion.client.renderers.layer.DeepslateSentinelChargedLayer;
import com.ratrod.archaion.entities.DeepslateSentinel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DeepslateSentinelRenderer extends MobRenderer<DeepslateSentinel, DeepslateSentinelModel> {

    private static final ResourceLocation TEXTURE_LOCATION = Archaion.prefix("textures/entity/deepslate_sentinel.png");

    public DeepslateSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new DeepslateSentinelModel(context.bakeLayer(DeepslateSentinelModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new DeepslateSentinelChargedLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(DeepslateSentinel entity) {
        return TEXTURE_LOCATION;
    }
}
