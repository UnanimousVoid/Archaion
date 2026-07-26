package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ManageAnimationStatePacket(int entityId, int animationId, Action action) implements CustomPacketPayload {
    public static final Type<ManageAnimationStatePacket> TYPE = new Type<>(Archaion.prefix("manage_animation_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManageAnimationStatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ManageAnimationStatePacket::entityId,
            ByteBufCodecs.INT,
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
            if (entity instanceof ACEntity<?> acEntity && acEntity.getAnimationManager() != null) {
                ACAnimation animation = acEntity.getAnimationManager().getAnimation(packet.animationId());
                if (animation != null) {
                    switch (packet.action) {
                        case STOP -> animation.stop();
                        case START -> animation.start();
                        case FORCE_START -> animation.forceStart();
                    }
                } else {
                    Archaion.LOGGER.debug("Animation index {} is missing.", packet.animationId());
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
