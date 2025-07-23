package net.sievert.jolcraft.item.custom.book;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.dwarf.DwarvenLoreHelper;

import java.util.List;

public class DwarvenTomeItem extends Item {
    public DwarvenTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (context.level() != null && context.level().isClientSide()) {
            boolean knowsLanguage = DwarvenLanguageHelper.knowsDwarvishClient();

            var dataComponentType = JolCraftDataComponents.LORE_LINE_ID.get();
            String loreKey = dataComponentType != null ? stack.get(dataComponentType) : null;

            if (Screen.hasShiftDown()) {
                if (knowsLanguage) {
                    tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                } else {
                    tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                }
            } else {
                // Basic summary only
                if (knowsLanguage) {
                    var entry = (loreKey != null && !loreKey.isEmpty()) ? DwarvenLoreHelper.get(loreKey, false) : null;
                    if (entry != null) {
                        tooltip.add(entry.text());
                    } else {
                        tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                }
                // Add the shift help at the end
                Component shiftKey = Component.literal("Shift").withStyle(ChatFormatting.BLUE);
                tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
