package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;

public class StrongboxMenu extends AbstractContainerMenu {
    public final StrongboxBlockEntity blockEntity;
    private final Level level;

    // Used by NeoForge's auto-gui opening
    public StrongboxMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public StrongboxBlockEntity getBlockEntity() {
        return blockEntity;
    }

    // Main constructor
    public StrongboxMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(JolCraftMenuTypes.STRONGBOX_MENU.get(), id);
        this.blockEntity = (StrongboxBlockEntity) blockEntity;
        this.level = inv.player.level();

        // Strongbox slots
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(this.blockEntity, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 68 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 126));
        }
    }

    // --- Shift-click logic ---
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        int strongboxSize = 18;
        int invStart = strongboxSize;
        int invEnd = invStart + 27;
        int hotbarStart = invEnd;
        int hotbarEnd = hotbarStart + 9;

        // Shift-click from Strongbox to Player
        if (index < strongboxSize) {
            if (!moveItemStackTo(stack, invStart, hotbarEnd, true)) return ItemStack.EMPTY;
        } else {
            // Shift-click from Player to Strongbox
            if (!moveItemStackTo(stack, 0, strongboxSize, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
    }




}
