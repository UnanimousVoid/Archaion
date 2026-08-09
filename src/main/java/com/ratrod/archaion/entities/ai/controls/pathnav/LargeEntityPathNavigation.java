package com.ratrod.archaion.entities.ai.controls.pathnav;

import com.ratrod.archaion.Archaion;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Ground path navigator tuned for entities with large hitboxes.
 * Uses 3D voxel sweeping to truncate path nodes whenever a direct
 * line-of-walk between the entity and a future node is clear,
 * preventing large entities from walking unnecessary zigzag paths.
 */
public class LargeEntityPathNavigation extends GroundPathNavigation {
    private static final float EPSILON = 1.0E-6F;

    public LargeEntityPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new BandaidPathFinder(nodeEvaluator, maxNodes);
    }

    @Override
    protected void followThePath() {
        Path curPath = Objects.requireNonNull(path);
        Vec3 tempPos = getTempMobPos();
        Mob e = mob;

        final Vec3 center = tempPos.add(-e.getBbWidth() * 0.5F, 0.0F, -e.getBbWidth() * 0.5F);
        final Vec3 maxArea = center.add(e.getBbWidth(), e.getBbHeight(), e.getBbWidth());
        Vec3 entityPos = new Vec3(e.getX(), e.getY(), e.getZ());

        int pathLength = curPath.getNodeCount();
        for (int i = path.getNextNodeIndex(); i < curPath.getNodeCount(); i++) {
            if (path.getNode(i).y != Math.floor(tempPos.y) && hasValidPathType(path.getNode(i).type)) {
                pathLength = i;
                break;
            }
        }

        if (tryTruncateNodes(curPath, pathLength, entityPos, center, maxArea)) {
            float maxDistanceToWaypoint = e.getBbWidth() > 0.75F ? e.getBbWidth() * 0.5F : 0.75F - e.getBbWidth() * 0.5F;
            if (followingPath(curPath, maxDistanceToWaypoint)
                    || (elevationChangedFor(curPath) && followingPath(curPath, e.getBbWidth() * 0.5F))
                    && canCutCorner(path.getNextNode().type)) {
                path.setNextNodeIndex(path.getNextNodeIndex() + 1);
            }
        }

        doStuckDetection(tempPos);
    }

    private static int leti(float c, int step) {
        return Mth.floor(c - step * EPSILON);
    }

    private static int teti(float c, int step) {
        return Mth.floor(c + step * EPSILON);
    }

    private static float axisOf(Vec3 pos, int axis) {
        return switch (axis) {
            case 1 -> (float) pos.x;
            case 2 -> (float) pos.y;
            case 3 -> (float) pos.z;
            default -> 0.0F;
        };
    }

    private boolean followingPath(Path curPath, float threshold) {
        Vec3 pos = curPath.getNextEntityPos(mob);
        float dx = Mth.abs((float) (mob.getX() - pos.x));
        float dy = Mth.abs((float) (mob.getY() - pos.y));
        float dz = Mth.abs((float) (mob.getZ() - pos.z));
        return dx < threshold && dy < 1.0D && dz < threshold;
    }

    @Override
    protected boolean canMoveDirectly(Vec3 a, Vec3 b) {
        return isClearForMovementBetween(mob, a, b, false);
    }

    private boolean elevationChangedFor(Path ePath) {
        final int curNode = ePath.getNextNodeIndex();
        final int curY = ePath.getNode(curNode).y;
        final int end = (int) Math.min(ePath.getNodeCount(),
                curNode + Math.ceil(mob.getBbWidth() / 2.0D) + 1.0F);
        for (int i = curNode + 1; i < end; i++) {
            if (ePath.getNode(i).y != curY) return true;
        }
        return false;
    }

    private boolean tryTruncateNodes(Path pathToTrim, int pathLength, Vec3 entityPos, Vec3 center, Vec3 max) {
        for (int i = pathLength; --i > pathToTrim.getNextNodeIndex(); ) {
            Node node = pathToTrim.getNode(i);
            double half = Mth.floor(mob.getBbWidth() + 1.0F) * 0.5D;
            Vec3 nodePos = new Vec3(node.x + half, node.y, node.z + half);
            Vec3 dist = nodePos.subtract(entityPos);
            if (sweepThrough(dist, center, max)) {
                pathToTrim.setNextNodeIndex(i);
                return false;
            }
        }
        return true;
    }

    private boolean sweepThrough(Vec3 pathVec, Vec3 center, Vec3 max) {
        float l = 0.0F;
        float ml = (float) pathVec.length();
        if (ml < EPSILON) return true;

        final float[] trailEdge = new float[3];
        final int[] leadI = new int[3], trailI = new int[3], step = new int[3];
        final float[] trailDelta = new float[3], trailNext = new float[3], normal = new float[3];

        for (int i = 0; i < 3; i++) {
            float axis = axisOf(pathVec, i);
            boolean dir = axis >= 0.0F;
            step[i] = dir ? 1 : -1;
            float lead = axisOf(dir ? max : center, i);
            trailEdge[i] = axisOf(dir ? center : max, i);
            leadI[i] = leti(lead, step[i]);
            trailI[i] = teti(trailEdge[i], step[i]);
            normal[i] = axis / ml;
            trailDelta[i] = Mth.abs(ml / axis);
            float dist = dir ? (leadI[i] + 1 - lead) : (lead - leadI[i]);
            trailNext[i] = trailDelta[i] < Float.POSITIVE_INFINITY ? trailDelta[i] * dist : Float.POSITIVE_INFINITY;
        }

        final BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        do {
            int axis = trailNext[0] < trailNext[1] ? (trailNext[0] < trailNext[2] ? 0 : 2) : (trailNext[1] < trailNext[2] ? 1 : 2);
            float nd = trailNext[axis] - l;
            l = trailNext[axis];
            leadI[axis] += step[axis];
            trailNext[axis] += trailDelta[axis];

            for (int i = 0; i < 3; i++) {
                trailEdge[i] += nd * normal[i];
                trailI[i] = teti(trailEdge[i], step[i]);
            }

            int sx = step[0], x0 = axis == 0 ? leadI[0] : trailI[0], x1 = leadI[0] + sx;
            int sy = step[1], y0 = axis == 1 ? leadI[1] : trailI[1], y1 = leadI[1] + sy;
            int sz = step[2], z0 = axis == 2 ? leadI[2] : trailI[2], z1 = leadI[2] + sz;

            for (int x = x0; x != x1; x += sx) {
                for (int z = z0; z != z1; z += sz) {
                    for (int y = y0; y != y1; y += sy) {
                        if (!this.level.getBlockState(mpos.set(x, y, z)).isPathfindable(PathComputationType.LAND))
                            return false;
                    }
                    PathType below = this.nodeEvaluator.getPathType(new PathfindingContext(this.level, this.mob), x, y0 - 1, z);
                    if (below == PathType.WATER || below == PathType.LAVA || below == PathType.OPEN)
                        return false;
                    PathType in = this.nodeEvaluator.getPathType(new PathfindingContext(this.level, this.mob), x, y0, z);
                    float malus = this.mob.getPathfindingMalus(in);
                    if (malus < 0.0F || malus >= 8.0F) return false;
                    if (in == PathType.FIRE || in == PathType.FIRE_IN_NEIGHBOR || in == PathType.DAMAGING || in == PathType.WATER)
                        return false;
                }
            }
        } while (l <= ml);
        return true;
    }

    @Override
    protected boolean hasValidPathType(PathType type) {
        return type != PathType.WATER && type != PathType.LAVA && type != PathType.OPEN;
    }

    @Override
    public boolean canCutCorner(PathType type) {
        return type != PathType.LAVA && super.canCutCorner(type);
    }

    public static class BandaidPathFinder extends PathFinder {
        public BandaidPathFinder(NodeEvaluator processor, int maxVisitedNodes) {
            super(processor, maxVisitedNodes);
        }

        @Nullable
        @Override
        public Path findPath(@NotNull PathNavigationRegion region, @NotNull Mob entity, @NotNull Set<BlockPos> targets, float range, int accuracy, float ySearchMultiplier) {
            Path p = super.findPath(region, entity, targets, range, accuracy, ySearchMultiplier);
            return p == null ? null : new BandaidPath(p);
        }
    }

    public static class BandaidPath extends Path {

        public BandaidPath(Path origin) {
            super(copyNodes(origin), origin.getTarget(), origin.canReach());
        }

        private static List<Node> copyNodes(Path origin) {
            List<Node> list = new ObjectArrayList<>();
            for (int i = 0; i < origin.getNodeCount(); i++) {
                list.add(origin.getNode(i));
            }
            return list;
        }

        @Override
        public @NotNull Vec3 getEntityPosAtNode(Entity entity, int index) {
            Node node = getNode(index);
            return new Vec3(
                    node.x + Math.floor(entity.getBbWidth() + 1.0F) * 0.5D,
                    node.y,
                    node.z + Math.floor(entity.getBbWidth() + 1.0F) * 0.5D
            );
        }

        @Override
        public String toString() {
            if (getNextNodeIndex() < getNodeCount()) {
                return Archaion.MODID + " Path: [length=" + getNodeCount()
                        + ", start=" + getNodePos(0)
                        + ", next=" + getNextNodePos()
                        + ", dest=" + getEndNode().asBlockPos() + "]";
            }
            return "NULL OR INVALID PATH";
        }
    }
}
