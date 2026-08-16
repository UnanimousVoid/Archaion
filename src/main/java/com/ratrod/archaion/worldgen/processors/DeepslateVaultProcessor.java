package com.ratrod.archaion.worldgen.processors;

import com.mojang.serialization.MapCodec;
import com.ratrod.archaion.registry.ACTrialVariants;
import com.ratrod.archaion.registry.ACStructureProcessorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public class DeepslateVaultProcessor extends StructureProcessor {
    public static final MapCodec<DeepslateVaultProcessor> CODEC = MapCodec.unit(DeepslateVaultProcessor::new);

    @Override
    public StructureTemplate.@Nullable StructureBlockInfo process(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (processedBlockInfo.nbt() != null && (processedBlockInfo.state().is(Blocks.VAULT) || ACTrialVariants.isVaultBlock(processedBlockInfo.state().getBlock()))) {
            CompoundTag nbt = processedBlockInfo.nbt().copy();
            nbt.remove("server_data");
            nbt.remove("shared_data");
            return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), processedBlockInfo.state(), nbt);
        }
        return processedBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ACStructureProcessorTypes.DEEPSLATE_VAULT.get();
    }
}
