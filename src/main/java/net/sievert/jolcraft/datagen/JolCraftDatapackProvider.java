package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.item.JolCraftTrimMaterials;
import net.sievert.jolcraft.item.JolCraftTrimPatterns;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class JolCraftDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.TRIM_MATERIAL, JolCraftTrimMaterials::bootstrap)
            .add(Registries.TRIM_PATTERN, JolCraftTrimPatterns::bootstrap);

    public JolCraftDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(JolCraft.MOD_ID));
    }
}
