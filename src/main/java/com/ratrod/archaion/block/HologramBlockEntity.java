package com.ratrod.archaion.block;

import com.ratrod.archaion.registry.ACBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HologramBlockEntity extends BlockEntity {

    private static final String TAG_TEXT = "text";

    private String text = "";
    public int clientTicks = 0;

    public HologramBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ACBlockEntities.HOLOGRAM.get(), worldPosition, blockState);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getTextColor() {
        return 0xFFa3ffe5;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, HologramBlockEntity blockEntity) {
        blockEntity.clientTicks++;
//        if (blockEntity.clientTicks % 20 == 0) {
//            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("hologram_ambient"));
//            AAALevel.addParticle(level, info.position(Vec3.atCenterOf(pos)).scale(0.75F));
//        }

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.text = tag.getString(TAG_TEXT);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString(TAG_TEXT, this.text);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
