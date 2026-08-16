package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class EchoStarProjectileRenderer extends EntityRenderer<EchoStarProjectile, EntityRenderState> {

    public EchoStarProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public boolean shouldRender(EchoStarProjectile entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
