package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACItems {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(Archaion.MODID);

    public static final DeferredItem<Item> ECHO_KEY = ITEM.registerItem("echo_key", Item::new);

}
