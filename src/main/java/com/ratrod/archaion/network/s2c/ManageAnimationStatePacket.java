package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ManageAnimationStatePacket(int entityId, String animationId, Action action) implements CustomPacketPayload {
    public static final Type<ManageAnimationStatePacket> TYPE = new Type<>(Archaion.prefix("manage_animation_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManageAnimationStatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ManageAnimationStatePacket::entityId,
            ByteBufCodecs.STRING_UTF8,
            ManageAnimationStatePacket::animationId,
            ByteBufCodecs.VAR_INT.map(id -> Action.values()[id], Action::ordinal),
            ManageAnimationStatePacket::action,
            ManageAnimationStatePacket::new
    );

    public static void handle(ManageAnimationStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }

            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
            if (entity instanceof ACEntity acEntity && acEntity.getAnimationManager() != null) {
                if (packet.action == Action.START) {
                    acEntity.getAnimationManager().getAnimationState(packet.animationId).startIfStopped(entity.tickCount);
                } else if (packet.action == Action.FORCE_START) {
                    acEntity.getAnimationManager().getAnimationState(packet.animationId).start(entity.tickCount);
                } else if (packet.action == Action.STOP) {
                    acEntity.getAnimationManager().getAnimationState(packet.animationId).stop();
                }
            }
        });
    }

    public enum Action {
        START,
        FORCE_START,
        STOP
    }

    @Override
    public Type<ManageAnimationStatePacket> type() {
        return TYPE;
    }
}
