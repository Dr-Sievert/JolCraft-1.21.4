package net.sievert.jolcraft.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.integration.jei.custom.DwarfTradeCategory;
import net.sievert.jolcraft.util.dwarf.DwarfTradeJeiHelper;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JolCraftJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("jolcraft", "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DwarfTradeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DwarfTradeCategory.RECIPE_TYPE, DwarfTradeJeiHelper.getAllDwarfJeiTrades());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (var prof : DwarfTradeJeiHelper.PROFESSIONS) {
            registration.addCraftingStation(
                    DwarfTradeCategory.RECIPE_TYPE,
                    prof.spawnEgg().get()
            );
        }
    }



}
