package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.HaunterModel;
import com.ratrod.archaion.client.renderers.layer.ACEmissiveLayer;
import com.ratrod.archaion.client.renderers.layer.HaunterChargedLayer;
import com.ratrod.archaion.entities.Haunter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HaunterRenderer extends MobRenderer<Haunter, HaunterModel> {

    private static final ResourceLocation TEXTURE = Archaion.prefix("textures/entity/haunter.png");

    public HaunterRenderer(EntityRendererProvider.Context context) {
        super(context, new HaunterModel(context.bakeLayer(HaunterModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(new ACEmissiveLayer<>(this, new HaunterModel(context.bakeLayer(HaunterModel.LAYER_LOCATION)), TEXTURE, haunter -> 1.0F));
        this.addLayer(new HaunterChargedLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Haunter entity) {
        return TEXTURE;
    }

    @Override
    protected float getWhiteOverlayProgress(Haunter entity, float partialTicks) {
        float value = 0.75F + Mth.sin(entity.tickCount * 1.0F) * 0.25F;
        return entity.isSwelling() ? Mth.clamp(value, 0.5F, 1.0F) : 0.0F;
    }
}