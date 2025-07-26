package net.sievert.jolcraft.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.integration.jei.custom.trade.DwarfTradeCategory;
import net.sievert.jolcraft.integration.jei.custom.info.InfoPageCategory;
import net.sievert.jolcraft.integration.jei.custom.trade.DwarfTradeJeiHelper;
import net.sievert.jolcraft.integration.jei.custom.info.InfoPageHelper;
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
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new DwarfTradeCategory(guiHelper),
                new InfoPageCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DwarfTradeCategory.RECIPE_TYPE, DwarfTradeJeiHelper.getAllDwarfJeiTrades());
        registration.addRecipes(InfoPageCategory.RECIPE_TYPE, InfoPageHelper.getAllInfoPages());
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
