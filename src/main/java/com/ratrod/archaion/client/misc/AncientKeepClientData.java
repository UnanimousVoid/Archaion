package com.ratrod.archaion.client.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class AncientKeepClientData {

    @Nullable
    public static BoundingBox ANCIENT_KEEP_BOX;

    private static float ANCIENT_KEEP_FOG_MIX = 0.0F;
    private static final float ANCIENT_KEEP_FOG_RAMP = 1.0F / 20.0F;

    private static void spawnAmbientParticles(Level level, BoundingBox box, Vec3 playerPos, RandomSource random) {
        for (int i = 0; i < 30; i++) {
            Vec3 pos = randomPointNearPlayer(playerPos, box, random);
            if (!isInOpenSpace(level, pos)) {
                continue;
            }

            level.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 0, 0.2 + random.nextFloat() * 0.3, 0);
        }
    }

    private static Vec3 randomPointNearPlayer(Vec3 playerPos, BoundingBox box, RandomSource random) {
        double radius = 32.0D;
        double x = Mth.clamp(playerPos.x + (random.nextDouble() * 2.0D - 1.0D) * radius, box.minX(), box.maxX());
        double y = Mth.clamp(playerPos.y + (random.nextDouble() * 2.0D - 1.0D) * radius, box.minY(), box.maxY());
        double z = Mth.clamp(playerPos.z + (random.nextDouble() * 2.0D - 1.0D) * radius, box.minZ(), box.maxZ());
        return new Vec3(x, y, z);
    }

    private static boolean isInOpenSpace(Level level, Vec3 pos) {
        BlockPos blockPos = BlockPos.containing(pos);
        return level.isEmptyBlock(blockPos) && level.isEmptyBlock(blockPos.above());
    }

    public static void tick(Player player) {
        boolean inside = ANCIENT_KEEP_BOX != null && ANCIENT_KEEP_BOX.isInside(player.blockPosition());
        if (inside && ANCIENT_KEEP_FOG_MIX < 1.0F) {
            ANCIENT_KEEP_FOG_MIX = Math.min(1.0F, ANCIENT_KEEP_FOG_MIX + ANCIENT_KEEP_FOG_RAMP);
        } else if (!inside && ANCIENT_KEEP_FOG_MIX > 0.0F) {
            ANCIENT_KEEP_FOG_MIX = Math.max(0.0F, ANCIENT_KEEP_FOG_MIX - ANCIENT_KEEP_FOG_RAMP);
        }

        if (inside) {
            spawnAmbientParticles(player.level(), ANCIENT_KEEP_BOX, player.position(), player.getRandom());
        }
    }

    public static float keepFogFactor() {
        return (float) Mth.smoothstep(ANCIENT_KEEP_FOG_MIX);
    }
}