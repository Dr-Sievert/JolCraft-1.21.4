package net.sievert.jolcraft.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.sievert.jolcraft.item.JolCraftItems;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.mojang.text2speech.Narrator.LOGGER;

public class JolCraftDwarfTrades extends VillagerTrades {

    //Buying

    public static class ItemsForGold implements ItemListing {
        private final ItemStack itemStack;
        private final int goldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        public ItemsForGold(Block block, int goldCost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(block), goldCost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForGold(Item item, int goldCost, int numberOfItems, int villagerXp) {
            this(new ItemStack(item), goldCost, numberOfItems, 12, villagerXp);
        }

        public ItemsForGold(Item item, int goldCost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(item), goldCost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForGold(ItemStack itemStack, int goldCost, int numberOfItems, int maxUses, int villagerXp) {
            this(itemStack, goldCost, numberOfItems, maxUses, villagerXp, 0.05F);
        }

        public ItemsForGold(Item item, int goldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(new ItemStack(item), goldCost, numberOfItems, maxUses, villagerXp, priceMultiplier);
        }

        public ItemsForGold(
                Item item, int goldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider
        ) {
            this(new ItemStack(item), goldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
        }

        public ItemsForGold(ItemStack itemStack, int goldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(itemStack, goldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        public ItemsForGold(
                ItemStack itemStack,
                int goldCost,
                int numberOfItems,
                int maxUses,
                int villagerXp,
                float priceMultiplier,
                Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider
        ) {
            this.itemStack = itemStack;
            this.goldCost = goldCost;
            this.itemStack.setCount(numberOfItems);
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }

        @Override
        public MerchantOffer getOffer(Entity p_219699_, RandomSource p_219700_) {
            ItemStack itemstack = this.itemStack.copy();
            Level level = p_219699_.level();
            this.enchantmentProvider
                    .ifPresent(
                            p_348340_ -> EnchantmentHelper.enchantItemFromProvider(
                                    itemstack,
                                    level.registryAccess(),
                                    (ResourceKey<EnchantmentProvider>) p_348340_,
                                    level.getCurrentDifficultyAt(p_219699_.blockPosition()),
                                    p_219700_
                            )
                    );
            return new MerchantOffer(new ItemCost(JolCraftItems.GOLD_COIN, this.goldCost), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    //Selling

    public static class GoldForItems implements ItemListing {
        private final ItemCost itemStack;
        private final int maxUses;
        private final int villagerXp;
        private final int goldAmount;
        private final float priceMultiplier;

        public GoldForItems(ItemLike item, int cost, int maxUses, int villagerXp) {
            this(item, cost, maxUses, villagerXp, 1);
        }

        public GoldForItems(ItemLike item, int cost, int maxUses, int villagerXp, int goldAmount) {
            this(new ItemCost(item.asItem(), cost), maxUses, villagerXp, goldAmount);
        }

        public GoldForItems(ItemCost itemStack, int maxUses, int villagerXp, int goldAmount) {
            this.itemStack = itemStack;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.goldAmount = goldAmount;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(Entity p_219682_, RandomSource p_219683_) {
            return new MerchantOffer(this.itemStack, new ItemStack(JolCraftItems.GOLD_COIN.get(), this.goldAmount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }


    //Buying Explorer Maps

    public static class TreasureMapForGold implements VillagerTrades.ItemListing {
        private final int goldCost;
        private final TagKey<Structure> destination;
        private final String displayName;
        private final Holder<MapDecorationType> destinationType;
        private final int maxUses;
        private final int dwarfXp;

        public TreasureMapForGold(
                int emeraldCost, TagKey<Structure> destination, String displayName, Holder<MapDecorationType> destinationType, int maxUses, int dwarfXp
        ) {
            this.goldCost = emeraldCost;
            this.destination = destination;
            this.displayName = displayName;
            this.destinationType = destinationType;
            this.maxUses = maxUses;
            this.dwarfXp = dwarfXp;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (!(trader.level() instanceof ServerLevel serverLevel)) {
                return null;
            }

            BlockPos targetPos;
            try {
                // Search for the nearest structure matching the tag (radius: 5 chunks)
                targetPos = serverLevel.findNearestMapStructure(this.destination, trader.blockPosition(), 10, true);
            } catch (Exception e) {
                LOGGER.warn("Failed to locate structure for Forge map trade", e);
                return null;
            }

            if (targetPos == null) {
                return null;
            }

            // Create the map item
            ItemStack map = MapItem.create(serverLevel, targetPos.getX(), targetPos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(serverLevel, map);

            // Add marker and display name
            MapItemSavedData.addTargetDecoration(map, targetPos, "+", this.destinationType);
            map.set(DataComponents.ITEM_NAME, Component.translatable(this.displayName));

            // Return the custom map offer with Gold Coin as payment
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN, this.goldCost),
                    Optional.of(new ItemCost(Items.MAP)),
                    map,
                    this.maxUses,
                    this.dwarfXp,
                    0.2F
            );
        }

    }



}
