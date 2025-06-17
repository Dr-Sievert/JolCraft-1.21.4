package net.sievert.jolcraft.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalvageLootHelper {

    private static final Random RANDOM = new Random();

    public static List<ItemStack> generateSalvageLoot(ItemStack scrapItem) {
        List<ItemStack> loot = new ArrayList<>();

        // Always give base scrap
        loot.add(new ItemStack(JolCraftItems.SCRAP.get()));

        // --- GENERAL SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.GENERAL_SCRAP)) {
           return loot;
        }

        // --- TEXTILE SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.TEXTILE_SCRAP)) {
            loot.add(new ItemStack(JolCraftItems.SCRAP.get(), 1 + RANDOM.nextInt(2)));
            if (RANDOM.nextFloat() < 0.35f)
                loot.add(new ItemStack(Items.STRING));
            if (RANDOM.nextFloat() < 0.15f)
                loot.add(new ItemStack(Items.LEATHER));
        }

        // --- REDSTONE SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.REDSTONE_SCRAP)) {
            loot.add(new ItemStack(JolCraftItems.SCRAP.get(), 1 + RANDOM.nextInt(3)));
            if (RANDOM.nextFloat() < 0.30f)
                loot.add(new ItemStack(Items.REDSTONE));
            if (RANDOM.nextFloat() < 0.05f)
                loot.add(new ItemStack(JolCraftItems.SCRAP_HEAP.get()));
        }

        // --- IRON SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.IRON_SCRAP)) {
            loot.add(new ItemStack(JolCraftItems.SCRAP.get(), 1 + RANDOM.nextInt(4)));
            if (RANDOM.nextFloat() < 0.50f)
                loot.add(new ItemStack(Items.IRON_NUGGET, 2 + RANDOM.nextInt(3)));
            if (RANDOM.nextFloat() < 0.15f)
                loot.add(new ItemStack(Items.IRON_INGOT));
            if (RANDOM.nextFloat() < 0.1f)
                loot.add(new ItemStack(JolCraftItems.SCRAP_HEAP.get()));
        }

        // --- GOLD SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.GOLD_SCRAP)) {
            loot.add(new ItemStack(JolCraftItems.SCRAP.get(), 1 + RANDOM.nextInt(5)));
            if (RANDOM.nextFloat() < 0.50f)
                loot.add(new ItemStack(Items.GOLD_NUGGET, 2 + RANDOM.nextInt(3)));
            if (RANDOM.nextFloat() < 0.15f)
                loot.add(new ItemStack(Items.GOLD_INGOT));
            if (RANDOM.nextFloat() < 0.2f)
                loot.add(new ItemStack(JolCraftItems.SCRAP_HEAP.get()));
        }

        // --- MITHRIL SCRAP ---
        if (scrapItem.is(JolCraftTags.Items.MITHRIL_SCRAP)) {
            loot.add(new ItemStack(JolCraftItems.SCRAP.get(), 1 + RANDOM.nextInt(10)));
            if (RANDOM.nextFloat() < 0.3f)
                loot.add(new ItemStack(JolCraftItems.SCRAP_HEAP.get()));
        }

        return loot;
    }
}
