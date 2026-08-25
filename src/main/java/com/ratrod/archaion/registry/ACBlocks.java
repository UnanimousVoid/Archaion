package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.HologramBlock;
import com.ratrod.archaion.block.ReinforcedBarBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ACBlocks {
    public static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(Archaion.MODID);

    public static final DeferredBlock<Block> REINFORCED_POLISHED_DEEPSLATE = registerBlock("reinforced_polished_deepslate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_BRICKS = registerBlock("reinforced_deepslate_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_TILES = registerBlock("reinforced_deepslate_tiles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_PILLAR = registerBlock("reinforced_deepslate_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> DEEPSLATE_PILLAR = registerBlock("deepslate_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(4.5F, 1200.0F)));
    public static final DeferredBlock<Block> SOUL_LAMP = registerBlock("soul_lamp", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).lightLevel((state) -> 15)));
    public static final DeferredBlock<Block> REINFORCED_GRATE = registerBlock("reinforced_grate", () -> new WaterloggedTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.COPPER_GRATE).strength(55.0F, 1200.0F).noOcclusion()));
    public static final DeferredBlock<Block> REINFORCED_CHAIN = registerBlock("reinforced_chain", () -> new ChainBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.CHAIN).strength(55.0F, 1200.0F).noOcclusion()));

    public static final DeferredBlock<StairBlock> REINFORCED_POLISHED_DEEPSLATE_STAIRS = registerStairs("reinforced_polished_deepslate_stairs", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<StairBlock> REINFORCED_DEEPSLATE_BRICK_STAIRS = registerStairs("reinforced_deepslate_brick_stairs", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<StairBlock> REINFORCED_DEEPSLATE_TILE_STAIRS = registerStairs("reinforced_deepslate_tile_stairs", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<SlabBlock> REINFORCED_POLISHED_DEEPSLATE_SLAB = registerSlabs("reinforced_polished_deepslate_slab", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<SlabBlock> REINFORCED_DEEPSLATE_BRICK_SLAB = registerSlabs("reinforced_deepslate_brick_slab", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<SlabBlock> REINFORCED_DEEPSLATE_TILE_SLAB = registerSlabs("reinforced_deepslate_tile_slab", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<WallBlock> REINFORCED_POLISHED_DEEPSLATE_WALL = registerWalls("reinforced_polished_deepslate_wall", REINFORCED_POLISHED_DEEPSLATE);
    public static final DeferredBlock<WallBlock> REINFORCED_DEEPSLATE_BRICK_WALL = registerWalls("reinforced_deepslate_brick_wall", REINFORCED_DEEPSLATE_BRICKS);
    public static final DeferredBlock<WallBlock> REINFORCED_DEEPSLATE_TILE_WALL = registerWalls("reinforced_deepslate_tile_wall", REINFORCED_DEEPSLATE_TILES);

    public static final DeferredBlock<ReinforcedBarBlock> REINFORCED_BARS = registerBlock("reinforced_bars", () -> new ReinforcedBarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));

    public static final DeferredBlock<HologramBlock> DEEPSLATE_HOLOGRAM = registerBlock("deepslate_hologram", () -> new HologramBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).noOcclusion().strength(4.5F, 1200.0F)));

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, new Item.Properties());
    }

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, Item.Properties properties) {
        DeferredBlock<T> registeredBlock = ACBlocks.BLOCK.register(name, block);
        ACItems.ITEM.register(name, () -> new BlockItem(registeredBlock.get(), properties));
        return registeredBlock;
    }

    public static <T extends Block> DeferredBlock<StairBlock> registerStairs(String name, DeferredBlock<T> block) {
        return registerBlock(name, () -> new StairBlock(((Block)block.get()).defaultBlockState(), BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)block.get())));
    }

    public static <T extends Block> DeferredBlock<SlabBlock> registerSlabs(String string, DeferredBlock<T> block) {
        return registerBlock(string, () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(block.get())), new Item.Properties());
    }

    public static <T extends Block> DeferredBlock<WallBlock> registerWalls(String name, DeferredBlock<T> block) {
        return registerBlock(name, () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL)));
    }
}
