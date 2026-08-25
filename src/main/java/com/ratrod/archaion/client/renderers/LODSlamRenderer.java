package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.projectile.LODSlamEffect;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LODSlamRenderer extends EntityRenderer<LODSlamEffect> {

    public LODSlamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LODSlamEffect entity) {
        return null;
    }

    @Override
    public boolean shouldRender(LODSlamEffect entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
