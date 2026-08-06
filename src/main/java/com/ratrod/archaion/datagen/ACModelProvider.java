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

        itemModels.generateFlatItem(ACItems.ECHO_MACE.get(), ModelTemplates.FLAT_HANDHELD_MACE_ITEM);
        itemModels.generateFlatItem(ACItems.ECHO_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.SLATED_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.WIGHT_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.BRAVE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.DEEPSLATE_SENTINEL_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.LAST_OF_DEEPSLATE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.ECHO_MACE_UPGRADE_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.BRAVE_ROD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.BRAVE_ESSENCE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.IMPACT_PEARL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ACItems.ECHO_CHARGE.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(ACBlocks.SOUL_LAMP.get());
        blockModels.createTrivialCube(ACBlocks.REINFORCED_GRATE.get());
        blockModels.createAxisAlignedPillarBlockCustomModel(ACBlocks.REINFORCED_CHAIN.get(), BlockModelGenerators.plainVariant(TexturedModel.CHAIN.create(ACBlocks.REINFORCED_CHAIN.get(), blockModels.modelOutput)));
        itemModels.generateFlatItem(ACBlocks.REINFORCED_CHAIN.get().asItem(), ModelTemplates.FLAT_ITEM);

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

    private void createDeepslateSpawner(BlockModelGenerators blockModels) {
        DeepslateSpawnerBlock block = ACBlocks.DEEPSLATE_SPAWNER.get();

        Identifier inactiveModel = ModelLocationUtils.getModelLocation(block);
        Identifier activeModel = ModelLocationUtils.getModelLocation(block, "_active");
        Identifier ejectingModel = ModelLocationUtils.getModelLocation(block, "_ejecting_reward");

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(DeepslateSpawnerBlock.STATE).generate(state -> switch (state) {
                            case INACTIVE, COOLDOWN -> BlockModelGenerators.plainVariant(inactiveModel);
                            case WAITING_FOR_PLAYERS, ACTIVE, WAITING_FOR_REWARD_EJECTION -> BlockModelGenerators.plainVariant(activeModel);
                            case EJECTING_REWARD -> BlockModelGenerators.plainVariant(ejectingModel);
                        }))
        );
    }

    private void createDeepslateVault(BlockModelGenerators blockModels) {
        DeepslateVaultBlock block = ACBlocks.DEEPSLATE_VAULT.get();
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(VaultBlock.STATE).generate(state -> switch (state) {
                            case INACTIVE -> BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block));
                            case ACTIVE -> BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, "_active"));
                            case UNLOCKING -> BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, "_unlocking"));
                            case EJECTING -> BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block, "_ejecting_reward"));
                        }))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }
}
