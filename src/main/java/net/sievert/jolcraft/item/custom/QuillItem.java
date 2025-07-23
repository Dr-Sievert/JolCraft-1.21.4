package net.sievert.jolcraft.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.item.custom.tooltip.SimpleTooltipItem;

public class QuillItem extends SimpleTooltipItem {

    public QuillItem(Properties properties, String tooltipKey) {
        super(properties, tooltipKey);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target.getType() == EntityType.SQUID && !stack.is(JolCraftItems.QUILL_FULL.get())) {
            if (!player.level().isClientSide) {
                ItemStack fullQuill = new ItemStack(JolCraftItems.QUILL_FULL.get());

                if (stack.getCount() == 1) {
                    // Replace directly in hand if it's the last empty quill
                    player.setItemInHand(hand, fullQuill);
                } else {
                    // Normal case: shrink and add
                    stack.shrink(1);
                    boolean added = player.addItem(fullQuill);
                    if (!added) {
                        player.drop(fullQuill, false);
                    }
                }
                player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.5F);
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
}
