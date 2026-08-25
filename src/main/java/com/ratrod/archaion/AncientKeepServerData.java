package com.ratrod.archaion;

import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.s2c.AncientKeepAmbientPacket;
import com.ratrod.archaion.registry.ACStructures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AncientKeepServerData {

    private static final Set<UUID> insideAncientKeep = new HashSet<>();

    public static void tickPlayer(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (isInsideAncientKeep(player)) {
            if (insideAncientKeep.add(uuid)) {
                sendAncientKeepBox(player);
            }
        } else {
            insideAncientKeep.remove(uuid);
        }
    }

    public static void onPlayerLoggedOut(Player player) {
        insideAncientKeep.remove(player.getUUID());
    }

    private static boolean isInsideAncientKeep(ServerPlayer player) {
        StructureStart start = ((ServerLevel) player.level()).structureManager().getStructureWithPieceAt(player.blockPosition(), ACStructures.ON_ANCIENT_KEEP_MAPS);
        return start.isValid();
    }

    private static void sendAncientKeepBox(ServerPlayer player) {
        StructureStart start = ((ServerLevel) player.level()).structureManager().getStructureWithPieceAt(player.blockPosition(), ACStructures.ON_ANCIENT_KEEP_MAPS);
        if (start.isValid()) {
            BoundingBox b = start.getBoundingBox();
            ACNetwork.sendToPlayer(player, new AncientKeepAmbientPacket(new AABB(b.minX(), b.minY(), b.minZ(), b.maxX(), b.maxY(), b.maxZ())));
        }
    }
}
