package com.ratrod.archaion;


import com.mojang.logging.LogUtils;
import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

import java.util.Locale;

@Mod(Archaion.MODID)
public class Archaion {
    public static final String MODID = "archaion";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Archaion(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ACNetwork::register);

        ACBlocks.BLOCK.register(modEventBus);
        ACItems.ITEM.register(modEventBus);
        ACEntityTypes.ENTITY_TYPE.register(modEventBus);

    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }


    public static Identifier prefix(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path.toLowerCase(Locale.ROOT));
    }
}
