package net.sievert.jolcraft.item.custom.gem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.item.custom.tooltip.ToolItem;
import net.sievert.jolcraft.util.attachment.TomeUnlockHelper;

import java.util.List;

public class ChiselItem extends ToolItem {

    public ChiselItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(material.applySwordProperties(properties, attackDamage, attackSpeed));
    }

    public ChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.jolcraft.chisel")
                    .withStyle(ChatFormatting.GRAY));

            if (!TomeUnlockHelper.hasUnlockClient(TomeUnlockHelper.CUTTING_GEMS)) {
                tooltip.add(Component.translatable("tooltip.jolcraft.chisel.cut_locked")
                        .withStyle(ChatFormatting.RED));
            }

        } else {
            Component shiftKey = Component.literal("Shift").withStyle(ChatFormatting.BLUE);
            tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

}

