package net.sievert.jolcraft.item.custom.gem;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.sievert.jolcraft.item.custom.ToolItem;

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
        tooltip.add(Component.translatable("tooltip.jolcraft.chisel")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}


