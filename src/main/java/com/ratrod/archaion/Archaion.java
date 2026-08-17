package com.ratrod.archaion;


import com.mojang.logging.LogUtils;
import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.registry.*;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Locale;

@Mod(Archaion.MODID)
public class Archaion {
    public static final String MODID = "archaion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Archaion(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(ACNetwork::register);

        ACBlocks.BLOCK.register(modEventBus);
        ACItems.ITEM.register(modEventBus);
        ACEffects.MOB_EFFECT.register(modEventBus);
        ACSounds.SOUND_EVENT.register(modEventBus);
        ACBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ACEntityTypes.ENTITY_TYPE.register(modEventBus);
        ACEntityDataSerializers.ENTITY_DATA_SERIALIZERS.register(modEventBus);
        ACCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ACStructureTypes.STRUCTURE_TYPE.register(modEventBus);
        ACStructurePlacements.STRUCTURE_PLACEMENT.register(modEventBus);
        ACStructureProcessorTypes.STRUCTURE_PROCESSOR.register(modEventBus);
        ACLootModifiers.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);

        ACTrialVariants.initialize();

    }

    public static Identifier prefix(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path.toLowerCase(Locale.ROOT));
    }
}
