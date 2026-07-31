package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.DeepslateSpawnerBlock;
import com.ratrod.archaion.block.DeepslateVaultBlock;
import com.ratrod.archaion.block.ReinforcedBarBlock;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.VaultBlock;

public class ACModelProvider extends ModelProvider {

    public ACModelProvider(PackOutput output) {
        super(output, Archaion.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(ACItems.ECHO_KEY.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(ACBlocks.SOUL_LAMP.get());
        blockModels.createAxisAlignedPillarBlock(ACBlocks.REINFORCED_DEEPSLATE_PILLAR.get(), TexturedModel.COLUMN);
        blockModels.createAxisAlignedPillarBlock(ACBlocks.DEEPSLATE_PILLAR.get(), TexturedModel.COLUMN);
        itemModels.generateFlatItem(ACBlocks.REINFORCED_BARS.get().asItem(), ModelTemplates.FLAT_ITEM);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ACBlocks.REINFORCED_BARS.get()).with(PropertyDispatch.initial(ReinforcedBarBlock.BELOW, ReinforcedBarBlock.FACING)
                                .select(false, Direction.NORTH, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get())))
                                .select(false, Direction.EAST, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get())).with(BlockModelGenerators.Y_ROT_90))
                                .select(false, Direction.SOUTH, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get())).with(BlockModelGenerators.Y_ROT_180))
                                .select(false, Direction.WEST, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get())).with(BlockModelGenerators.Y_ROT_270))
                                .select(true, Direction.NORTH, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get(), "_spiky")))
                                .select(true, Direction.EAST, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get(), "_spiky")).with(BlockModelGenerators.Y_ROT_90))
                                .select(true, Direction.SOUTH, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get(), "_spiky")).with(BlockModelGenerators.Y_ROT_180))
                                .select(true, Direction.WEST, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(ACBlocks.REINFORCED_BARS.get(), "_spiky")).with(BlockModelGenerators.Y_ROT_270)))
        );
        blockModels.family(ACBlocks.REINFORCED_POLISHED_DEEPSLATE.get()).stairs(ACBlocks.REINFORCED_POLISHED_DEEPSLATE_STAIRS.get()).slab(ACBlocks.REINFORCED_POLISHED_DEEPSLATE_SLAB.get()).wall(ACBlocks.REINFORCED_POLISHED_DEEPSLATE_WALL.get());
        blockModels.family(ACBlocks.REINFORCED_DEEPSLATE_BRICKS.get()).stairs(ACBlocks.REINFORCED_DEEPSLATE_BRICK_STAIRS.get()).slab(ACBlocks.REINFORCED_DEEPSLATE_BRICK_SLAB.get()).wall(ACBlocks.REINFORCED_DEEPSLATE_BRICK_WALL.get());
        blockModels.family(ACBlocks.REINFORCED_DEEPSLATE_TILES.get()).stairs(ACBlocks.REINFORCED_DEEPSLATE_TILE_STAIRS.get()).slab(ACBlocks.REINFORCED_DEEPSLATE_TILE_SLAB.get()).wall(ACBlocks.REINFORCED_DEEPSLATE_TILE_WALL.get());

        createDeepslateVault(blockModels);
        createDeepslateSpawner(blockModels);
    }

    private void createDeepslateVault(BlockModelGenerators blockModels) {
        DeepslateVaultBlock block = ACBlocks.DEEPSLATE_VAULT.get();

        TextureMapping inactiveTextures = TextureMapping.vault(block, "_front_off", "_side_off", "_top", "_bottom");
        TextureMapping activeTextures = TextureMapping.vault(block, "_front_on", "_side_on", "_top", "_bottom");
        TextureMapping unlockingTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top", "_bottom");
        TextureMapping ejectingTextures = TextureMapping.vault(block, "_front_ejecting", "_side_on", "_top_ejecting", "_bottom");

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(VaultBlock.STATE).generate(state -> switch (state) {
                            case INACTIVE -> BlockModelGenerators.plainVariant(ModelTemplates.VAULT.create(block, inactiveTextures, blockModels.modelOutput));
                            case ACTIVE -> BlockModelGenerators.plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_active", activeTextures, blockModels.modelOutput));
                            case UNLOCKING -> BlockModelGenerators.plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_unlocking", unlockingTextures, blockModels.modelOutput));
                            case EJECTING -> BlockModelGenerators.plainVariant(ModelTemplates.VAULT.createWithSuffix(block, "_ejecting_reward", ejectingTextures, blockModels.modelOutput));
                        }))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

private void createDeepslateSpawner(BlockModelGenerators blockModels) {
        DeepslateSpawnerBlock block = ACBlocks.DEEPSLATE_SPAWNER.get();

        TextureMapping inactiveTextures = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
        TextureMapping activeTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
        TextureMapping ejectingRewardTextures = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");

        Identifier inactiveModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, inactiveTextures, blockModels.modelOutput);
        Identifier activeModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", activeTextures, blockModels.modelOutput);
        Identifier ejectingModel = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_ejecting_reward", ejectingRewardTextures, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(DeepslateSpawnerBlock.STATE).generate(state -> switch (state) {
                            case INACTIVE, COOLDOWN -> BlockModelGenerators.plainVariant(inactiveModel);
                            case WAITING_FOR_PLAYERS, ACTIVE, WAITING_FOR_REWARD_EJECTION -> BlockModelGenerators.plainVariant(activeModel);
                            case EJECTING_REWARD -> BlockModelGenerators.plainVariant(ejectingModel);
                        }))
        );
    }
}
