package net.sievert.jolcraft.util.bounty;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.ArrayList;
import java.util.List;

public class BountyReward {

    public static List<ItemStack> getReward(BountyData data, RandomSource random) {
        int tier = data.tier();
        List<ItemStack> rewards = new ArrayList<>();

        // Base coins per tier
        int coins = switch (tier) {
            case 1 -> 4 + random.nextInt(3);   // 4-6 coins
            case 2 -> 7 + random.nextInt(4);   // 7-10 coins
            case 3 -> 12 + random.nextInt(5);  // 12-16 coins
            case 4 -> 20 + random.nextInt(8);  // 20-27 coins
            case 5 -> 30 + random.nextInt(10); // 30-39 coins
            default -> 0;
        };

        if (coins > 0) {
            rewards.add(new ItemStack(JolCraftItems.GOLD_COIN.get(), coins));
        }

        // Chance for bonus restock crate at higher tiers
        float crateChance = switch (tier) {
            case 2 -> 0.125f;
            case 3 -> 0.25f;
            case 4 -> 0.5f;
            case 5 -> 0.7f;
            default -> 0f;
        };
        if (crateChance > 0 && random.nextFloat() < crateChance) {
            rewards.add(new ItemStack(JolCraftItems.RESTOCK_CRATE.get()));
        }

        return rewards;
    }

}
