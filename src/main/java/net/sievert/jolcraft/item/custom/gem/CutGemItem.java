package net.sievert.jolcraft.item.custom.gem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class CutGemItem extends Item {

    public static final Map<String, List<Component>> GEM_TOOLTIPS = Map.ofEntries(
            Map.entry("aegiscore", List.of(
                    Component.literal("§9+0.5 ").append(Component.translatable("attribute.name.armor_toughness")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("ashfang", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.jolcraft.attack_damage_increase")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("deepmarrow", List.of(
                    Component.literal("§9+12.5% ").append(Component.translatable("attribute.jolcraft.xp_boost")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("earthblood", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.name.mining_efficiency")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("emberglass", List.of(
                    Component.literal("§9+2 ").append(Component.translatable("attribute.name.max_health")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("frostvein", List.of(
                    Component.literal("§9+20% ").append(Component.translatable("attribute.jolcraft.slow_resist")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("grimstone", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.name.attack_speed")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("ironheart", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.jolcraft.armor_increase")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("lumiere", List.of(
                    Component.literal("§9+25% ").append(Component.translatable("attribute.jolcraft.radiant")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("moonshard", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.jolcraft.movement_speed_boost_night")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("rustagate", List.of(
                    Component.literal("§9+7.5% ").append(Component.translatable("attribute.jolcraft.armor_unbreaking")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("skyburrow", List.of(
                    Component.literal("§9+5% ").append(Component.translatable("attribute.jolcraft.movement_speed_boost_day")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("sungleam", List.of(
                    Component.literal("§9+10% ").append(Component.translatable("attribute.jolcraft.extra_chest_loot")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("verdanite", List.of(
                    Component.literal("§9+25% ").append(Component.translatable("attribute.jolcraft.extra_crop")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            )),
            Map.entry("woecrystal", List.of(
                    Component.literal("§9+10% ").append(Component.translatable("attribute.jolcraft.magic_resistance")).withStyle(ChatFormatting.BLUE).append(" §7(All Slots)")
            ))
    );

    private final String gemKey;

    public CutGemItem(Properties properties, String gemKey) {
        super(properties);
        this.gemKey = gemKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            // Show only one, simple, shift-aware detailed line
            tooltip.add(Component.translatable("tooltip.jolcraft.cut_gem")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }
        else{
            // Always show the main short gem tooltip first
            List<Component> lines = GEM_TOOLTIPS.get(gemKey);
            if (lines != null) tooltip.addAll(lines);
            // Show shift info prompt under the short tooltip
            Component shiftKey = Component.literal("Shift").withStyle(net.minecraft.ChatFormatting.BLUE);
            tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                    .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }


}
