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
    private static BoundingBox keepBox;

    public static void setBox(@Nullable BoundingBox box) {
        keepBox = box;
    }

    public static void tick(Player player) {
        BoundingBox box = keepBox;
        if (box == null) {
            return;
        }
        if (!box.isInside(player.blockPosition())) {
            return;
        }
        spawnAmbientParticles(player.level(), box, player.position(), player.getRandom());
    }

    private static void spawnAmbientParticles(Level level, BoundingBox box, Vec3 playerPos, RandomSource random) {
        for (int i = 0; i < 30; i++) {
            Vec3 pos = randomPointNearPlayer(playerPos, box, random);
            if (!isInOpenSpace(level, pos)) {
                continue;
            }

            level.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 0, 0.2 + random.nextFloat() * 0.3, 0);

//            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("ancient_keep_ambient"))
//                    .position(pos.x, pos.y, pos.z)
//                    .scale(1F);
//            AAALevel.addParticle(level, true, info);
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
}
