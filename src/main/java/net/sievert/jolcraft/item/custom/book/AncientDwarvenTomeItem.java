package net.sievert.jolcraft.item.custom.book;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.util.dwarf.DwarvenLoreHelper;
import net.minecraft.ChatFormatting;
import net.sievert.jolcraft.item.custom.AncientItemBase;

import java.util.List;

public class AncientDwarvenTomeItem extends AncientItemBase {
    public AncientDwarvenTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Component getFullyReadableTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag) {
        // Look up the lore text, if it exists
        var dataComponentType = JolCraftDataComponents.LORE_LINE_ID.get();
        String loreKey = dataComponentType != null ? stack.get(dataComponentType) : null;
        var entry = (loreKey != null && !loreKey.isEmpty()) ? DwarvenLoreHelper.get(loreKey, true) : null;
        return (entry != null)
                ? entry.text()
                : Component.translatable("tooltip.jolcraft.ancient_dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY);
    }

    @Override
    protected Component getLockedTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag) {
        return Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY);
    }

    @Override
    protected Component getPartialUnderstandingTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag) {
        return Component.translatable("tooltip.jolcraft.ancient_dwarven_tome.partial_understanding")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }

    @Override
    protected Component getUnreadableTooltipSGA(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag) {
        // Show SGA version of what would otherwise be visible if unlocked (base class handles SGA-ifying)
        var dataComponentType = JolCraftDataComponents.LORE_LINE_ID.get();
        String loreKey = dataComponentType != null ? stack.get(dataComponentType) : null;
        var entry = (loreKey != null && !loreKey.isEmpty()) ? DwarvenLoreHelper.get(loreKey, true) : null;
        return (entry != null)
                ? entry.text()
                : Component.translatable("tooltip.jolcraft.ancient_dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY);
    }
}
