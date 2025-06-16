package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.concurrent.CompletableFuture;

public class JolCraftRecipeProvider extends RecipeProvider {
    public JolCraftRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new JolCraftRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "JolCraft Recipes";
        }
    }


    @Override
    protected void buildRecipes() {

        shaped(RecipeCategory.MISC, JolCraftItems.CONTRACT_BLANK.get())
                .pattern("BB")
                .pattern("BB")
                .pattern("BB")
                .define('B', Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER)).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.CONTRACT_WRITTEN.get())
                .requires(JolCraftItems.CONTRACT_BLANK.get())
                .requires(JolCraftTags.Items.INK_AND_QUILLS)
                .unlockedBy("has_contract_blank", has(JolCraftItems.CONTRACT_BLANK.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_FULL.get())
                .requires(Items.GLASS)
                .requires(Items.FEATHER)
                .requires(Items.INK_SAC)
                .unlockedBy("has_ink", has(Items.INK_SAC))
                .save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_FULL.get())
                .requires(JolCraftItems.QUILL_EMPTY.get())
                .requires(Items.INK_SAC)
                .unlockedBy("has_quill_empty", has(JolCraftItems.QUILL_EMPTY.get()))
                .save(output, "quill_full_refill");

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_EMPTY.get())
                .requires(Items.GLASS)
                .requires(Items.FEATHER)
                .unlockedBy("has_feather", has(Items.FEATHER))
                .save(output);





    }


}
