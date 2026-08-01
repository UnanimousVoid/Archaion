package com.ratrod.archaion.worldgen.structures;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ratrod.archaion.registry.ACStructureTypes;
import com.ratrod.archaion.worldgen.pools.FixedJigsawPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AncientKeepStructure extends Structure {
    public static final DimensionPadding DEFAULT_DIMENSION_PADDING;
    public static final LiquidSettings DEFAULT_LIQUID_SETTINGS;
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 512;
    public static final int MIN_DEPTH = 0;
    public static final int MAX_DEPTH = 64;
    public static final MapCodec<AncientKeepStructure> CODEC;
    private final Holder<StructureTemplatePool> startPool;
    private final Optional<Identifier> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final JigsawStructure.MaxDistance maxDistanceFromCenter;
    private final List<PoolAliasBinding> poolAliases;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;

    private static DataResult<AncientKeepStructure> verifyRange(AncientKeepStructure structure) {
        byte var10000;
        switch (structure.terrainAdaptation()) {
            case NONE:
                var10000 = 0;
                break;
            case BURY:
            case BEARD_THIN:
            case BEARD_BOX:
            case ENCAPSULATE:
                var10000 = 12;
                break;
            default:
                throw new MatchException((String)null, (Throwable)null);
        }

        int edgeNeeded = var10000;
        return structure.maxDistanceFromCenter.horizontal() + edgeNeeded > MAX_TOTAL_STRUCTURE_RANGE ? DataResult.error(() -> "Horizontal structure size including terrain adaptation must not exceed " + MAX_TOTAL_STRUCTURE_RANGE) : DataResult.success(structure);
    }

    public AncientKeepStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<Identifier> startJigsawName, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, JigsawStructure.MaxDistance maxDistanceFromCenter, List<PoolAliasBinding> poolAliases, DimensionPadding dimensionPadding, LiquidSettings liquidSettings) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.poolAliases = poolAliases;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    public AncientKeepStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Heightmap.Types projectStartToHeightmap) {
        this(settings, startPool, Optional.empty(), maxDepth, startHeight, useExpansionHack, Optional.of(projectStartToHeightmap), new JigsawStructure.MaxDistance(80), List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS);
    }

    public AncientKeepStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack) {
        this(settings, startPool, Optional.empty(), maxDepth, startHeight, useExpansionHack, Optional.empty(), new JigsawStructure.MaxDistance(80), List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS);
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int height = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        return FixedJigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, startPos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter, PoolAliasLookup.create(this.poolAliases, startPos, context.seed()), this.dimensionPadding, this.liquidSettings);
    }

    public StructureType<?> type() {
        return ACStructureTypes.ANCIENT_KEEP_STRUCTURE.get();
    }

    @VisibleForTesting
    public Holder<StructureTemplatePool> getStartPool() {
        return this.startPool;
    }

    @VisibleForTesting
    public List<PoolAliasBinding> getPoolAliases() {
        return this.poolAliases;
    }

    static {
        DEFAULT_DIMENSION_PADDING = DimensionPadding.ZERO;
        DEFAULT_LIQUID_SETTINGS = LiquidSettings.APPLY_WATERLOGGING;
        CODEC = RecordCodecBuilder.<AncientKeepStructure>mapCodec((i) -> i.group(settingsCodec(i), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((j) -> j.startPool), Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((j) -> j.startJigsawName), Codec.intRange(MIN_DEPTH, MAX_DEPTH).fieldOf("size").forGetter((j) -> j.maxDepth), HeightProvider.CODEC.fieldOf("start_height").forGetter((j) -> j.startHeight), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((j) -> j.useExpansionHack), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((j) -> j.projectStartToHeightmap), maxDistanceCodec().fieldOf("max_distance_from_center").forGetter((j) -> j.maxDistanceFromCenter), Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter((j) -> j.poolAliases), DimensionPadding.CODEC.optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING).forGetter((j) -> j.dimensionPadding), LiquidSettings.CODEC.optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter((j) -> j.liquidSettings)).apply(i, AncientKeepStructure::new)).validate(AncientKeepStructure::verifyRange);
    }

    private static Codec<JigsawStructure.MaxDistance> maxDistanceCodec() {
        Codec<Integer> horizontalCodec = Codec.intRange(1, MAX_TOTAL_STRUCTURE_RANGE);
        Codec<JigsawStructure.MaxDistance> fullCodec = RecordCodecBuilder.create((i) -> i.group(horizontalCodec.fieldOf("horizontal").forGetter(JigsawStructure.MaxDistance::horizontal), ExtraCodecs.intRange(1, DimensionType.Y_SIZE).optionalFieldOf("vertical", DimensionType.Y_SIZE).forGetter(JigsawStructure.MaxDistance::vertical)).apply(i, JigsawStructure.MaxDistance::new));
        return Codec.either(fullCodec, horizontalCodec).xmap((either) -> either.map(Function.identity(), JigsawStructure.MaxDistance::new), (distance) -> distance.horizontal() == distance.vertical() ? Either.right(distance.horizontal()) : Either.left(distance));
    }
}
