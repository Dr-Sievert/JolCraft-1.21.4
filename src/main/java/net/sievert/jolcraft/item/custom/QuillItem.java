package net.sievert.jolcraft.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.item.JolCraftItems;

public class QuillItem extends Item {

    public QuillItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target.getType() == EntityType.SQUID && !stack.is(JolCraftItems.QUILL_FULL)) {
            if (!player.level().isClientSide) {
                stack.shrink(1);
                player.addItem(new ItemStack(JolCraftItems.QUILL_FULL.get()));
                player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.5F);
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
}