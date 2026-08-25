package com.ratrod.archaion.client.renderers.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class ACEmissiveLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final M model;
    private final Function<T, ResourceLocation> textureProvider;
    private final ToDoubleFunction<T> alphaProvider;

    public ACEmissiveLayer(RenderLayerParent<T, M> renderer, M model, ResourceLocation texture, ToDoubleFunction<T> alphaProvider) {
        this(renderer, model, entity -> texture, alphaProvider);
    }

    public ACEmissiveLayer(RenderLayerParent<T, M> renderer, M model, Function<T, ResourceLocation> textureProvider, ToDoubleFunction<T> alphaProvider) {
        super(renderer);
        this.model = model;
        this.textureProvider = textureProvider;
        this.alphaProvider = alphaProvider;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        float alpha = (float) Math.max(0.0, Math.min(1.0, this.alphaProvider.applyAsDouble(entity)));
        if (alpha <= 0.0F) {
            return;
        }
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(this.textureProvider.apply(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, (int) (alpha * 255.0F) << 24 | 0x00FFFFFF);
    }
}
