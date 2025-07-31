package net.sievert.jolcraft.screen.custom.slot;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.item.JolCraftItems;

public class LockpickSlot extends Slot {
    public LockpickSlot(Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow lockpicks
        return stack.is(JolCraftItems.LOCKPICK);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64; // or 1, if you want only 1 lockpick per attempt
    }
}
