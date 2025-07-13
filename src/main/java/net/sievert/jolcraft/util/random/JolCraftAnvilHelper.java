package net.sievert.jolcraft.util.random;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * An exact copy of AnvilMenu#createResult, adapted as a helper.
 * If you need JolCraft-specific logic (like gold name), do it *after* calling this.
 */
public class JolCraftAnvilHelper {
    /**
     * Result holder.
     */
    public record AnvilResult(ItemStack result, int cost, int materialCost) {}

    /**
     * Runs the full vanilla anvil logic, exactly as in AnvilMenu#createResult.
     * Use for any custom event where you want to reconstruct the expected output.
     *
     * @param left      The left input stack (input slot 0)
     * @param right     The right input stack (input slot 1)
     * @param rename    New name (may be null or blank)
     * @param player    The player using the anvil
     * @return          AnvilResult: (result stack, level cost, material cost)
     */
    public static AnvilResult vanillaResult(
            ItemStack left, ItemStack right,
            @Nullable String rename,
            Player player
    ) {
        ItemStack itemstack = left;
        int repairItemCountCost = 0;
        int cost = 1;
        int i = 0;
        long j = 0L;
        int k = 0;

        if (!itemstack.isEmpty() && EnchantmentHelper.canStoreEnchantments(itemstack)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = right;
            ItemEnchantments.Mutable itemenchantments$mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemstack1));
            j += (long)itemstack.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0)).intValue()
                    + (long)itemstack2.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0)).intValue();
            repairItemCountCost = 0;
            boolean flag = false;
            // Skipping hooks here; call your hooks outside the helper if needed

            if (!itemstack2.isEmpty()) {
                flag = itemstack2.has(DataComponents.STORED_ENCHANTMENTS);
                if (itemstack1.isDamageableItem() && itemstack.isValidRepairItem(itemstack2)) {
                    int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        return new AnvilResult(ItemStack.EMPTY, 0, 0);
                    }

                    int j3;
                    for (j3 = 0; l2 > 0 && j3 < itemstack2.getCount(); j3++) {
                        int k3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(k3);
                        i++;
                        l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }

                    repairItemCountCost = j3;
                } else {
                    if (!flag && (!itemstack1.is(itemstack2.getItem()) || !itemstack1.isDamageableItem())) {
                        return new AnvilResult(ItemStack.EMPTY, 0, 0);
                    }

                    if (itemstack1.isDamageableItem() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for (Entry<Holder<Enchantment>> entry : itemenchantments.entrySet()) {
                        Holder<Enchantment> holder = entry.getKey();
                        int i2 = itemenchantments$mutable.getLevel(holder);
                        int j2 = entry.getIntValue();
                        j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                        Enchantment enchantment = holder.value();
                        boolean flag1 = itemstack.supportsEnchantment(holder);
                        if (player.getAbilities().instabuild) {
                            flag1 = true;
                        }

                        for (Holder<Enchantment> holder1 : itemenchantments$mutable.keySet()) {
                            if (!holder1.equals(holder) && !Enchantment.areCompatible(holder, holder1)) {
                                flag1 = false;
                                i++;
                            }
                        }

                        if (!flag1) {
                            flag3 = true;
                        } else {
                            flag2 = true;
                            if (j2 > enchantment.getMaxLevel()) {
                                j2 = enchantment.getMaxLevel();
                            }

                            itemenchantments$mutable.set(holder, j2);
                            int l3 = enchantment.getAnvilCost();
                            if (flag) {
                                l3 = Math.max(1, l3 / 2);
                            }

                            i += l3 * j2;
                            if (itemstack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (flag3 && !flag2) {
                        return new AnvilResult(ItemStack.EMPTY, 0, 0);
                    }
                }
            }

            // Rename logic
            String filtered = (rename != null) ? StringUtil.filterText(rename) : "";
            if (!filtered.isBlank()) {
                if (!filtered.equals(itemstack.getHoverName().getString())) {
                    k = 1;
                    i += k;
                    // Vanilla: CUSTOM_NAME, *not* ITEM_NAME
                    itemstack1.set(DataComponents.CUSTOM_NAME, Component.literal(filtered));
                }
            } else if (itemstack.has(DataComponents.CUSTOM_NAME)) {
                k = 1;
                i += k;
                itemstack1.remove(DataComponents.CUSTOM_NAME);
            }
            if (flag && !itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

            int k2 = i <= 0 ? 0 : (int) Mth.clamp(j + (long) i, 0L, 2147483647L);
            cost = k2;
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0) {
                if (cost >= 40) {
                    cost = 39;
                }
                // onlyRenaming = true; // Unneeded here
            }

            if (cost >= 40 && !player.getAbilities().instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int i3 = itemstack1.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (i3 < itemstack2.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    i3 = itemstack2.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (k != i || k == 0) {
                    i3 = calculateIncreasedRepairCost(i3);
                }

                itemstack1.set(DataComponents.REPAIR_COST, i3);
                EnchantmentHelper.setEnchantments(itemstack1, itemenchantments$mutable.toImmutable());
            }

            return new AnvilResult(itemstack1, cost, repairItemCountCost);
        } else {
            return new AnvilResult(ItemStack.EMPTY, 0, 0);
        }
    }

    // Vanilla logic: (old * 2) + 1, capped.
    public static int calculateIncreasedRepairCost(int oldRepairCost) {
        return (int)Math.min((long)oldRepairCost * 2L + 1L, Integer.MAX_VALUE);
    }
}
