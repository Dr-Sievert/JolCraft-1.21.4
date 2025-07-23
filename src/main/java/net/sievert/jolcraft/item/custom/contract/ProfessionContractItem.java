package net.sievert.jolcraft.item.custom.contract;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;

import java.util.List;

public class ProfessionContractItem extends Item {
    public ProfessionContractItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (DwarvenLanguageHelper.knowsDwarvishClient()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.jolcraft.profession_contract")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }else{
                Component shiftKey = Component.literal("Shift").withStyle(ChatFormatting.BLUE);
                tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.paper.locked")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
