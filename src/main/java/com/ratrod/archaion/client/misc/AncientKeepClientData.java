package com.ratrod.archaion.client.misc;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AncientKeepClientData {

    @Nullable
    public static AABB ANCIENT_KEEP_BOX;

    private static float ANCIENT_KEEP_FOG_MIX = 0.0F;
    private static final float ANCIENT_KEEP_FOG_RAMP = 1.0F / 20.0F;

    private static void spawnAmbientParticles(Level level, AABB box, Vec3 playerPos, RandomSource random) {
        for (int i = 0; i < 12; i++) {
            Vec3 pos = randomPointNearPlayer(playerPos, box, random);
            level.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 0, 0.2 + random.nextFloat() * 0.3, 0);
        }
    }

    private static Vec3 randomPointNearPlayer(Vec3 playerPos, AABB box, RandomSource random) {
        double radius = 32.0D;
        double x = playerPos.x + (random.nextDouble() * 2.0D - 1.0D) * radius;
        double y = playerPos.y + (random.nextDouble() * 2.0D - 1.0D) * radius;
        double z = playerPos.z + (random.nextDouble() * 2.0D - 1.0D) * radius;
        return new Vec3(x, y, z);
    }

    public static void tick(Player player) {
        boolean inside = ANCIENT_KEEP_BOX != null && ANCIENT_KEEP_BOX.contains(player.position());
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