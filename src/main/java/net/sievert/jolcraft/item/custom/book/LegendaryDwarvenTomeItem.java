package net.sievert.jolcraft.item.custom.book;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class LegendaryDwarvenTomeItem extends DwarvenTomeItem {
    public LegendaryDwarvenTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        // Respect a custom name if present (DataComponents.ITEM_NAME)
        Component customName = stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, null);
        if (customName != null && !customName.getString().isEmpty()) {
            // Copy and color the custom name
            return customName.copy().withStyle(ChatFormatting.GOLD);
        }
        // Otherwise, use the default translated name, gold
        return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD);
    }
}
