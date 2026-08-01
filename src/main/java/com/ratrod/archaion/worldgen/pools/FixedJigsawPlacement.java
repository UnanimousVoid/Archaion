package com.ratrod.archaion.worldgen.pools;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SequencedPriorityIterator;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class FixedJigsawPlacement {
    private static final Logger LOGGER = LogUtils.getLogger();

    private FixedJigsawPlacement() {

    }

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext context, Holder<StructureTemplatePool> startPool, Optional<Identifier> startJigsaw, int maxDepth, BlockPos position, boolean doExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, JigsawStructure.MaxDistance maxDistanceFromCenter, PoolAliasLookup poolAliasLookup, DimensionPadding dimensionPadding, LiquidSettings liquidSettings) {
        RegistryAccess registryAccess = context.registryAccess();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
        LevelHeightAccessor heightAccessor = context.heightAccessor();
        WorldgenRandom random = context.random();
        Registry<StructureTemplatePool> pools = registryAccess.lookupOrThrow(Registries.TEMPLATE_POOL);
        Rotation centerRotation = Rotation.NONE;
        StructureTemplatePool centerPool = startPool.unwrapKey().flatMap(key -> pools.getOptional(poolAliasLookup.lookup(key))).orElse(startPool.value());
        StructurePoolElement centerElement = centerPool.getRandomTemplate(random);
        if (centerElement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        }

        BlockPos anchoredPosition;
        if (startJigsaw.isPresent()) {
            Optional<BlockPos> anchor = getRandomNamedJigsaw(centerElement, startJigsaw.get(), position, centerRotation, structureTemplateManager, random);
            if (anchor.isEmpty()) {
                LOGGER.error("No starting jigsaw {} found in start pool {}", startJigsaw.get(), startPool.unwrapKey().map(key -> key.identifier().toString()).orElse("<unregistered>"));
                return Optional.empty();
            }
            anchoredPosition = anchor.get();
        } else {
            anchoredPosition = position;
        }

        Vec3i localAnchorPosition = anchoredPosition.subtract(position);
        BlockPos adjustedPosition = position.subtract(localAnchorPosition);
        PoolElementStructurePiece centerPiece = new PoolElementStructurePiece(structureTemplateManager, centerElement, adjustedPosition, centerElement.getGroundLevelDelta(), centerRotation, centerElement.getBoundingBox(structureTemplateManager, adjustedPosition, centerRotation), liquidSettings);
        BoundingBox box = centerPiece.getBoundingBox();
        int centerX = (box.maxX() + box.minX()) / 2;
        int centerZ = (box.maxZ() + box.minZ()) / 2;
        int bottomY = projectStartToHeightmap.isEmpty() ? adjustedPosition.getY() : position.getY() + chunkGenerator.getFirstFreeHeight(centerX, centerZ, projectStartToHeightmap.get(), heightAccessor, context.randomState());
        int oldAbsoluteGroundY = box.minY() + centerPiece.getGroundLevelDelta();
        centerPiece.move(0, bottomY - oldAbsoluteGroundY, 0);
        if (isStartTooCloseToWorldHeightLimits(heightAccessor, dimensionPadding, centerPiece.getBoundingBox())) {
            LOGGER.debug("Center piece {} with bounding box {} does not fit dimension padding {}", centerElement, centerPiece.getBoundingBox(), dimensionPadding);
            return Optional.empty();
        }

        int centerY = bottomY + localAnchorPosition.getY();
        return Optional.of(new Structure.GenerationStub(new BlockPos(centerX, centerY, centerZ), builder -> {
            List<PoolElementStructurePiece> pieces = Lists.newArrayList();
            pieces.add(centerPiece);
            if (maxDepth > 0) {
                AABB aabb = new AABB(centerX - maxDistanceFromCenter.horizontal(), Math.max(centerY - maxDistanceFromCenter.vertical(), heightAccessor.getMinY() + dimensionPadding.bottom()), centerZ - maxDistanceFromCenter.horizontal(), centerX + maxDistanceFromCenter.horizontal() + 1, Math.min(centerY + maxDistanceFromCenter.vertical() + 1, heightAccessor.getMaxY() + 1 - dimensionPadding.top()), centerZ + maxDistanceFromCenter.horizontal() + 1);
                VoxelShape shape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(box)), BooleanOp.ONLY_FIRST);
                addPieces(context.randomState(), maxDepth, doExpansionHack, chunkGenerator, structureTemplateManager, heightAccessor, random, pools, centerPiece, pieces, shape, poolAliasLookup, liquidSettings);
                Objects.requireNonNull(builder);
                pieces.forEach(builder::addPiece);
            }
        }));
    }

    private static boolean isStartTooCloseToWorldHeightLimits(LevelHeightAccessor heightAccessor, DimensionPadding dimensionPadding, BoundingBox centerPieceBb) {
        if (dimensionPadding == DimensionPadding.ZERO) {
            return false;
        }
        return centerPieceBb.minY() < heightAccessor.getMinY() + dimensionPadding.bottom() || centerPieceBb.maxY() > heightAccessor.getMaxY() - dimensionPadding.top();
    }

    private static Optional<BlockPos> getRandomNamedJigsaw(StructurePoolElement element, Identifier targetJigsawId, BlockPos position, Rotation rotation, StructureTemplateManager structureTemplateManager, WorldgenRandom random) {
        for (StructureTemplate.JigsawBlockInfo jigsaw : element.getShuffledJigsawBlocks(structureTemplateManager, position, rotation, random)) {
            if (targetJigsawId.equals(jigsaw.name())) {
                return Optional.of(jigsaw.info().pos());
            }
        }
        return Optional.empty();
    }

    private static void addPieces(RandomState randomState, int maxDepth, boolean doExpansionHack, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, LevelHeightAccessor heightAccessor, RandomSource random, Registry<StructureTemplatePool> pools, PoolElementStructurePiece centerPiece, List<PoolElementStructurePiece> pieces, VoxelShape shape, PoolAliasLookup poolAliasLookup, LiquidSettings liquidSettings) {
        Placer placer = new Placer(pools, maxDepth, chunkGenerator, structureTemplateManager, pieces, random);
        placer.tryPlacingChildren(centerPiece, new MutableObject<>(shape), 0, doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);
        while (placer.placing.hasNext()) {
            PieceState state = placer.placing.next();
            placer.tryPlacingChildren(state.piece(), state.free(), state.depth(), doExpansionHack, heightAccessor, randomState, poolAliasLookup, liquidSettings);
        }
    }

    private record PieceState(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth) {
    }

    private static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureTemplateManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final RandomSource random;
        private final SequencedPriorityIterator<PieceState> placing = new SequencedPriorityIterator<>();

        private Placer(Registry<StructureTemplatePool> pools, int maxDepth, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolElementStructurePiece> pieces, RandomSource random) {
            this.pools = pools;
            this.maxDepth = maxDepth;
            this.chunkGenerator = chunkGenerator;
            this.structureTemplateManager = structureTemplateManager;
            this.pieces = pieces;
            this.random = random;
        }

        private void tryPlacingChildren(PoolElementStructurePiece sourcePiece, MutableObject<VoxelShape> contextFree, int depth, boolean doExpansionHack, LevelHeightAccessor heightAccessor, RandomState randomState, PoolAliasLookup poolAliasLookup, LiquidSettings liquidSettings) {
            StructurePoolElement sourceElement = sourcePiece.getElement();
            BlockPos sourceBoxPosition = sourcePiece.getPosition();
            Rotation sourceRotation = sourcePiece.getRotation();
            StructureTemplatePool.Projection sourceProjection = sourceElement.getProjection();
            boolean sourceRigid = sourceProjection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> sourceFree = new MutableObject<>();
            BoundingBox sourceBB = sourcePiece.getBoundingBox();
            int sourceBoxY = sourceBB.minY();

            for (StructureTemplate.JigsawBlockInfo sourceJigsaw : sourceElement.getShuffledJigsawBlocks(this.structureTemplateManager, sourceBoxPosition, sourceRotation, this.random)) {
                StructureTemplate.StructureBlockInfo sourceJigsawInfo = sourceJigsaw.info();
                Direction sourceDirection = JigsawBlock.getFrontFacing(sourceJigsawInfo.state());
                BlockPos sourceJigsawPos = sourceJigsawInfo.pos();
                BlockPos targetJigsawPos = sourceJigsawPos.relative(sourceDirection);
                int sourceJigsawLocalY = sourceJigsawPos.getY() - sourceBoxY;
                int sourceJigsawBaseHeight = Integer.MIN_VALUE;
                ResourceKey<StructureTemplatePool> poolName = poolAliasLookup.lookup(sourceJigsaw.pool());
                Optional<? extends Holder<StructureTemplatePool>> maybeTargetPool = this.pools.get(poolName);
                if (maybeTargetPool.isEmpty()) {
                    LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
                    continue;
                }
                Holder<StructureTemplatePool> targetPool = maybeTargetPool.get();
                if (targetPool.value().size() == 0 && !targetPool.is(Pools.EMPTY)) {
                    LOGGER.warn("Empty or non-existent pool: {}", poolName.identifier());
                    continue;
                }
                Holder<StructureTemplatePool> fallback = targetPool.value().getFallback();
                if (fallback.value().size() == 0 && !fallback.is(Pools.EMPTY)) {
                    LOGGER.warn("Empty or non-existent fallback pool: {}", fallback.unwrapKey().map(key -> key.identifier().toString()).orElse("<unregistered>"));
                    continue;
                }

                boolean attachInsideSource = sourceBB.isInside(targetJigsawPos);
                MutableObject<VoxelShape> childrenFree;
                if (attachInsideSource) {
                    childrenFree = sourceFree;
                    if (sourceFree.get() == null) {
                        sourceFree.setValue(Shapes.create(AABB.of(sourceBB)));
                    }
                } else {
                    childrenFree = contextFree;
                }
                List<StructurePoolElement> targetPieces = Lists.newArrayList();
                if (depth != this.maxDepth) {
                    targetPieces.addAll(targetPool.value().getShuffledTemplates(this.random));
                }
                targetPieces.addAll(fallback.value().getShuffledTemplates(this.random));
                int placementPriority = sourceJigsaw.placementPriority();

                boolean placed = false;
                for (StructurePoolElement targetElement : targetPieces) {
                    if (targetElement == EmptyPoolElement.INSTANCE) {
                        break;
                    }
                    Rotation targetRotation = Rotation.NONE;
                    List<StructureTemplate.JigsawBlockInfo> targetJigsaws = targetElement.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, targetRotation, this.random);
                    BoundingBox hackBox = targetElement.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, targetRotation);
                    int expandTo = doExpansionHack && hackBox.getYSpan() <= 16 ? targetJigsaws.stream().mapToInt(targetJigsaw -> {
                        if (!hackBox.isInside(targetJigsaw.info().pos().relative(JigsawBlock.getFrontFacing(targetJigsaw.info().state())))) {
                            return 0;
                        }
                        Optional<? extends Holder<StructureTemplatePool>> childPool = this.pools.get(poolAliasLookup.lookup(targetJigsaw.pool()));
                        Optional<? extends Holder<StructureTemplatePool>> childFallbackPool = childPool.map(pool -> pool.value().getFallback());
                        int childPoolSize = childPool.map(pool -> pool.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                        int childFallbackSize = childFallbackPool.map(pool -> pool.value().getMaxSize(this.structureTemplateManager)).orElse(0);
                        return Math.max(childPoolSize, childFallbackSize);
                    }).max().orElse(0) : 0;

                    for (StructureTemplate.JigsawBlockInfo targetJigsaw : targetJigsaws) {
                        if (!JigsawBlock.canAttach(sourceJigsaw, targetJigsaw)) {
                            continue;
                        }
                        BlockPos targetJigsawLocalPos = targetJigsaw.info().pos();
                        BlockPos rawTargetBoxPos = targetJigsawPos.subtract(targetJigsawLocalPos);
                        BoundingBox rawTargetBB = targetElement.getBoundingBox(this.structureTemplateManager, rawTargetBoxPos, targetRotation);
                        int rawTargetY = rawTargetBB.minY();
                        StructureTemplatePool.Projection targetProjection = targetElement.getProjection();
                        boolean targetRigid = targetProjection == StructureTemplatePool.Projection.RIGID;
                        int targetJigsawLocalY = targetJigsawLocalPos.getY();
                        int deltaY = sourceJigsawLocalY - targetJigsawLocalY + JigsawBlock.getFrontFacing(sourceJigsawInfo.state()).getStepY();
                        int targetBoxY;
                        if (sourceRigid && targetRigid) {
                            targetBoxY = sourceBoxY + deltaY;
                        } else {
                            if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
                                sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
                            }
                            targetBoxY = sourceJigsawBaseHeight - targetJigsawLocalY;
                        }
                        int yOffset = targetBoxY - rawTargetY;
                        BoundingBox targetBB = rawTargetBB.moved(0, yOffset, 0);
                        BlockPos targetBoxPosition = rawTargetBoxPos.offset(0, yOffset, 0);
                        if (expandTo > 0) {
                            targetBB.encapsulate(new BlockPos(targetBB.minX(), targetBB.minY() + Math.max(expandTo + 1, targetBB.maxY() - targetBB.minY()), targetBB.minZ()));
                        }
                        if (Shapes.joinIsNotEmpty(childrenFree.get(), Shapes.create(AABB.of(targetBB).deflate(0.25F)), BooleanOp.ONLY_SECOND)) {
                            continue;
                        }
                        childrenFree.setValue(Shapes.joinUnoptimized(childrenFree.get(), Shapes.create(AABB.of(targetBB)), BooleanOp.ONLY_FIRST));
                        int targetGroundLevelDelta = targetRigid ? sourcePiece.getGroundLevelDelta() - deltaY : targetElement.getGroundLevelDelta();
                        PoolElementStructurePiece targetPiece = new PoolElementStructurePiece(this.structureTemplateManager, targetElement, targetBoxPosition, targetGroundLevelDelta, targetRotation, targetBB, liquidSettings);
                        int junctionY;
                        if (sourceRigid) {
                            junctionY = sourceBoxY + sourceJigsawLocalY;
                        } else if (targetRigid) {
                            junctionY = targetBoxY + targetJigsawLocalY;
                        } else {
                            if (sourceJigsawBaseHeight == Integer.MIN_VALUE) {
                                sourceJigsawBaseHeight = this.chunkGenerator.getFirstFreeHeight(sourceJigsawPos.getX(), sourceJigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
                            }
                            junctionY = sourceJigsawBaseHeight + deltaY / 2;
                        }
                        sourcePiece.addJunction(new JigsawJunction(targetJigsawPos.getX(), junctionY - sourceJigsawLocalY + sourcePiece.getGroundLevelDelta(), targetJigsawPos.getZ(), deltaY, targetProjection));
                        targetPiece.addJunction(new JigsawJunction(sourceJigsawPos.getX(), junctionY - targetJigsawLocalY + targetGroundLevelDelta, sourceJigsawPos.getZ(), -deltaY, sourceProjection));
                        this.pieces.add(targetPiece);
                        if (depth + 1 <= this.maxDepth) {
                            this.placing.add(new PieceState(targetPiece, childrenFree, depth + 1), placementPriority);
                        }
                        placed = true;
                        break;
                    }
                    if (placed) {
                        break;
                    }
                }
            }
        }
    }
}
