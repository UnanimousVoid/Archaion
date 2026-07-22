package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ACBlocks {
    public static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(Archaion.MODID);

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, new Item.Properties());
    }

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, Item.Properties properties) {
        DeferredBlock<T> registeredBlock = ACBlocks.BLOCK.register(name, block);
        ACItems.ITEM.register(name, () -> new BlockItem(registeredBlock.get(), properties));
        return registeredBlock;
    }
}
