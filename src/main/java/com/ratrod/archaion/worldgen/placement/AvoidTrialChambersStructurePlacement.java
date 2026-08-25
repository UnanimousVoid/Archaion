package com.ratrod.archaion.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ratrod.archaion.registry.ACStructurePlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class AvoidTrialChambersStructurePlacement extends RandomSpreadStructurePlacement {
    public static final int DEFAULT_TRIAL_CHAMBERS_MIN_DISTANCE = 16;
    public static final MapCodec<AvoidTrialChambersStructurePlacement> CODEC = RecordCodecBuilder.<AvoidTrialChambersStructurePlacement>mapCodec(instance -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(AvoidTrialChambersStructurePlacement::locateOffset),
            FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", FrequencyReductionMethod.DEFAULT).forGetter(AvoidTrialChambersStructurePlacement::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(AvoidTrialChambersStructurePlacement::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(AvoidTrialChambersStructurePlacement::salt),
            ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(AvoidTrialChambersStructurePlacement::exclusionZone),
            Codec.intRange(0, 4096).fieldOf("spacing").forGetter(AvoidTrialChambersStructurePlacement::spacing),
            Codec.intRange(0, 4096).fieldOf("separation").forGetter(AvoidTrialChambersStructurePlacement::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(AvoidTrialChambersStructurePlacement::spreadType),
            Codec.intRange(0, 4096).optionalFieldOf("trial_chambers_min_distance", DEFAULT_TRIAL_CHAMBERS_MIN_DISTANCE).forGetter(AvoidTrialChambersStructurePlacement::trialChambersMinDistance)
    ).apply(instance, AvoidTrialChambersStructurePlacement::new)).validate(AvoidTrialChambersStructurePlacement::validate);

    private static final ResourceKey<StructureSet> TRIAL_CHAMBERS_STRUCTURE_SET = ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath("minecraft", "trial_chambers"));

    private final int trialChambersMinDistance;

    private static DataResult<AvoidTrialChambersStructurePlacement> validate(AvoidTrialChambersStructurePlacement placement) {
        return placement.spacing() <= placement.separation()
                ? DataResult.error(() -> "Spacing has to be larger than separation")
                : DataResult.success(placement);
    }

    public AvoidTrialChambersStructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone, int spacing, int separation, RandomSpreadType spreadType, int trialChambersMinDistance) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
        this.trialChambersMinDistance = trialChambersMinDistance;
    }

    public int trialChambersMinDistance() {
        return this.trialChambersMinDistance;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        if (!super.isPlacementChunk(state, sourceX, sourceZ)) {
            return false;
        }
        return this.trialChambersMinDistance <= 0 || !this.isTooCloseToTrialChambers(state, sourceX, sourceZ);
    }

    private boolean isTooCloseToTrialChambers(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        for (Holder<StructureSet> structureSet : state.possibleStructureSets()) {
            if (structureSet.is(TRIAL_CHAMBERS_STRUCTURE_SET)) {
                return state.hasStructureChunkInRange(structureSet, sourceX, sourceZ, this.trialChambersMinDistance);
            }
        }
        return false;
    }

    @Override
    public StructurePlacementType<?> type() {
        return ACStructurePlacements.AVOID_TRIAL_CHAMBERS.get();
    }
}
