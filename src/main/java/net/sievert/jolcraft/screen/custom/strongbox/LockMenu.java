package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.slot.ButtonSlot;
import net.sievert.jolcraft.screen.custom.slot.LockpickSlot;
import net.sievert.jolcraft.sound.JolCraftSounds;

import java.util.Random;


public class LockMenu extends AbstractContainerMenu {
    public final StrongboxBlockEntity blockEntity;
    private final Level level;
    private final Random random = new Random();  // Random instance for sprite selection

    // Used by NeoForge's auto-gui opening
    public LockMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    // Main constructor for the LockMenu
    public LockMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(JolCraftMenuTypes.LOCK_MENU.get(), id);
        this.blockEntity = (StrongboxBlockEntity) blockEntity;
        this.level = inv.player.level();

        // Add the lockpick slot (1 slot)
        this.addSlot(new LockpickSlot(this.blockEntity, 0, 16, 16));  // Lockpick slot

        // Add the button slots (for button clicks)
        this.addSlot(new ButtonSlot(this, 1, 48, 31));  // First button
        this.addSlot(new ButtonSlot(this, 2, 80, 31));  // Second button
        this.addSlot(new ButtonSlot(this, 3, 112, 31));  // Third button

        // Add Player inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 68 + row * 18));
            }
        }

        // Add Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 126));
        }

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                // Safely cast the blockEntity to StrongboxBlockEntity
                if (blockEntity instanceof StrongboxBlockEntity strongbox) {
                    return strongbox.getContainerData().get(0);  // Access the lockpick progress
                }
                return 0;  // Fallback value if blockEntity is not StrongboxBlockEntity
            }

            @Override
            public void set(int value) {
                // Safely cast the blockEntity to StrongboxBlockEntity
                if (blockEntity instanceof StrongboxBlockEntity strongbox) {
                    strongbox.getContainerData().set(0, value);  // Set the lockpick progress
                }
            }
        });

    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        Slot lockpickslot = this.slots.getFirst();
        Slot clickedSlot = this.slots.get(slotId);

        // Only process button slots (1, 2, 3) and ensure we are not already processing
        if (slotId >= 1 && slotId <= 3) {
            if (clickedSlot instanceof ButtonSlot buttonSlot) {
                int buttonIndex = buttonSlot.buttonIndex;
                if (buttonIndex == 1 || buttonIndex == 2 || buttonIndex == 3) {
                    blockEntity.lockpickProgress += 20;  // Increment by 10
                    player.playSound(JolCraftSounds.STRONGBOX_LOCKPICK.get(), 1.0F, 1.0F);
                    blockEntity.setChanged();
                }
            } else {

            }
        }

        super.clicked(slotId, button, clickType, player);

    }



    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        int lockSize = 1;
        int invStart = lockSize;
        int invEnd = invStart + 27;
        int hotbarStart = invEnd;
        int hotbarEnd = hotbarStart + 9;

        // Shift-click from Lock to Player
        if (index < lockSize) {
            if (!moveItemStackTo(stack, invStart, hotbarEnd, true)) return ItemStack.EMPTY;
        } else {
            // Shift-click from Player to Lock
            if (!moveItemStackTo(stack, 0, lockSize, false)) return ItemStack.EMPTY;
        }

        // Handle placing the lockpick into the lock slot (normal or shift-click)
        if (index == 0) {
            this.setCarried(stack);  // Set the carried item as the lockpick when placed in the lock slot

            // Trigger button randomization when the lockpick is placed in the slot (normal or shift-click)
            if (!stack.isEmpty()) {
            }

            // If the lockpick is removed from the slot, reset button states
            if (stack.isEmpty()) {
            }
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }



    // Check if the menu is still valid (if the Strongbox is still there)
    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);  // Ensure the carried item is returned to the player's inventory
        // Reset the current interacting player when the menu is removed
        if (blockEntity instanceof StrongboxBlockEntity strongbox) {
            if (strongbox.getCurrentInteractingPlayer() == player) {
                strongbox.currentInteractingPlayer = null;  // Clear the reference to the interacting player in the BE
                blockEntity.lockpickProgress = 0;
            }
        }

        // Optionally clear the lockpick slot manually
        Slot lockpickSlot = this.slots.get(0);  // Lockpick slot
        if (lockpickSlot != null) {
            ItemStack lockpick = lockpickSlot.getItem();
            if (!lockpick.isEmpty()) {
                // Drop the lockpick if it's in the slot (return to player's inventory)
                dropOrPlaceInInventory(player, lockpick);
                lockpickSlot.set(ItemStack.EMPTY);  // Clear the lockpick from the slot
            }
        }
    }


    private static void dropOrPlaceInInventory(Player player, ItemStack stack) {
        boolean flag;
        boolean flag2;
        label27: {
            flag = player.isRemoved() && player.getRemovalReason() != Entity.RemovalReason.CHANGED_DIMENSION;
            if (player instanceof ServerPlayer serverplayer && serverplayer.hasDisconnected()) {
                flag2 = true;
                break label27;
            }

            flag2 = false;
        }

        boolean flag1 = flag2;
        if (flag || flag1) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer) {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    // Check if the lockpick slot has a lockpick (button should be active only if lockpick is in the slot)
    public boolean isActive() {
        ItemStack lockpick = getLockpickSlotItem();  // Get the lockpick from the menu
        return !lockpick.isEmpty();  // Only activate button if lockpick is in the slot
    }

    // Get the lockpick slot item (Slot 0 is the lockpick slot)
    public ItemStack getLockpickSlotItem() {
        Slot lockpickSlot = this.slots.getFirst();  // Slot 0 is the lockpick slot
        return lockpickSlot.getItem();  // Return the actual ItemStack in the lockpick slot
    }

    public StrongboxBlockEntity getBlockEntity() {
        return blockEntity;
    }


}
