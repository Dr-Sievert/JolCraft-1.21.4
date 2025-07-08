package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ButtonSlot extends Slot {
    private final LockMenu menu;
    public final int buttonIndex; // Identifies the button action

    public ButtonSlot(LockMenu menu, int buttonIndex, int x, int y) {
        super(menu.getBlockEntity(), buttonIndex, x, y);
        this.menu = menu;
        this.buttonIndex = buttonIndex;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false; // This slot is just for button click, no items can be placed
    }

    @Override
    public boolean isActive() {
        return true; // Always active for clicks
    }
}
