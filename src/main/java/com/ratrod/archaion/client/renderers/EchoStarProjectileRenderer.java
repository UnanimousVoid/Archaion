package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EchoStarProjectileRenderer extends EntityRenderer<EchoStarProjectile> {

    public EchoStarProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EchoStarProjectile entity) {
        return null;
    }

    @Override
    public boolean shouldRender(EchoStarProjectile entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
