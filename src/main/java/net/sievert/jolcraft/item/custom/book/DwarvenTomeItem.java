package net.sievert.jolcraft.item.custom.book;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.lore.DwarvenLoreHelper;

import java.util.List;

public class DwarvenTomeItem extends Item {
    public DwarvenTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (context.level() != null && context.level().isClientSide()) {
            boolean knowsLanguage = DwarvenLanguageHelper.knowsDwarvishClient(); // ✅ uses helper

            var dataComponentType = JolCraftDataComponents.LORE_LINE_ID.get();
            String loreKey = dataComponentType != null ? stack.get(dataComponentType) : null;

            if (knowsLanguage) {
                var entry = (loreKey != null && !loreKey.isEmpty()) ? DwarvenLoreHelper.get(loreKey, false) : null;
                if (entry != null) {
                    tooltip.add(entry.text());
                } else {
                    tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(net.minecraft.ChatFormatting.GRAY));
                }
            } else {
                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }
}
