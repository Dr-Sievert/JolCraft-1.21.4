package net.sievert.jolcraft.screen.custom.slot;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.item.JolCraftItems;

public class LockpickSlot extends Slot {

    public LockpickSlot(Container container, int index, int xPosition, int yPosition, StrongboxBlockEntity blockEntity) {
        super(container, index, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot (only lockpicks)
     */
    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow lockpicks to be placed in this slot
        return stack.is(JolCraftItems.LOCKPICK);  // Replace with the correct lockpick item reference
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64;
    }


}
