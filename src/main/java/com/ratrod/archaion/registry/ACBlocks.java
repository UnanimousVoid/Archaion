package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.DeepslateSpawnerBlock;
import com.ratrod.archaion.block.DeepslateVaultBlock;
import com.ratrod.archaion.block.ReinforcedBarBlock;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ACBlocks {
    public static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(Archaion.MODID);

    public static final DeferredBlock<Block> REINFORCED_POLISHED_DEEPSLATE = registerBlock("reinforced_polished_deepslate", Block::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_BRICKS = registerBlock("reinforced_deepslate_bricks", Block::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_TILES = registerBlock("reinforced_deepslate_tiles", Block::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_PILLAR = registerBlock("reinforced_deepslate_pillar", RotatedPillarBlock::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> DEEPSLATE_PILLAR = registerBlock("deepslate_pillar", RotatedPillarBlock::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));
    public static final DeferredBlock<Block> SOUL_LAMP = registerBlock("soul_lamp", Block::new, properties -> properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).lightLevel((statex) -> 15));

    public static final DeferredBlock<StairBlock> REINFORCED_POLISHED_DEEPSLATE_STAIRS = registerStairs("reinforced_polished_deepslate_stairs", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<StairBlock> REINFORCED_DEEPSLATE_BRICK_STAIRS = registerStairs("reinforced_deepslate_brick_stairs", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<StairBlock> REINFORCED_DEEPSLATE_TILE_STAIRS = registerStairs("reinforced_deepslate_tile_stairs", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<SlabBlock> REINFORCED_POLISHED_DEEPSLATE_SLAB = registerSlabs("reinforced_polished_deepslate_slab", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<SlabBlock> REINFORCED_DEEPSLATE_BRICK_SLAB = registerSlabs("reinforced_deepslate_brick_slab", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<SlabBlock> REINFORCED_DEEPSLATE_TILE_SLAB = registerSlabs("reinforced_deepslate_tile_slab", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<WallBlock> REINFORCED_POLISHED_DEEPSLATE_WALL = registerWalls("reinforced_polished_deepslate_wall", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<WallBlock> REINFORCED_DEEPSLATE_BRICK_WALL = registerWalls("reinforced_deepslate_brick_wall", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<WallBlock> REINFORCED_DEEPSLATE_TILE_WALL = registerWalls("reinforced_deepslate_tile_wall", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<ReinforcedBarBlock> REINFORCED_BARS = registerBlock("reinforced_bars", ReinforcedBarBlock::new, properties -> properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F));

    public static final DeferredBlock<DeepslateVaultBlock> DEEPSLATE_VAULT = registerBlock("deepslate_vault", DeepslateVaultBlock::new, properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.VAULT).strength(-1.0F, 3600000.0F).noOcclusion());
    public static final DeferredBlock<DeepslateSpawnerBlock> DEEPSLATE_SPAWNER = registerBlock("deepslate_spawner", DeepslateSpawnerBlock::new, properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.TRIAL_SPAWNER).strength(50.0F, 1200.0F).noOcclusion().lightLevel(state -> state.getValue(DeepslateSpawnerBlock.STATE).lightLevel()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
        DeferredBlock<T> registeredBlock = BLOCK.registerBlock(name, factory, properties::apply);
        ACItems.ITEM.registerSimpleBlockItem(registeredBlock);
        return registeredBlock;
    }

    public static <T extends Block> DeferredBlock<StairBlock> registerStairs(String name, DeferredBlock<T> block) {
        return registerBlock(name, properties -> new StairBlock(block.get().defaultBlockState(), properties), properties -> BlockBehaviour.Properties.ofLegacyCopy(block.get()));
    }

    public static <T extends Block> DeferredBlock<SlabBlock> registerSlabs(String name, DeferredBlock<T> block) {
        return registerBlock(name, SlabBlock::new, properties -> BlockBehaviour.Properties.ofLegacyCopy(block.get()));
    }

    public static <T extends Block> DeferredBlock<WallBlock> registerWalls(String name, DeferredBlock<T> block) {
        return registerBlock(name, WallBlock::new, properties -> BlockBehaviour.Properties.ofLegacyCopy(block.get()));
    }
}
