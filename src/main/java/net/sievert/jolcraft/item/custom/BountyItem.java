package net.sievert.jolcraft.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.util.BountyHelper;
import net.sievert.jolcraft.client.data.MyClientLanguageData;

import java.util.List;

public class BountyItem extends Item {
    public BountyItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean knowsLanguage = MyClientLanguageData.knowsLanguage();
        if (knowsLanguage) {
            int tier = BountyHelper.getBountyTier(stack);
            if (tier <= 0) {
                tooltip.add(Component.translatable("tooltip.jolcraft.bounty.invalid").withStyle(ChatFormatting.RED));
            } else {
                String tierName = switch (tier) {
                    case 1 -> "Novice";
                    case 2 -> "Apprentice";
                    case 3 -> "Journeyman";
                    case 4 -> "Expert";
                    case 5 -> "Master";
                    default -> "Unknown";
                };
                tooltip.add(Component.translatable("tooltip.jolcraft.bounty.tier", tierName).withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.bounty.locked").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
