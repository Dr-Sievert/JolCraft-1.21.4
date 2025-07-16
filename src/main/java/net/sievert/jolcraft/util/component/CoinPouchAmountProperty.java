package net.sievert.jolcraft.util.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.data.JolCraftDataComponents;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public final class CoinPouchAmountProperty implements SelectItemModelProperty<Integer> {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath("jolcraft", "coin_pouch_amount");
    public static final CoinPouchAmountProperty INSTANCE = new CoinPouchAmountProperty();

    public static final MapCodec<CoinPouchAmountProperty> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final Type<CoinPouchAmountProperty, Integer> TYPE =
            SelectItemModelProperty.Type.create(MAP_CODEC, Codec.INT);

    private CoinPouchAmountProperty() {}

    @Nullable
    @Override
    public Integer get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        int coins = stack.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);

        if (coins == 0) {
            return 0; // "empty" or small
        } else if (coins < 333) {
            return 0; // still "small"
        } else if (coins < 666) {
            return 1; // "large"
        } else {
            return 2; // "full"
        }
    }

    @Override
    public Type<? extends SelectItemModelProperty<Integer>, Integer> type() {
        return TYPE;
    }
}