package net.sievert.jolcraft.integration.jei.custom.trade;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import javax.annotation.Nullable;

public class DwarfTradeRecipe {
    private final String profession;
    private final int level; // 1-5
    private final ItemStack inputA;
    private final ItemStack inputB;
    private final ItemStack output;
    private final DeferredItem<Item> spawnEgg;

    public DwarfTradeRecipe(String profession, int level, ItemStack inputA, @Nullable ItemStack inputB, ItemStack output, DeferredItem<Item> spawnEgg) {
        this.profession = profession;
        this.level = level;
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
        this.spawnEgg = spawnEgg;
    }

    public String getProfession() { return profession; }
    public int getLevel() { return level; }
    public ItemStack getInputA() { return inputA; }
    public @Nullable ItemStack getInputB() { return inputB; }
    public ItemStack getOutput() { return output; }
    public DeferredItem<Item> getSpawnEgg() { return spawnEgg; }
}
