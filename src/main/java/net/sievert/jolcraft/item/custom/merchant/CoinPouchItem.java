package net.sievert.jolcraft.item.custom.merchant;

import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.data.custom.item.CoinPouchTooltip;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.sound.JolCraftSounds;

import java.util.List;
import java.util.Optional;

public class CoinPouchItem extends Item {

    public static final int MAX_COINS = 999;

    private static final int BAR_COLOR = ARGB.color(255, 232, 193, 67);

    public CoinPouchItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    // --- Extract coins by right-clicking pouch in inventory (to slot or cursor) ---
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack cursor, Slot slot, ClickAction action, Player player, SlotAccess access) {
        // Right click, cursor empty: Extract up to 64 coins to the slot
        if (action == ClickAction.SECONDARY && cursor.isEmpty()) {
            int current = pouch.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
            if (current > 0) {
                int toGive = Math.min(64, current);
                ItemStack out = new ItemStack(JolCraftItems.GOLD_COIN.get(), toGive);
                pouch.set(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), current - toGive);
                access.set(out);

                // Play appropriate sound based on how many were extracted
                if (toGive == 1) playSingleSound(player);
                else playStackSound(player);

                broadcastChangesOnContainerMenu(player);
                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
        }
        // Left or right click: Insert coins from slot into pouch
        ItemStack slotStack = access.get();
        if (isGoldCoin(slotStack)) {
            int current = pouch.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
            int addable = 0;
            if (action == ClickAction.PRIMARY) {
                addable = Math.min(MAX_COINS - current, slotStack.getCount());
            } else if (action == ClickAction.SECONDARY) {
                addable = Math.min(MAX_COINS - current, 1);
            }
            // For insert (clicking coins into pouch)
            if (addable > 0) {
                boolean wasEmpty = (current == 0);
                pouch.set(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), current + addable);
                slotStack.shrink(addable);
                access.set(slotStack.isEmpty() ? ItemStack.EMPTY : slotStack);

                // Sound logic
                if (addable == 1 && wasEmpty) {
                    // 1 coin, pouch was empty
                    playPouchInsertSound(player);
                } else if (addable == 1) {
                    playSingleSound(player);
                } else {
                    playStackSound(player);
                }

                broadcastChangesOnContainerMenu(player);
            } else {
                playInsertFailSound(player);
            }
            return true;
        }
        return false;
    }

    // --- Fill pouch from player inventory on right-click in hand ---
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack pouch = player.getItemInHand(hand);
        int current = pouch.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
        int canAdd = MAX_COINS - current;
        if (canAdd > 0) {
            int added = tryConsumeGoldCoinsFromInventory(player, canAdd);
            if (added > 0) {
                boolean wasEmpty = (current == 0);
                pouch.set(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), current + added);

                if (added == 1 && wasEmpty) {
                    playPouchInsertSound(player);
                } else if (added == 1) {
                    playSingleSound(player);
                } else {
                    playStackSound(player);
                }

                broadcastChangesOnContainerMenu(player);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS;
            }
        }
        playInsertFailSound(player);
        return InteractionResult.PASS;
    }


    // --- Utility: Is this a gold coin? ---
    private boolean isGoldCoin(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == JolCraftItems.GOLD_COIN.get();
    }

    // --- Utility: Remove up to max coins from inventory ---
    private int tryConsumeGoldCoinsFromInventory(Player player, int max) {
        int removed = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (isGoldCoin(slotStack)) {
                int toTake = Math.min(slotStack.getCount(), max - removed);
                slotStack.shrink(toTake);
                removed += toTake;
                if (removed >= max) break;
            }
        }
        return removed;
    }

    // --- Bar ---
    @Override
    public boolean isBarVisible(ItemStack stack) {
        int amount = stack.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
        return amount > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int amount = stack.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
        return Math.min(13, (int) ((amount / (float) MAX_COINS) * 13));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }


    // --- Hover render ---
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        int amount = stack.getOrDefault(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
        return amount > 0
                ? Optional.of(new CoinPouchTooltip(amount))
                : Optional.empty();
    }


    // --- Sounds (copying BundleItem) ---
    private void playStackSound(Player player) {
        player.playSound(JolCraftSounds.COIN_STACK.get(), 0.8F + player.level().random.nextFloat() * 0.2F, 1.0F + player.level().random.nextFloat() * 0.2F);
    }
    private void playSingleSound(Player player) {
        player.playSound(JolCraftSounds.COIN_SINGLE.get(), 0.8F + player.level().random.nextFloat() * 0.2F, 1.0F + player.level().random.nextFloat() * 0.2F);
    }
    private void playPouchInsertSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 1.3F);
    }
    private void playInsertFailSound(Player player) {
        player.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 0.6F, 1.3F);
    }


    // --- UI Helper ---
    private void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu != null) {
            menu.slotsChanged(player.getInventory());
        }
    }

    // --- Set Data Component ---
    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        stack.set(JolCraftDataComponents.COIN_POUCH_AMOUNT.get(), 0);
    }

    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.jolcraft.coin_pouch")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        } else {
            net.minecraft.network.chat.Component shiftKey = net.minecraft.network.chat.Component.literal("Shift")
                    .withStyle(net.minecraft.ChatFormatting.BLUE);
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.jolcraft.shift", shiftKey)
                    .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }



}
