package net.sievert.jolcraft.util.dwarf;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.util.bounty.BountyHelper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class JolCraftDwarfTrades extends VillagerTrades {

    // -- BUYING (player pays gold, gets item) --

    public static class ItemsForGold implements ItemListing {
        private final Item item;
        private final int minItemCount, maxItemCount;
        private final int minGoldCost, maxGoldCost;
        private final int maxUses, villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        // Full constructor (random item count, random gold cost)
        public ItemsForGold(Item item, int minGoldCost, int maxGoldCost, int minItemCount, int maxItemCount, int maxUses, int villagerXp) {
            this(item, minGoldCost, maxGoldCost, minItemCount, maxItemCount, maxUses, villagerXp, 0.05F, Optional.empty());
        }

        // Constructor: fixed item output, random gold cost
        public ItemsForGold(Item item, int minGoldCost, int maxGoldCost, int itemCount, int maxUses, int villagerXp) {
            this(item, minGoldCost, maxGoldCost, itemCount, itemCount, maxUses, villagerXp, 0.05F, Optional.empty());
        }

        // Backward compatibility: fixed item, fixed gold
        public ItemsForGold(Item item, int goldCost, int itemCount, int maxUses, int villagerXp) {
            this(item, goldCost, goldCost, itemCount, itemCount, maxUses, villagerXp);
        }

        // ItemStack overload for advanced cases
        public ItemsForGold(ItemStack stack, int minGoldCost, int maxGoldCost, int maxUses, int villagerXp) {
            this(stack.getItem(), minGoldCost, maxGoldCost, stack.getCount(), stack.getCount(), maxUses, villagerXp);
        }

        public ItemsForGold(Item item, int minGoldCost, int maxGoldCost, int minItemCount, int maxItemCount, int maxUses, int villagerXp, float priceMultiplier, Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
            this.item = item;
            this.minItemCount = minItemCount;
            this.maxItemCount = maxItemCount;
            this.minGoldCost = minGoldCost;
            this.maxGoldCost = maxGoldCost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int gold = Mth.nextInt(random, minGoldCost, maxGoldCost);
            int items = Mth.nextInt(random, minItemCount, maxItemCount);
            ItemStack stack = new ItemStack(item, items);
            if (enchantmentProvider.isPresent()) {
                Level level = entity.level();
                EnchantmentHelper.enchantItemFromProvider(stack, level.registryAccess(), enchantmentProvider.get(), level.getCurrentDifficultyAt(entity.blockPosition()), random);
            }
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), gold),
                    stack,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }



    // -- BUYING + INPUT ITEM (player pays item+gold, gets item) --

    public static class ItemsAndGoldToItems implements ItemListing {
        private final Item inputItem;
        private final int minInputCount, maxInputCount;
        private final int minGoldCost, maxGoldCost;
        private final Item outputItem;
        private final int minOutputCount, maxOutputCount;
        private final int maxUses, villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        // Full constructor (random everything)
        public ItemsAndGoldToItems(ItemLike fromItem, int minInputCount, int maxInputCount,
                                   int minGoldCost, int maxGoldCost,
                                   ItemLike toItem, int minOutputCount, int maxOutputCount,
                                   int maxUses, int villagerXp, float priceMultiplier) {
            this(fromItem, minInputCount, maxInputCount, minGoldCost, maxGoldCost, toItem, minOutputCount, maxOutputCount, maxUses, villagerXp, priceMultiplier, Optional.empty());
        }
        public ItemsAndGoldToItems(ItemLike fromItem, int minInputCount, int maxInputCount,
                                   int minGoldCost, int maxGoldCost,
                                   ItemLike toItem, int minOutputCount, int maxOutputCount,
                                   int maxUses, int villagerXp, float priceMultiplier,
                                   Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
            this.inputItem = fromItem.asItem();
            this.minInputCount = minInputCount;
            this.maxInputCount = maxInputCount;
            this.minGoldCost = minGoldCost;
            this.maxGoldCost = maxGoldCost;
            this.outputItem = toItem.asItem();
            this.minOutputCount = minOutputCount;
            this.maxOutputCount = maxOutputCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }
        // Single value constructor (backward compat)
        public ItemsAndGoldToItems(ItemLike fromItem, int fromCount, int goldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
            this(fromItem, fromCount, fromCount, goldCost, goldCost, toItem, toCount, toCount, maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        // Fixed input/output, random gold cost
        public ItemsAndGoldToItems(
                ItemLike fromItem, int fromCount,
                int minGoldCost, int maxGoldCost,
                ItemLike toItem, int toCount,
                int maxUses, int villagerXp, float priceMultiplier
        ) {
            this(
                    fromItem, fromCount, fromCount, minGoldCost, maxGoldCost,
                    toItem, toCount, toCount, maxUses, villagerXp, priceMultiplier, Optional.empty()
            );
        }
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int inputCount = Mth.nextInt(random, minInputCount, maxInputCount);
            int outputCount = Mth.nextInt(random, minOutputCount, maxOutputCount);
            int gold = Mth.nextInt(random, minGoldCost, maxGoldCost);
            ItemStack input = new ItemStack(inputItem, inputCount);
            ItemStack output = new ItemStack(outputItem, outputCount);
            if (enchantmentProvider.isPresent()) {
                Level level = entity.level();
                EnchantmentHelper.enchantItemFromProvider(output, level.registryAccess(), enchantmentProvider.get(), level.getCurrentDifficultyAt(entity.blockPosition()), random);
            }
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), gold),
                    Optional.of(new ItemCost(inputItem, inputCount)),
                    output,
                    0,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }

    // -- WITH CUSTOM STACK MODIFIERS --

    public static class ItemsWithDataForGold implements VillagerTrades.ItemListing {
        private final Item item;
        private final int minItemCount, maxItemCount;
        private final int minGoldCost, maxGoldCost;
        private final int maxUses, villagerXp;
        private final float priceMultiplier;
        private final Consumer<ItemStack> stackModifier;
        public ItemsWithDataForGold(Item item, int minItemCount, int maxItemCount, int minGoldCost, int maxGoldCost, int maxUses, int villagerXp, Consumer<ItemStack> stackModifier) {
            this(item, minItemCount, maxItemCount, minGoldCost, maxGoldCost, maxUses, villagerXp, 0.05F, stackModifier);
        }
        public ItemsWithDataForGold(Item item, int minItemCount, int maxItemCount, int minGoldCost, int maxGoldCost, int maxUses, int villagerXp, float priceMultiplier, Consumer<ItemStack> stackModifier) {
            this.item = item;
            this.minItemCount = minItemCount;
            this.maxItemCount = maxItemCount;
            this.minGoldCost = minGoldCost;
            this.maxGoldCost = maxGoldCost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.stackModifier = stackModifier != null ? stackModifier : s -> {};
        }
        // Legacy
        public ItemsWithDataForGold(Item item, int itemCount, int goldCost, int maxUses, int villagerXp, Consumer<ItemStack> stackModifier) {
            this(item, itemCount, itemCount, goldCost, goldCost, maxUses, villagerXp, stackModifier);
        }
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            int itemCount = Mth.nextInt(random, minItemCount, maxItemCount);
            int gold = Mth.nextInt(random, minGoldCost, maxGoldCost);
            ItemStack stack = new ItemStack(item, itemCount);
            stackModifier.accept(stack);
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), gold),
                    stack,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }


    public static class ItemsAndGoldToItemsWithData implements VillagerTrades.ItemListing {
        private final Item inputItem;
        private final int minInputCount, maxInputCount;
        private final int minGoldCost, maxGoldCost;
        private final Item outputItem;
        private final int minOutputCount, maxOutputCount;
        private final int maxUses, villagerXp;
        private final float priceMultiplier;
        private final Consumer<ItemStack> stackModifier;

        public ItemsAndGoldToItemsWithData(ItemLike input, int minInputCount, int maxInputCount, int minGoldCost, int maxGoldCost,
                                           Item output, int minOutputCount, int maxOutputCount,
                                           int maxUses, int villagerXp, float priceMultiplier, Consumer<ItemStack> stackModifier) {
            this.inputItem = input.asItem();
            this.minInputCount = minInputCount;
            this.maxInputCount = maxInputCount;
            this.minGoldCost = minGoldCost;
            this.maxGoldCost = maxGoldCost;
            this.outputItem = output.asItem();
            this.minOutputCount = minOutputCount;
            this.maxOutputCount = maxOutputCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.stackModifier = stackModifier != null ? stackModifier : s -> {};
        }
        // Legacy
        public ItemsAndGoldToItemsWithData(ItemLike input, int inputCount, int goldCost, Item output, int outputCount, int maxUses, int villagerXp, float priceMultiplier, Consumer<ItemStack> stackModifier) {
            this(input, inputCount, inputCount, goldCost, goldCost, output, outputCount, outputCount, maxUses, villagerXp, priceMultiplier, stackModifier);
        }
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int inputCount = Mth.nextInt(random, minInputCount, maxInputCount);
            int gold = Mth.nextInt(random, minGoldCost, maxGoldCost);
            int outputCount = Mth.nextInt(random, minOutputCount, maxOutputCount);
            ItemStack stack = new ItemStack(outputItem, outputCount);
            stackModifier.accept(stack);
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), gold),
                    Optional.of(new ItemCost(inputItem, inputCount)),
                    stack,
                    0,
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }


    // -- EXCHANGING (item for item, with ranges) --

    public static class ItemForItem implements VillagerTrades.ItemListing {
        private final ItemLike input;
        private final int minInputCount, maxInputCount;
        private final ItemLike output;
        private final int minOutputCount, maxOutputCount;
        private final int maxUses, villagerXp;
        private final float priceMultiplier;
        public ItemForItem(ItemLike input, int minInputCount, int maxInputCount, ItemLike output, int minOutputCount, int maxOutputCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.input = input;
            this.minInputCount = minInputCount;
            this.maxInputCount = maxInputCount;
            this.output = output;
            this.minOutputCount = minOutputCount;
            this.maxOutputCount = maxOutputCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }
        public ItemForItem(ItemLike input, int inputCount, ItemLike output, int outputCount, int maxUses, int villagerXp) {
            this(input, inputCount, inputCount, output, outputCount, outputCount, maxUses, villagerXp, 0.05F);
        }
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int inputCount = Mth.nextInt(random, minInputCount, maxInputCount);
            int outputCount = Mth.nextInt(random, minOutputCount, maxOutputCount);
            return new MerchantOffer(
                    new ItemCost(input.asItem(), inputCount),
                    new ItemStack(output.asItem(), outputCount),
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }

    public static class BountyItemForItem extends JolCraftDwarfTrades.ItemForItem {
        private final int bountyTier;
        public BountyItemForItem(ItemLike input, int inputCount, ItemLike output, int outputCount, int maxUses, int villagerXp, int bountyTier) {
            super(input, inputCount, output, outputCount, maxUses, villagerXp);
            this.bountyTier = bountyTier;
        }
        public BountyItemForItem(ItemLike input, int minInputCount, int maxInputCount, ItemLike output, int minOutputCount, int maxOutputCount, int maxUses, int villagerXp, int bountyTier) {
            super(input, minInputCount, maxInputCount, output, minOutputCount, maxOutputCount, maxUses, villagerXp, 0.05F);
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


    // -- SELLING (player sells item, gets gold payout) --

    public static class GoldForItems implements VillagerTrades.ItemListing {
        private final ItemLike item;
        private final int minInputCount, maxInputCount;
        private final int maxUses, villagerXp;
        private final int minGoldAmount, maxGoldAmount;
        private final float priceMultiplier;
        // Full constructor
        public GoldForItems(ItemLike item, int minInputCount, int maxInputCount, int maxUses, int villagerXp, int minGoldAmount, int maxGoldAmount) {
            this.item = item;
            this.minInputCount = minInputCount;
            this.maxInputCount = maxInputCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.minGoldAmount = minGoldAmount;
            this.maxGoldAmount = maxGoldAmount;
            this.priceMultiplier = 0.05F;
        }
        // Single value constructor (backward compat)
        public GoldForItems(ItemLike item, int inputCount, int maxUses, int villagerXp, int goldAmount) {
            this(item, inputCount, inputCount, maxUses, villagerXp, goldAmount, goldAmount);
        }
        public GoldForItems(ItemLike item, int inputCount, int maxUses, int villagerXp, int minGold, int maxGold) {
            this(item, inputCount, inputCount, maxUses, villagerXp, minGold, maxGold);
        }
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int inputCount = Mth.nextInt(random, minInputCount, maxInputCount);
            int payout = Mth.nextInt(random, minGoldAmount, maxGoldAmount);
            return new MerchantOffer(
                    new ItemCost(item.asItem(), inputCount),
                    new ItemStack(JolCraftItems.GOLD_COIN.get(), payout),
                    maxUses,
                    villagerXp,
                    priceMultiplier
            );
        }
    }


    // -- MAPS (special) --

    public static class TreasureMapForGold implements VillagerTrades.ItemListing {
        private final int goldCost;
        private final TagKey<Structure> destinationTag;
        private final String displayName;
        private final Holder<MapDecorationType> destinationType;
        private final int maxUses;
        private final int dwarfXp;

        public TreasureMapForGold(int goldCost, TagKey<Structure> destinationTag, String displayName, Holder<MapDecorationType> destinationType, int maxUses, int dwarfXp) {
            this.goldCost = goldCost;
            this.destinationTag = destinationTag;
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
                targetPos = serverLevel.findNearestMapStructure(this.destinationTag, trader.blockPosition(), 100, true);
            } catch (Exception e) {
                return null;
            }
            if (targetPos == null) return null;
            ItemStack map = MapItem.create(serverLevel, targetPos.getX(), targetPos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(serverLevel, map);
            MapItemSavedData.addTargetDecoration(map, targetPos, "+", this.destinationType);
            map.set(DataComponents.ITEM_NAME, Component.translatable(this.displayName));
            return new MerchantOffer(
                    new ItemCost(JolCraftItems.GOLD_COIN.get(), this.goldCost),
                    Optional.of(new ItemCost(Items.MAP, 1)),
                    map,
                    0,
                    this.maxUses,
                    this.dwarfXp,
                    0.2F
            );
        }
    }
}
