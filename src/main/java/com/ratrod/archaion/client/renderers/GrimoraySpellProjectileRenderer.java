package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.projectile.GrimoraySpellProjectile;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GrimoraySpellProjectileRenderer extends EntityRenderer<GrimoraySpellProjectile> {

    public GrimoraySpellProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(GrimoraySpellProjectile entity) {
        return null;
    }

    @Override
    public boolean shouldRender(GrimoraySpellProjectile entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
