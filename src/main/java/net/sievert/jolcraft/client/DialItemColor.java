package net.sievert.jolcraft.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.sievert.jolcraft.data.JolCraftDataComponents;

import java.util.Locale;
import java.util.function.Consumer;

public record DialItemColor(int rgb, boolean showInTooltip) implements TooltipProvider {
    private static final Codec<DialItemColor> FULL_CODEC = RecordCodecBuilder.create((p_337944_) -> p_337944_.group(Codec.INT.fieldOf("rgb").forGetter(DialItemColor::rgb), Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(DialItemColor::showInTooltip)).apply(p_337944_, DialItemColor::new));
    public static final Codec<DialItemColor> CODEC;
    public static final StreamCodec<ByteBuf, DialItemColor> STREAM_CODEC;

    public static int getOrDefault(ItemStack stack, int defaultValue) {
        DialItemColor dialColor = stack.get(JolCraftDataComponents.DIAL_COLOR.get());
        return dialColor != null ? ARGB.opaque(dialColor.rgb()) : defaultValue;
    }

    public void addToTooltip(Item.TooltipContext p_340955_, Consumer<Component> p_331920_, TooltipFlag p_330757_) {
        if (this.showInTooltip) {
            if (p_330757_.isAdvanced()) {
                p_331920_.accept(Component.translatable("item.color", new Object[]{String.format(Locale.ROOT, "#%06X", this.rgb)}).withStyle(ChatFormatting.GRAY));
            } else {
                p_331920_.accept(Component.translatable("item.dyed").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
            }
        }

    }

    public DialItemColor withTooltip(boolean showInTooltip) {
        return new DialItemColor(this.rgb, showInTooltip);
    }

    static {
        CODEC = Codec.withAlternative(FULL_CODEC, Codec.INT, (p_332619_) -> new DialItemColor(p_332619_, true));
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, DialItemColor::rgb, ByteBufCodecs.BOOL, DialItemColor::showInTooltip, DialItemColor::new);
    }
}
