package net.sievert.jolcraft.integration.jei.custom.info;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class InfoPageRecipe {
    private final ItemStack focusStack;
    private final Component content;

    public InfoPageRecipe(ItemStack focusStack, Component content) {
        this.focusStack = focusStack;
        this.content = content;
    }

    public ItemStack getFocusStack() {
        return focusStack;
    }
    public Component getContent() {
        return content;
    }
}
