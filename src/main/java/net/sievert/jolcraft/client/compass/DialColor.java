package net.sievert.jolcraft.client.compass;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record DialColor(int defaultColor) implements ItemTintSource {
    public static final MapCodec<DialColor> MAP_CODEC = RecordCodecBuilder.mapCodec(
            p_386972_ -> p_386972_.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(DialColor::defaultColor)).apply(p_386972_,DialColor::new)
    );

    @Override
    public int calculate(ItemStack p_387455_, @Nullable ClientLevel p_390508_, @Nullable LivingEntity p_390428_) {
        return DialItemColor.getOrDefault(p_387455_, this.defaultColor);
    }

    @Override
    public MapCodec<DialColor> type() {
        return MAP_CODEC;
    }
}
