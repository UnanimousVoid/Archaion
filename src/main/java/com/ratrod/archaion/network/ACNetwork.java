package com.ratrod.archaion.network;

import com.ratrod.archaion.network.s2c.AncientKeepAmbientPacket;
import com.ratrod.archaion.network.s2c.BossMusicPacket;
import com.ratrod.archaion.network.s2c.CameraShakePacket;
import com.ratrod.archaion.network.s2c.ManageAnimationStatePacket;
import com.ratrod.archaion.network.s2c.RemoveBossBarDataPacket;
import com.ratrod.archaion.network.s2c.SyncBossBarDataPacket;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ACNetwork {
    private static final String PROTOCOL_VERSION = "1.0";

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(AncientKeepAmbientPacket.TYPE, AncientKeepAmbientPacket.CODEC, AncientKeepAmbientPacket::handle);
        registrar.playToClient(CameraShakePacket.TYPE, CameraShakePacket.CODEC, CameraShakePacket::handle);
        registrar.playToClient(ManageAnimationStatePacket.TYPE, ManageAnimationStatePacket.CODEC, ManageAnimationStatePacket::handle);
        registrar.playToClient(SyncBossBarDataPacket.TYPE, SyncBossBarDataPacket.CODEC, SyncBossBarDataPacket::handle);
        registrar.playToClient(RemoveBossBarDataPacket.TYPE, RemoveBossBarDataPacket.CODEC, RemoveBossBarDataPacket::handle);
        registrar.playToClient(BossMusicPacket.TYPE, BossMusicPacket.CODEC, BossMusicPacket::handle);
    }

    public static void sendToAll(CustomPacketPayload packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }

    public static void sendToTrackingPlayers(Entity entity, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        ClientPacketDistributor.sendToServer(packet);
    }
}
