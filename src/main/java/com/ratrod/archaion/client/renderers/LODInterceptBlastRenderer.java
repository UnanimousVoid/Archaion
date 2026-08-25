package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.projectile.LODInterceptBlast;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LODInterceptBlastRenderer extends EntityRenderer<LODInterceptBlast> {

    public LODInterceptBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LODInterceptBlast entity) {
        return null;
    }

    @Override
    public boolean shouldRender(LODInterceptBlast entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
