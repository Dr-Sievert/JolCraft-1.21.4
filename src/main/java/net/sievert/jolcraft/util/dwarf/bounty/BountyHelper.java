package net.sievert.jolcraft.util.dwarf.bounty;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponentType;
import net.sievert.jolcraft.data.JolCraftDataComponents;

public class BountyHelper {

    public static void setBountyTier(ItemStack stack, int tier) {
        // Get the actual DataComponentType<Integer> from your DeferredHolder
        DataComponentType<Integer> bountyTierComponent = JolCraftDataComponents.BOUNTY_TIER.get();
        stack.set(bountyTierComponent, tier);
    }

    public static int getBountyTier(ItemStack stack) {
        DataComponentType<Integer> bountyTierComponent = JolCraftDataComponents.BOUNTY_TIER.get();
        // Default to 0 if not present
        return stack.getOrDefault(bountyTierComponent, 0);
    }
}