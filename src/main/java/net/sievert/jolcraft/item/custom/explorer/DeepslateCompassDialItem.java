package net.sievert.jolcraft.item.custom.explorer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.data.JolCraftDataComponents;

import java.util.List;

public class DeepslateCompassDialItem extends Item {
    public DeepslateCompassDialItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String structureId = stack.get(JolCraftDataComponents.STRUCTURE_GROUP);
        if (structureId != null && !structureId.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.jolcraft.deepslate_compass_dial." + structureId).withStyle(ChatFormatting.BLUE));
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.deepslate_compass_dial.unknown").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
