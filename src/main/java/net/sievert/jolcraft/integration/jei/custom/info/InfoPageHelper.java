package net.sievert.jolcraft.integration.jei.custom.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.List;

public class InfoPageHelper {
    public static List<InfoPageRecipe> getAllInfoPages() {
        return List.of(
                new InfoPageRecipe(
                        new ItemStack(JolCraftItems.STRONGBOX_ITEM.get()),
                        Component.translatable("jei.jolcraft.info_page.strongbox")
                )
        );
    }
}
