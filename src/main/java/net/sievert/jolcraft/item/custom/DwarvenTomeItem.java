package net.sievert.jolcraft.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.component.JolCraftDataComponents;

import java.util.List;

public class DwarvenTomeItem extends Item {
    public DwarvenTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Only run this on the client side
        if (context.level() != null && context.level().isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Check if the player knows the Dwarven language
                boolean knowsLanguage = MyClientLanguageData.knowsLanguage();

                if (knowsLanguage) {
                    // Fetch lore data and display it
                    var dataComponentType = JolCraftDataComponents.LORE_LINE_ID.get();
                    if (dataComponentType != null) {
                        String loreKey = stack.get(dataComponentType);
                        if (loreKey != null && !loreKey.isEmpty()) {
                            tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                        }
                    }
                } else {
                    // If the player doesn't know the Dwarven language
                    tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                }
            }
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }
}
