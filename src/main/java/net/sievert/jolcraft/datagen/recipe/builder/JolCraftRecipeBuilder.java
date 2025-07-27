package net.sievert.jolcraft.datagen.recipe.builder;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import javax.annotation.Nullable;

public class JolCraftRecipeBuilder implements RecipeBuilder {
    private final RecipeBuilder inner;
    private final String modId;

    // Constructors for both shaped and shapeless
    public JolCraftRecipeBuilder(ShapedRecipeBuilder shaped, String modId) {
        this.inner = shaped;
        this.modId = modId;
    }

    public JolCraftRecipeBuilder(ShapelessRecipeBuilder shapeless, String modId) {
        this.inner = shapeless;
        this.modId = modId;
    }

    // === DSL METHODS (only common ones) ===

    public JolCraftRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        if (inner instanceof ShapedRecipeBuilder s) s.unlockedBy(name, criterion);
        else if (inner instanceof ShapelessRecipeBuilder s) s.unlockedBy(name, criterion);
        return this;
    }

    public JolCraftRecipeBuilder group(@Nullable String group) {
        if (inner instanceof ShapedRecipeBuilder s) s.group(group);
        else if (inner instanceof ShapelessRecipeBuilder s) s.group(group);
        return this;
    }

    public JolCraftRecipeBuilder pattern(String pattern) {
        if (inner instanceof ShapedRecipeBuilder s) s.pattern(pattern);
        return this;
    }

    public JolCraftRecipeBuilder define(char symbol, ItemLike item) {
        if (inner instanceof ShapedRecipeBuilder s) s.define(symbol, item);
        return this;
    }

    public JolCraftRecipeBuilder define(char symbol, TagKey<Item> tag) {
        if (inner instanceof ShapedRecipeBuilder s) s.define(symbol, tag);
        return this;
    }

    public JolCraftRecipeBuilder requires(ItemLike item) {
        if (inner instanceof ShapelessRecipeBuilder s) s.requires(item);
        return this;
    }

    public JolCraftRecipeBuilder requires(ItemLike item, int count) {
        if (inner instanceof ShapelessRecipeBuilder s) s.requires(item, count);
        return this;
    }

    public JolCraftRecipeBuilder requires(TagKey<Item> tag) {
        if (inner instanceof ShapelessRecipeBuilder s) s.requires(tag);
        return this;
    }


    // === Base interface ===

    @Override
    public Item getResult() {
        return inner.getResult();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        ResourceLocation loc = resourceKey.location();
        ResourceLocation fixedLoc = ResourceLocation.fromNamespaceAndPath(modId, loc.getPath());
        ResourceKey<Recipe<?>> fixedKey = ResourceKey.create(Registries.RECIPE, fixedLoc);
        inner.save(output, fixedKey);
    }

    @Override
    public void save(RecipeOutput output) {
        ResourceLocation defaultLoc = BuiltInRegistries.ITEM.getKey(inner.getResult());
        ResourceLocation fixedLoc = ResourceLocation.fromNamespaceAndPath(modId, defaultLoc.getPath());
        ResourceKey<Recipe<?>> fixedKey = ResourceKey.create(Registries.RECIPE, fixedLoc);
        inner.save(output, fixedKey);
    }
}
