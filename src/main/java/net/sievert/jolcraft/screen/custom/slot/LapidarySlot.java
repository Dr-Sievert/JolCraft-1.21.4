package net.sievert.jolcraft.screen.custom.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.data.JolCraftTags;

public class LapidarySlot extends Slot {
    public LapidarySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(JolCraftTags.Items.GEODES) || stack.is(JolCraftTags.Items.GEMS_UNCUT);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64;
    }
}
