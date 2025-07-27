package net.sievert.jolcraft.client.compass;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.sievert.jolcraft.data.JolCraftDataComponents;

public record DialItemColor(int rgb) {
    private static final Codec<DialItemColor> FULL_CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf("rgb").forGetter(DialItemColor::rgb)
            ).apply(inst, DialItemColor::new)
    );
    public static final Codec<DialItemColor> CODEC;
    public static final StreamCodec<ByteBuf, DialItemColor> STREAM_CODEC;

    public static int getOrDefault(ItemStack stack, int defaultValue) {
        DialItemColor dialColor = stack.get(JolCraftDataComponents.DIAL_COLOR.get());
        return dialColor != null ? ARGB.opaque(dialColor.rgb()) : defaultValue;
    }

    static {
        CODEC = Codec.withAlternative(FULL_CODEC, Codec.INT, DialItemColor::new);
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                DialItemColor::rgb,
                DialItemColor::new
        );
    }
}
