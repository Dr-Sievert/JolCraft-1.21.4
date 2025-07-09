package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sievert.jolcraft.block.custom.StrongboxBlock;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.slot.LockpickSlot;
import net.sievert.jolcraft.sound.JolCraftSounds;

import java.util.Random;


public class LockMenu extends AbstractContainerMenu {
    public final StrongboxBlockEntity blockEntity;
    private final Level level;
    private final Random random = new Random();  // Random instance for sprite selection
    final int MAX_PROGRESS = 200;

    // Used by NeoForge's auto-gui opening
    public LockMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    // Main constructor for the LockMenu
    public LockMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(JolCraftMenuTypes.LOCK_MENU.get(), id);
        this.blockEntity = (StrongboxBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.addDataSlots(data);
        updateLockpickButton();
        updatechanges();

        // Add the lockpick slot
        this.addSlot(new LockpickSlot(this.blockEntity, 0, 16, 16, this.blockEntity));  // Lockpick slot

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

    }

    private int tickCounter = 0; // Counter to track ticks for updating the button
    private int tickRate = 20 + new Random().nextInt(81); // 20 to 100 ticks (1s to 5s)

    public void tick() {

        tickCounter++;
        if (tickCounter >= tickRate) {
            tickCounter = 0;
            tickRate = 20 + new Random().nextInt(81); // New random between 20 and 100 ticks
            updateLockpickButton();
            updatechanges();
        }

        if (getLockpickProgress() > 0) {
            setLockpickProgress(clampLockpickProgress(getLockpickProgress() - 1));
            updatechanges();
        }
    }

    public void updateLockpickButton() {
        if (!level.isClientSide) {
            setShouldButtonLayerUpdate(true);
            setCorrectButtonId(new Random().nextInt(3));
            updatechanges();
        }
    }

    public void updatechanges(){
        this.broadcastChanges();   // Sync container data to client
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        int correctButton = getCorrectButtonId();
        ItemStack lockpick = getLockpickSlotItem();

        if(level.isClientSide){
            return false;
        }

        if (!lockpick.isEmpty()) {
            if (buttonId == correctButton) {
                setLockpickProgress(clampLockpickProgress(getLockpickProgress() + 50));
                level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_LOCKPICK.get(), SoundSource.BLOCKS, 1.2F, 1.0F);
                // Unlock and close for this player if at or above max progress!
                if (getLockpickProgress() >= MAX_PROGRESS) {
                    unlockStrongbox(player);
                }
            } else {
                lockpick.shrink(1);
                setLockpickProgress(0);
                level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_LOCKPICK_BREAK.get(), SoundSource.BLOCKS, 1.5F, 0.8F);
            }
            tickRate = 20 + new Random().nextInt(81); // New random between 20 and 100 ticks
            updateLockpickButton();
            updatechanges();
            return true;
        }
        return false;
    }

    public void unlockStrongbox(Player player) {
        if (player != null) {
            player.closeContainer();
        }
        this.level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_UNLOCK.get(), SoundSource.BLOCKS, 1.5f, 1.0f);
        this.level.setBlock(blockEntity.getBlockPos(), blockEntity.getBlockState().setValue(StrongboxBlock.LOCKED, false), 3);
        updatechanges();
        blockEntity.setChanged();
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
                updateLockpickButton();
                updatechanges();
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
                if(!level.isClientSide){
                    setLockpickProgress(0);
                    updatechanges();
                }
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

    private int clampLockpickProgress(int value) {
        return Math.max(0, Math.min(value, MAX_PROGRESS));
    }

    public final ContainerData data = new ContainerData() {
        private int lockpickProgress = 0;
        private int correctButtonId = 1;
        private int buttonLayerUpdate = 0; // 0 = false, 1 = true

        @Override
        public int get(int idx) {
            if (idx == 0) return lockpickProgress;
            if (idx == 1) return correctButtonId;
            if (idx == 2) return buttonLayerUpdate;
            return 0;
        }

        @Override
        public void set(int idx, int value) {
            if (idx == 0) lockpickProgress = value;
            if (idx == 1) correctButtonId = value;
            if (idx == 2) buttonLayerUpdate = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };


    public ContainerData getContainerData() {
        return data; // Just return the field!
    }

    public int  getLockpickProgress() { return data.get(0); }

    public void setLockpickProgress(int progress) { data.set(0, (int)progress); }

    public int getCorrectButtonId() { return data.get(1); }

    public void setCorrectButtonId(int id) { data.set(1, id); }

    public boolean getShouldButtonLayerUpdate() {
        return data.get(2) != 0;
    }

    public void setShouldButtonLayerUpdate(boolean value) {
        data.set(2, value ? 1 : 0);
    }


}
