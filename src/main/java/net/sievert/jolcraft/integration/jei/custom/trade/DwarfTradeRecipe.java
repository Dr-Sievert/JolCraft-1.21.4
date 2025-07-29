package net.sievert.jolcraft.integration.jei.custom.trade;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import javax.annotation.Nullable;

/**
 * Represents a single Dwarf trade entry for JEI, including support for input/output count ranges.
 */
public class DwarfTradeRecipe {
    private final String profession;
    private final int level; // 1-5
    private final ItemStack inputA;
    private final ItemStack inputB;
    private final ItemStack output;
    private final DeferredItem<Item> spawnEgg;

    // Count ranges for JEI display
    private final int inputAMin;
    private final int inputAMax;
    private final int inputBMin;
    private final int inputBMax;
    private final int outputMin;
    private final int outputMax;

    public DwarfTradeRecipe(
            String profession,
            int level,
            ItemStack inputA,
            @Nullable ItemStack inputB,
            ItemStack output,
            DeferredItem<Item> spawnEgg,
            int inputAMin, int inputAMax,
            int inputBMin, int inputBMax,
            int outputMin, int outputMax
    ) {
        this.profession = profession;
        this.level = level;
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
        this.spawnEgg = spawnEgg;
        this.inputAMin = inputAMin;
        this.inputAMax = inputAMax;
        this.inputBMin = inputBMin;
        this.inputBMax = inputBMax;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
    }

    public String getProfession() { return profession; }
    public int getLevel() { return level; }
    public ItemStack getInputA() { return inputA; }
    public @Nullable ItemStack getInputB() { return inputB; }
    public ItemStack getOutput() { return output; }
    public DeferredItem<Item> getSpawnEgg() { return spawnEgg; }

    // Count/range accessors
    public int getInputAMin() { return inputAMin; }
    public int getInputAMax() { return inputAMax; }
    public int getInputBMin() { return inputBMin; }
    public int getInputBMax() { return inputBMax; }
    public int getOutputMin() { return outputMin; }
    public int getOutputMax() { return outputMax; }
}
