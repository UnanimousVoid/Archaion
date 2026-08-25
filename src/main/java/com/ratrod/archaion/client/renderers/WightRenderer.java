package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.layer.ACEmissiveLayer;
import com.ratrod.archaion.client.renderers.layer.WightChargedLayer;
import com.ratrod.archaion.entities.Wight;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class WightRenderer extends SkeletonRenderer<Wight> {
    private static final ResourceLocation TEXTURE = Archaion.prefix("textures/entity/wight.png");
    private static final ResourceLocation GLOW_TEXTURE = Archaion.prefix("textures/entity/wight_eyes.png");

    public WightRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
        this.addLayer(new ACEmissiveLayer<>(this, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)), GLOW_TEXTURE, entity -> 1.0F));
        this.addLayer(new WightChargedLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Wight entity) {
        return TEXTURE;
    }
}
