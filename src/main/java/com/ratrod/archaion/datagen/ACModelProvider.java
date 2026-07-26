package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.ReinforcedBarBlock;
import com.ratrod.archaion.registry.ACBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.core.Direction;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;

public class ACModelProvider extends ModelProvider {
    public ACModelProvider(PackOutput output) {
        super(output, Archaion.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
//        blockModels.createTrivialCube(ACBlocks.REINFORCED_POLISHED_DEEPSLATE.get());
//        blockModels.createTrivialCube(ACBlocks.REINFORCED_DEEPSLATE_BRICKS.get());
//        blockModels.createTrivialCube(ACBlocks.REINFORCED_DEEPSLATE_TILES.get());
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

    }
}
