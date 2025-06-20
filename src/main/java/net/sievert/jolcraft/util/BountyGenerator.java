package net.sievert.jolcraft.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.sievert.jolcraft.data.BountyData;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.List;
import java.util.Random;

public class BountyGenerator {

    // Tier 1 - Novice items
    private static final List<ResourceLocation> TIER_1_ITEMS = List.of(
            BuiltInRegistries.ITEM.getKey(Items.COAL),
            BuiltInRegistries.ITEM.getKey(Items.FLINT),
            BuiltInRegistries.ITEM.getKey(Items.COPPER_INGOT),
            BuiltInRegistries.ITEM.getKey(Items.COBBLED_DEEPSLATE),
            BuiltInRegistries.ITEM.getKey(Items.TORCH),
            BuiltInRegistries.ITEM.getKey(Items.CLAY_BALL),
            BuiltInRegistries.ITEM.getKey(Items.IRON_NUGGET)
    );

    // Tier 2 - Apprentice items
    private static final List<ResourceLocation> TIER_2_ITEMS = List.of(
            BuiltInRegistries.ITEM.getKey(Items.IRON_INGOT),
            BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI),
            BuiltInRegistries.ITEM.getKey(Items.REDSTONE),
            BuiltInRegistries.ITEM.getKey(Items.GLOW_INK_SAC),
            BuiltInRegistries.ITEM.getKey(Items.SPIDER_EYE),
            BuiltInRegistries.ITEM.getKey(Items.GUNPOWDER),
            BuiltInRegistries.ITEM.getKey(Items.BONE)
    );

    // Tier 3 - Journeyman items
    private static final List<ResourceLocation> TIER_3_ITEMS = List.of(
            BuiltInRegistries.ITEM.getKey(Items.GOLD_INGOT),
            BuiltInRegistries.ITEM.getKey(Items.EMERALD),
            BuiltInRegistries.ITEM.getKey(Items.AMETHYST_SHARD),
            BuiltInRegistries.ITEM.getKey(Items.BLAZE_POWDER),
            BuiltInRegistries.ITEM.getKey(Items.INK_SAC)

            );

    // Tier 4 - Expert items
    private static final List<ResourceLocation> TIER_4_ITEMS = List.of(
            BuiltInRegistries.ITEM.getKey(Items.ANVIL),
            BuiltInRegistries.ITEM.getKey(Items.GOLDEN_APPLE),
            BuiltInRegistries.ITEM.getKey(Items.BOOK),
            BuiltInRegistries.ITEM.getKey(Items.CAULDRON),
            BuiltInRegistries.ITEM.getKey(Items.ITEM_FRAME),
            BuiltInRegistries.ITEM.getKey(Items.ENDER_PEARL)

    );

    // Tier 5 - Master items (without gems)
    private static final List<ResourceLocation> TIER_5_ITEMS = List.of(
            BuiltInRegistries.ITEM.getKey(Items.NETHERITE_SCRAP),
            BuiltInRegistries.ITEM.getKey(Items.HEART_OF_THE_SEA),
            BuiltInRegistries.ITEM.getKey(Items.DRAGON_BREATH)
    );

    // Your custom gems from JolCraftItems
    private static final List<ResourceLocation> CUSTOM_GEMS = List.of(
            BuiltInRegistries.ITEM.getKey(JolCraftItems.AEGISCORE.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.ASHFANG.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.DEEPMARROW.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.EARTHBLOOD.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.EMBERGLASS.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.FROSTVEIN.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.GRIMSTONE.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.IRONHEART.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.LUMIERE.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.MOONSHARD.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.RUSTAGATE.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.SKYBURROW.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.SUNGLEAM.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.VERDANITE.get()),
            BuiltInRegistries.ITEM.getKey(JolCraftItems.WOECRYSTAL.get())
    );

    public static BountyData generate(Random random, int tier) {
        return switch (tier) {
            case 1 -> generateTier1(random);
            case 2 -> generateTier2(random);
            case 3 -> generateTier3(random);
            case 4 -> generateTier4(random);
            case 5 -> generateTier5(random);
            default -> throw new IllegalArgumentException("Invalid bounty tier: " + tier);
        };
    }

    private static BountyData generateTier1(Random random) {
        ResourceLocation targetItem = TIER_1_ITEMS.get(random.nextInt(TIER_1_ITEMS.size()));
        int requiredCount = 5 + random.nextInt(8); // 5–12
        return new BountyData(targetItem, requiredCount, 1);
    }

    private static BountyData generateTier2(Random random) {
        ResourceLocation targetItem = TIER_2_ITEMS.get(random.nextInt(TIER_2_ITEMS.size()));
        int requiredCount = switch (targetItem.toString()) {
            case "minecraft:gunpowder", "minecraft:spider_eye", "minecraft:glow_ink_sac" -> 3 + random.nextInt(4); // 3–6
            case "minecraft:bone" -> 5 + random.nextInt(5); // 5–9
            default -> 4 + random.nextInt(5); // 4–8 for others
        };
        return new BountyData(targetItem, requiredCount, 2);
    }

    private static BountyData generateTier3(Random random) {
        ResourceLocation targetItem = TIER_3_ITEMS.get(random.nextInt(TIER_3_ITEMS.size()));
        int requiredCount = switch (targetItem.toString()) {
            case "minecraft:emerald" -> 2 + random.nextInt(4); // 2–5
            case "minecraft:porkchop" -> 5 + random.nextInt(6); // 5–10
            default -> 3 + random.nextInt(4); // 3–6 for others
        };
        return new BountyData(targetItem, requiredCount, 3);
    }

    private static BountyData generateTier4(Random random) {
        ResourceLocation targetItem = TIER_4_ITEMS.get(random.nextInt(TIER_4_ITEMS.size()));
        int requiredCount = switch (targetItem.toString()) {
            case "minecraft:golden_apple", "minecraft:book", "minecraft:paper", "minecraft:ink_sac", "minecraft:feather" -> 1 + random.nextInt(2); // 1–2
            case "minecraft:item_frame" -> 1 + random.nextInt(3); // 1–3
            default -> 1; // Anvil, cauldron etc. single items
        };
        return new BountyData(targetItem, requiredCount, 4);
    }

    private static BountyData generateTier5(Random random) {
        // 50% chance for a JolCraft gem, else from normal master pool
        boolean giveGem = !CUSTOM_GEMS.isEmpty() && random.nextBoolean();
        if (giveGem) {
            ResourceLocation gem = CUSTOM_GEMS.get(random.nextInt(CUSTOM_GEMS.size()));
            return new BountyData(gem, 1, 5);
        } else {
            ResourceLocation targetItem = TIER_5_ITEMS.get(random.nextInt(TIER_5_ITEMS.size()));
            int requiredCount = switch (targetItem.toString()) {
                case "minecraft:netherite_scrap", "minecraft:dragon_breath" -> 1 + random.nextInt(2); // 1–2
                default -> 1; // Heart of the sea etc.
            };
            return new BountyData(targetItem, requiredCount, 5);
        }
    }
}
