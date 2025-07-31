package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sievert.jolcraft.block.custom.StrongboxBlock;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.effect.JolCraftEffects;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.slot.LockpickSlot;
import net.sievert.jolcraft.sound.JolCraftSounds;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class LockMenu extends AbstractContainerMenu {
    public final StrongboxBlockEntity blockEntity;
    private final Level level;
    private final Random random = new Random();  // Random instance for sprite selection
    final int MAX_PROGRESS = 130;
    private int decayCounter = 0;

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

        // Add the lockpick slot
        SimpleContainer lockpickContainer = new SimpleContainer(1);
        this.addSlot(new LockpickSlot(lockpickContainer, 0, 16, 16));

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
    private int tickRate = 40 + new Random().nextInt(61); // 40 to 100 ticks (2s to 5s)

    public void tick() {
        Player player = blockEntity.currentInteractingPlayer;

        if (!level.isClientSide) {
            assert player != null;
            var effect = player.getEffect(JolCraftEffects.LOCKPICKING);
            if (effect != null) {
                // Player has the effect, so use the boosted values
                int decayTicks = 2 + effect.getAmplifier();
                int progressBoost = 10 + (effect.getAmplifier() * 10);
                setDecayTicks(decayTicks);
                setProgressBoost(progressBoost);
            } else {
                // No effect: use defaults
                setDecayTicks(1);       // Decay every tick
                setProgressBoost(0);    // No bonus
            }
            broadcastChanges();
        }

        tickCounter++;

        if (tickCounter >= tickRate && isActive()) {
            tickCounter = 0;
            updateLockpickButton();
        }

        if (!level.isClientSide) {
            if (getLockpickProgress() > 0) {
                decayCounter++;
                int interval = getDecayTicks(); // <-- This gets your container data's decayTicks, synced from your effect logic
                if (decayCounter >= interval) {
                    decayCounter = 0;
                    setLockpickProgress(clampLockpickProgress(getLockpickProgress() - 1));
                    updatechanges();
                }
            } else {
                decayCounter = 0; // Reset if bar is empty
            }
        }
    }

    public void updateLockpickButton() {
        if (!level.isClientSide) {
            int decay = Math.max(1, getDecayTicks());
            if (random.nextInt(101/decay) == 0) {
                setCorrectButtonId(3);                    // Enter unlock mode
                setUnlockSlotId(random.nextInt(3));       // Pick which button is unlock (0, 1, or 2)
            } else {
                setCorrectButtonId(random.nextInt(3));    // Pick normal correct button
                setUnlockSlotId(-1);                      // No unlock button this round
            }
            setButtonLayerUpdatePulse((getButtonLayerUpdatePulse() % 3) + 1); // cycles 1-3
            updatechanges();
        }
        tickCounter = 0;
        tickRate = 40 + new Random().nextInt(61); // 40 to 100 ticks (2s to 5s)

    }


    public void updatechanges(){
        this.broadcastChanges();   // Sync container data to client
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int buttonId) {
        int correctButton = getCorrectButtonId();
        int unlockSlot = getUnlockSlotId();
        ItemStack lockpick = getLockpickSlotItem();

        if (!lockpick.isEmpty()) {
            // --- Unlock mode ---
            if (correctButton == 3 && buttonId == unlockSlot) {
                if (!level.isClientSide) {
                    unlockStrongbox(player);
                }
                level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_UNLOCK.get(), SoundSource.BLOCKS, 1.5F, 1.2F);
                updateLockpickButton();
                return true;
            }
            // --- Normal mode ---
            if (buttonId == correctButton) {
                if (!level.isClientSide) {
                    setLockpickProgress(
                            clampLockpickProgress(getLockpickProgress() + 10 + random.nextInt(11) + getProgressBoost())
                    );
                    updatechanges();
                }
                level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_LOCKPICK.get(), SoundSource.BLOCKS, 1.2F, 1.0F);
                // Unlock and close for this player if at or above max progress!
                if (getLockpickProgress() >= MAX_PROGRESS) {
                    unlockStrongbox(player);
                }
            } else {
                if (!level.isClientSide) {
                    if(!player.isCreative()){
                        lockpick.shrink(1);
                    }
                    setLockpickProgress(0);
                    updatechanges();
                }
                level.playSound(null, blockEntity.getBlockPos(), JolCraftSounds.STRONGBOX_LOCKPICK_BREAK.get(), SoundSource.BLOCKS, 1.5F, 0.8F);
            }
            updateLockpickButton();
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
        blockEntity.setChanged();
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        int lockSize = 1;
        int invEnd = lockSize + 27;
        int hotbarEnd = invEnd + 9;

        // Shift-click from Lock to Player
        if (index < lockSize) {
            if (!moveItemStackTo(stack, lockSize, hotbarEnd, true)) return ItemStack.EMPTY;
        } else {
            // Shift-click from Player to Lock
            if (!moveItemStackTo(stack, 0, lockSize, false)) return ItemStack.EMPTY;
        }

        // Handle placing the lockpick into the lock slot (normal or shift-click)
        if (index == 0) {
            this.setCarried(stack);  // Set the carried item as the lockpick when placed in the lock slot
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    // Check if the menu is still valid (if the Strongbox is still there)
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public void removed(@NotNull Player player) {
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
        Slot lockpickSlot = this.slots.getFirst();  // Lockpick slot
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
        private int correctButtonId = 0;
        private int buttonLayerUpdatePulse = 0; // The "pulse" counter
        private int decayTicks = 1;             // How many ticks between decay
        private int progressBoost = 0;          // Bonus progress per click
        private int unlockSlotId = -1;          // Which button is the "unlock" slot, -1 if not active

        @Override
        public int get(int idx) {
            return switch (idx) {
                case 0 -> lockpickProgress;
                case 1 -> correctButtonId;
                case 2 -> buttonLayerUpdatePulse;
                case 3 -> decayTicks;
                case 4 -> progressBoost;
                case 5 -> unlockSlotId;
                default -> 0;
            };
        }

        @Override
        public void set(int idx, int value) {
            switch (idx) {
                case 0 -> lockpickProgress = value;
                case 1 -> correctButtonId = value;
                case 2 -> buttonLayerUpdatePulse = value;
                case 3 -> decayTicks = value;
                case 4 -> progressBoost = value;
                case 5 -> unlockSlotId = value;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };


    public ContainerData getContainerData() {
        return data; // Just return the field!
    }

    public int  getLockpickProgress() { return data.get(0); }
    public void setLockpickProgress(int progress) { data.set(0, (int)progress); }

    public int getCorrectButtonId() { return data.get(1); }
    public void setCorrectButtonId(int id) { data.set(1, id); }

    public int getButtonLayerUpdatePulse() { return data.get(2); }
    public void setButtonLayerUpdatePulse(int value) { data.set(2, value); }

    public int getDecayTicks() { return data.get(3); }
    public void setDecayTicks(int value) { data.set(3, value); }

    public int getProgressBoost() { return data.get(4); }
    public void setProgressBoost(int value) { data.set(4, value); }

    public int getUnlockSlotId() { return data.get(5); }
    public void setUnlockSlotId(int slot) { data.set(5, slot); }
}
