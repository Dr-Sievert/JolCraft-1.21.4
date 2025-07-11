package net.sievert.jolcraft.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.random.AncientEffectHelper;

import java.util.List;

public abstract class AncientItemBase extends Item {
    public AncientItemBase(Properties properties) {
        super(properties);
    }
    @Override
    public final void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (context.level() != null && context.level().isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (AncientEffectHelper.hasAncientMemoryClient()) {
                    if (DwarvenLanguageHelper.knowsDwarvishClient()) {
                        tooltip.add(getFullyReadableTooltip(stack, player, tooltip, flag));
                    } else {
                        tooltip.add(getLockedTooltip(stack, player, tooltip, flag));
                    }
                } else if (DwarvenLanguageHelper.knowsDwarvishClient()) {
                    tooltip.add(getPartialUnderstandingTooltip(stack, player, tooltip, flag));
                } else {
                    tooltip.add(AncientEffectHelper.getAncientText(player,
                            getUnreadableTooltipSGA(stack, player, tooltip, flag)));
                }
            }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }


    /** Subclass provides the fully readable tooltip for this item. */
    protected abstract Component getFullyReadableTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the "locked" tooltip for this item (has memory, but not language). */
    protected abstract Component getLockedTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the partial tooltip for this item (knows Dwarvish only). */
    protected abstract Component getPartialUnderstandingTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the SGA-wrapped unreadable tooltip (knows nothing). */
    protected abstract Component getUnreadableTooltipSGA(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);
}
