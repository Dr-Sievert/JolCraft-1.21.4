package net.sievert.jolcraft.item.custom.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.attachment.AncientEffectHelper;

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
                if (Screen.hasShiftDown()) {
                    if (AncientEffectHelper.hasAncientMemoryClient()) {
                        if (DwarvenLanguageHelper.knowsDwarvishClient()) {
                            tooltip.addAll(getFullyReadableTooltip(stack, player, tooltip, flag));
                        } else {
                            tooltip.addAll(getLockedTooltip(stack, player, tooltip, flag));
                        }
                    } else if (DwarvenLanguageHelper.knowsDwarvishClient()) {
                        tooltip.addAll(getPartialUnderstandingTooltip(stack, player, tooltip, flag));
                    } else {
                        tooltip.addAll(AncientEffectHelper.getAncientText(player,
                                getUnreadableTooltipSGA(stack, player, tooltip, flag)));
                    }

                    // Always add "ancient shift help" at the end of Shift-hover
                    if (!AncientEffectHelper.hasAncientMemoryClient()) {
                        tooltip.add(Component.translatable("tooltip.jolcraft.need_ancient")
                                .withStyle(ChatFormatting.RED));
                    }
                    if(!DwarvenLanguageHelper.knowsDwarvishClient()) {
                        tooltip.add(Component.translatable("tooltip.jolcraft.need_lang")
                                .withStyle(ChatFormatting.RED));
                        tooltip.add(Component.translatable("tooltip.jolcraft.ancient_memory")
                                .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
                    }
                } else {
                    if(DwarvenLanguageHelper.knowsDwarvishClient()) {
                        tooltip.addAll(getNoShiftTooltip(stack, player, tooltip, flag));
                    }else{
                        tooltip.addAll(getLockedTooltip(stack, player, tooltip, flag));
                    }
                    Component shiftKey = Component.literal("Shift").withStyle(ChatFormatting.BLUE);
                    tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }


    /** Subclass provides the fully readable tooltip for this item. */
    protected abstract List<Component> getFullyReadableTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides lines shown when NOT holding Shift (before the "Hold Shift" line). */
    protected abstract List<Component> getNoShiftTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the "locked" tooltip for this item (has memory, but not language). */
    protected abstract List<Component> getLockedTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the partial tooltip for this item (knows Dwarvish only). */
    protected abstract List<Component> getPartialUnderstandingTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Subclass provides the SGA-wrapped unreadable tooltip (knows nothing). */
    protected abstract List<Component> getUnreadableTooltipSGA(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);
}
