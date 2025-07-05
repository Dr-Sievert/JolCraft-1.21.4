package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.util.JolCraftTags;
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

        shaped(RecipeCategory.MISC, JolCraftBlocks.MUFFHORN_FUR_BLOCK.get())
                .pattern("BB")
                .pattern("BB")
                .define('B', JolCraftItems.MUFFHORN_FUR.get())
                .unlockedBy("has_muffhorn_fur", has(JolCraftItems.MUFFHORN_FUR.get())).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.MUFFHORN_FUR.get(), 4)
                .requires(JolCraftBlocks.MUFFHORN_FUR_BLOCK.get())
                .unlockedBy("has_muffhorn_fur", has(JolCraftItems.MUFFHORN_FUR.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.PARCHMENT.get())
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .define('B', Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER)).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.CONTRACT_BLANK.get())
                .pattern("BB")
                .pattern("BB")
                .pattern("BB")
                .define('B', Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER)).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.CONTRACT_WRITTEN.get())
                .requires(JolCraftItems.CONTRACT_BLANK.get())
                .requires(JolCraftTags.Items.INK_AND_QUILLS)
                .unlockedBy("has_contract_blank", has(JolCraftItems.CONTRACT_BLANK.get())).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.CONTRACT_GUILDMASTER.get())
                .requires(JolCraftItems.GUILD_SIGIL.get())
                .requires(JolCraftItems.CONTRACT_SIGNED.get())
                .unlockedBy("has_contract_signed", has(JolCraftItems.CONTRACT_SIGNED.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.GLASS_MUG.get())
                .pattern("B ")
                .pattern("BB")
                .pattern("B ")
                .define('B', Items.GLASS)
                .unlockedBy("has_glass", has(Items.GLASS)).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_FULL.get())
                .requires(Items.GLASS)
                .requires(Items.FEATHER)
                .requires(Items.INK_SAC)
                .unlockedBy("has_ink", has(Items.INK_SAC)).save(output);

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_FULL.get())
                .requires(JolCraftItems.QUILL_EMPTY.get())
                .requires(Items.INK_SAC)
                .unlockedBy("has_quill_empty", has(JolCraftItems.QUILL_EMPTY.get())).save(output, "quill_full_refill");

        shapeless(RecipeCategory.MISC, JolCraftItems.QUILL_EMPTY.get())
                .requires(Items.GLASS)
                .requires(Items.FEATHER)
                .unlockedBy("has_feather", has(Items.FEATHER)).save(output);

        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                JolCraftItems.SCRAP.get(), //Ingredient
                RecipeCategory.MISC,
                JolCraftItems.SCRAP_HEAP.get() //Block
        );

        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                JolCraftItems.BARLEY.get(), //Ingredient
                RecipeCategory.MISC,
                JolCraftBlocks.BARLEY_BLOCK.get() //Block
        );

        shapeless(RecipeCategory.MISC, JolCraftItems.VERDANT_DUST.get())
                .requires(JolCraftItems.VERDANITE.get())
                .unlockedBy("has_verdanite", has(JolCraftItems.VERDANITE.get())).save(output);

        shapeless(RecipeCategory.MISC, JolCraftBlocks.VERDANT_SOIL.get(), 1)
                .requires(Blocks.MUD)
                .requires(JolCraftItems.VERDANT_DUST.get())
                .unlockedBy("has_verdant_dust", has(JolCraftItems.VERDANT_DUST.get())).save(output);

        // Barley -> Barley Malt (Smelting)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(JolCraftItems.BARLEY.get()),
                        RecipeCategory.FOOD,
                        JolCraftItems.BARLEY_MALT.get(),
                        0.35f, // XP
                        200    // Cooking time (in ticks, vanilla is 200 = 10s)
                ).unlockedBy("has_barley", has(JolCraftItems.BARLEY.get())).save(output, "barley_malt_from_smelting");

        // Barley -> Barley Malt (Smoking)
        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(JolCraftItems.BARLEY.get()),
                        RecipeCategory.FOOD,
                        JolCraftItems.BARLEY_MALT.get(),
                        0.35f, // XP
                        100    // Cooking time (smoking is usually faster, vanilla is 100 = 5s)
                ).unlockedBy("has_barley", has(JolCraftItems.BARLEY.get())).save(output, "barley_malt_from_smoking");


        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_PLATE.get())
                .pattern("BXB")
                .define('B', Items.IRON_INGOT)
                .define('X', JolCraftItems.DEEPSLATE_BULBS.get())
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_BULBS.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_SWORD.get())
                .pattern("B")
                .pattern("B")
                .pattern("X")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_PICKAXE.get())
                .pattern("BBB")
                .pattern(" X ")
                .pattern(" X ")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_SHOVEL.get())
                .pattern("B")
                .pattern("X")
                .pattern("X")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_AXE.get())
                .pattern("BB")
                .pattern("BX")
                .pattern(" X")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output, "deepslate_axe_left");

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_AXE.get())
                .pattern("BB")
                .pattern("XB")
                .pattern("X ")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output, "deepslate_axe_right");

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_HOE.get())
                .pattern("BB")
                .pattern(" X")
                .pattern(" X")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output, "deepslate_hoe_left");

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_HOE.get())
                .pattern("BB")
                .pattern("X ")
                .pattern("X ")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_deepslate_bulbs", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output, "deepslate_hoe_right");

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_HELMET.get())
                .pattern("BBB")
                .pattern("B B")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .unlockedBy("has_deepslate_plate", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_CHESTPLATE.get())
                .pattern("B B")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .unlockedBy("has_deepslate_plate", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_LEGGINGS.get())
                .pattern("BBB")
                .pattern("B B")
                .pattern("B B")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .unlockedBy("has_deepslate_plate", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shaped(RecipeCategory.MISC, JolCraftItems.DEEPSLATE_BOOTS.get())
                .pattern("B B")
                .pattern("B B")
                .define('B', JolCraftItems.DEEPSLATE_PLATE.get())
                .unlockedBy("has_deepslate_plate", has(JolCraftItems.DEEPSLATE_PLATE.get())).save(output);

        shapeless(RecipeCategory.BREWING, JolCraftItems.INVERIX.get(), 3)
                .requires(JolCraftItems.MUFFHORN_MILK_BUCKET.get())
                .requires(Items.COAL)
                .unlockedBy("has_muffhorn_milk_bucket", has(JolCraftItems.MUFFHORN_MILK_BUCKET.get())).save(output, "inverix_coal");

        shapeless(RecipeCategory.BREWING, JolCraftItems.INVERIX.get(), 3)
                .requires(JolCraftItems.MUFFHORN_MILK_BUCKET.get())
                .requires(Items.CHARCOAL)
                .unlockedBy("has_muffhorn_milk_bucket", has(JolCraftItems.MUFFHORN_MILK_BUCKET.get())).save(output, "inverix_charcoal");

        trimSmithing(JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "smithing_trim_forge"))
        );

        shaped(RecipeCategory.MISC, JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), 2)
                .pattern("BXB")
                .pattern("BAB")
                .pattern("BBB")
                .define('B', Items.DIAMOND)
                .define('X', JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE.get())
                .define('A', JolCraftItems.DEEPSLATE_PLATE.get())
                .unlockedBy("has_forge_armor_trim_smithing_template", has(JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE.get())).save(output);


    }


}
