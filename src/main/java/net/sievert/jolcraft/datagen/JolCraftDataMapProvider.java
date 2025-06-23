package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.concurrent.CompletableFuture;

public class JolCraftDataMapProvider extends DataMapProvider {
    protected JolCraftDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(JolCraftItems.BARLEY_SEEDS.getId(), new Compostable(0.25f), false)
                .add(JolCraftItems.BARLEY.getId(), new Compostable(0.45f), false);
    }
}