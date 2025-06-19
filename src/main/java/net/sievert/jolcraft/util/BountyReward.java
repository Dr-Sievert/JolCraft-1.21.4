package net.sievert.jolcraft.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.data.BountyData;
import net.sievert.jolcraft.item.JolCraftItems;

public class BountyReward {

    public static ItemStack getReward(BountyData data, RandomSource random) {
        int tier = data.tier();

        // Reward amounts increase with tier
        return switch (tier) {
            case 1 -> new ItemStack(JolCraftItems.GOLD_COIN.get(), 4 + random.nextInt(3));  // 4-6 coins
            case 2 -> new ItemStack(JolCraftItems.GOLD_COIN.get(), 7 + random.nextInt(4));  // 7-10 coins
            case 3 -> new ItemStack(JolCraftItems.GOLD_COIN.get(), 12 + random.nextInt(5)); // 12-16 coins
            case 4 -> new ItemStack(JolCraftItems.GOLD_COIN.get(), 20 + random.nextInt(8)); // 20-27 coins
            case 5 -> new ItemStack(JolCraftItems.GOLD_COIN.get(), 30 + random.nextInt(10));// 30-39 coins
            default -> ItemStack.EMPTY;
        };
    }
}
