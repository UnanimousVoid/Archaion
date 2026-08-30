package com.ratrod.archaion.block;

import com.mojang.serialization.MapCodec;
import com.ratrod.archaion.registry.ACBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class TeleporterBlock extends BaseEntityBlock {

    public static final MapCodec<TeleporterBlock> CODEC = simpleCodec(TeleporterBlock::new);

    public static final EnumProperty<TeleporterColor> COLOR = EnumProperty.create("color", TeleporterColor.class);

    public TeleporterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, TeleporterColor.WHITE));
    }

    @Override
    public MapCodec<TeleporterBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new TeleporterBlockEntity(worldPosition, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, ACBlockEntities.TELEPORTER.get(), TeleporterBlockEntity::clientTick)
                : createTickerHelper(type, ACBlockEntities.TELEPORTER.get(), TeleporterBlockEntity::serverTick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player.isCreative()) {
            TeleporterColor next = state.getValue(COLOR).next();
            level.setBlock(pos, state.setValue(COLOR, next), Block.UPDATE_CLIENTS);
        }
        return InteractionResult.SUCCESS;
    }
}