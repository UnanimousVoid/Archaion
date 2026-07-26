package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.ReinforcedBarBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ACBlocks {
    public static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(Archaion.MODID);

    public static final DeferredBlock<Block> REINFORCED_POLISHED_DEEPSLATE = registerBlock("reinforced_polished_deepslate", properties -> new Block(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_BRICKS = registerBlock("reinforced_deepslate_bricks", properties -> new Block(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_TILES = registerBlock("reinforced_deepslate_tiles", properties -> new Block(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> REINFORCED_DEEPSLATE_PILLAR = registerBlock("reinforced_deepslate_pillar", properties -> new RotatedPillarBlock(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<Block> DEEPSLATE_PILLAR = registerBlock("deepslate_pillar", properties -> new RotatedPillarBlock(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));
    public static final DeferredBlock<ReinforcedBarBlock> REINFORCED_BARS = registerBlock("reinforced_bars", properties -> new ReinforcedBarBlock(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DEEPSLATE).strength(55.0F, 1200.0F)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCK.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ACItems.ITEM.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
}
