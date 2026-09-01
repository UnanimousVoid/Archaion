package com.ratrod.archaion.item;

import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class EchoChargeItem extends Item {

    public EchoChargeItem(Properties properties) {
        super(properties);
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ACSounds.LOD_SHOOT.get(), SoundSource.NEUTRAL, 0.5F, 1.6F + level.getRandom().nextFloat() * 0.2F);
        if (level instanceof ServerLevel serverLevel) {
            EchoStarProjectile echoStar = Projectile.spawnProjectileFromRotation(EchoStarProjectile::new, serverLevel, itemStack, player, 0.0F, 1.5F, 1.0F);
            echoStar.setBaseDamage(12.0F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("item.archaion.echo_charge.desc",
                Component.translatable("item.archaion.echo_charge.keyword.slumbering_titan").withStyle(ChatFormatting.AQUA),
                Component.translatable("item.archaion.echo_charge.keyword.special_weapon").withStyle(ChatFormatting.YELLOW))
                .withStyle(ChatFormatting.DARK_GRAY));
        builder.accept(CommonComponents.EMPTY);
    }
}
