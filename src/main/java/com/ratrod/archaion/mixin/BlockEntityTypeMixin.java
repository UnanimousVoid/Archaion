package com.ratrod.archaion.mixin;

import com.ratrod.archaion.registry.ACBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("RETURN"), cancellable = true)
    private void ac$addDeepslateVaultToVaultType(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && ((Object) this) == BlockEntityType.VAULT) {
            cir.setReturnValue(state.is(ACBlocks.DEEPSLATE_VAULT.get()));
        }
    }
}
