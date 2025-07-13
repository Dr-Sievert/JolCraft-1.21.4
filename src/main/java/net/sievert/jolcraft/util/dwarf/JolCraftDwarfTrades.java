package net.sievert.jolcraft.util.dwarf;

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
import net.sievert.jolcraft.util.bounty.BountyHelper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

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

    public static class ItemsAndGoldToItems implements VillagerTrades.ItemListing {
        private final ItemCost fromItem;
        private final int goldCost;
        private final ItemStack toItem;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        public ItemsAndGoldToItems(ItemLike fromItem, int fromItemCount, int goldCost, ItemLike toItem, int toItemCount, int maxUses, int villagerXp, float priceMultiplier) {
            this(new ItemCost(fromItem, fromItemCount), goldCost, new ItemStack(toItem, toItemCount), maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        public ItemsAndGoldToItems(
                ItemLike fromItem, int fromItemCount, int goldCost, ItemStack toItem, int toItemCount, int maxUses, int villagerXp, float priceMultiplier
        ) {
            this(new ItemCost(fromItem, fromItemCount), goldCost, toItem.copyWithCount(toItemCount), maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        public ItemsAndGoldToItems(
                ItemLike fromItem,
                int fromItemAmount,
                int goldCost,
                ItemLike toItem,
                int toItemCount,
                int maxUses,
                int villagerXp,
                float priceMultiplier,
                ResourceKey<EnchantmentProvider> enchantmentProvider
        ) {
            this(new ItemCost(fromItem, fromItemAmount), goldCost, new ItemStack(toItem, toItemCount), maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
        }

        public ItemsAndGoldToItems(
                ItemCost fromItem,
                int goldCost,
                ItemStack toItem,
                int maxUses,
                int villagerXp,
                float priceMultiplier,
                Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider
        ) {
            this.fromItem = fromItem;
            this.goldCost = goldCost;
            this.toItem = toItem;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity p_219696_, RandomSource p_219697_) {
            ItemStack itemstack = this.toItem.copy();
            Level level = p_219696_.level();
            this.enchantmentProvider
                    .ifPresent(
                            p_348335_ -> EnchantmentHelper.enchantItemFromProvider(
                                    itemstack,
                                    level.registryAccess(),
                                    (ResourceKey<EnchantmentProvider>)p_348335_,
                                    level.getCurrentDifficultyAt(p_219696_.blockPosition()),
                                    p_219697_
                            )
                    );
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), this.goldCost), Optional.of(this.fromItem), itemstack, 0, this.maxUses, this.villagerXp, this.priceMultiplier
            );
        }

    }

    public static class ItemsWithDataForGold implements VillagerTrades.ItemListing {
        private final Item item;
        private final int count;
        private final int goldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final Consumer<ItemStack> stackModifier;

        public ItemsWithDataForGold(
                Item item,
                int count,
                int goldCost,
                int maxUses,
                int villagerXp,
                Consumer<ItemStack> stackModifier
        ) {
            this(item, count, goldCost, maxUses, villagerXp, 0.05F, stackModifier);
        }

        public ItemsWithDataForGold(
                Item item,
                int count,
                int goldCost,
                int maxUses,
                int villagerXp,
                float priceMultiplier,
                Consumer<ItemStack> stackModifier
        ) {
            this.item = item;
            this.count = count;
            this.goldCost = goldCost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.stackModifier = stackModifier;
        }

        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            ItemStack stack = new ItemStack(item, count);
            if (stackModifier != null) {
                stackModifier.accept(stack);
            }

            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), goldCost),
                    stack,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }

    public static class ItemsAndGoldToItemsWithData implements VillagerTrades.ItemListing {
        private final ItemCost fromItem;
        private final int goldCost;
        private final Item toItem;
        private final int toCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final Consumer<ItemStack> stackModifier;

        public ItemsAndGoldToItemsWithData(
                ItemLike fromItem,
                int fromItemAmount,
                int goldCost,
                Item toItem,
                int toCount,
                int maxUses,
                int villagerXp,
                float priceMultiplier,
                Consumer<ItemStack> stackModifier
        ) {
            this.fromItem = new ItemCost(fromItem, fromItemAmount);
            this.goldCost = goldCost;
            this.toItem = toItem.asItem();
            this.toCount = toCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.stackModifier = stackModifier != null ? stackModifier : (s) -> {};
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack output = new ItemStack(this.toItem, this.toCount);
            this.stackModifier.accept(output);

            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), goldCost),
                    Optional.of(fromItem),
                    output,
                    0,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }



    //Exchanging
    public static class ItemForItem implements VillagerTrades.ItemListing {
        private final ItemStack input;
        private final ItemStack output;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemForItem(ItemLike input, int inputCount, ItemLike output, int outputCount, int maxUses, int villagerXp) {
            this.input = new ItemStack(input, inputCount);
            this.output = new ItemStack(output, outputCount);
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = 0.05F;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return new MerchantOffer(new ItemCost(this.input.getItem(), this.input.getCount()), this.output, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class BountyItemForItem extends JolCraftDwarfTrades.ItemForItem {
        private final int bountyTier;

        public BountyItemForItem(
                ItemLike input, int inputCount,
                ItemLike output, int outputCount,
                int maxUses, int villagerXp,
                int bountyTier
        ) {
            super(input, inputCount, output, outputCount, maxUses, villagerXp);
            this.bountyTier = bountyTier;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            MerchantOffer offer = super.getOffer(entity, random);
            if (offer == null) return null;

            ItemStack result = offer.getResult().copy();
            BountyHelper.setBountyTier(result, bountyTier);

            return new MerchantOffer(
                    offer.getItemCostA(),
                    offer.getItemCostB(),
                    result,
                    offer.getMaxUses(),
                    offer.getXp(),
                    offer.getPriceMultiplier()
            );
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
