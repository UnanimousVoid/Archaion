package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ratrod.archaion.api.trial.ACTrialSpawnerBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.phys.AABB;

public class TrialSpawnerRenderer implements BlockEntityRenderer<ACTrialSpawnerBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public TrialSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(ACTrialSpawnerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() != null) {
            TrialSpawner spawner = blockEntity.getTrialSpawner();
            Entity displayEntity = spawner.getData().getOrCreateDisplayEntity(spawner, blockEntity.getLevel(), spawner.getState());
            if (displayEntity != null) {
                SpawnerRenderer.renderEntityInSpawner(
                        partialTick, poseStack, buffer, packedLight, displayEntity, this.entityRenderer,
                        spawner.getData().getOSpin(), spawner.getData().getSpin()
                );
            }
        }
    }

    @Override
    public AABB getRenderBoundingBox(ACTrialSpawnerBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY() - 1.0, pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + 2.0, pos.getZ() + 2.0);
    }
}
