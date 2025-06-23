package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.sievert.jolcraft.JolCraft;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class JolCraftDataGenerator {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(true , new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(JolCraftBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookup));

        BlockTagsProvider blockTagsProvider = new JolCraftBlockTagProvider(packOutput, lookup);
        generator.addProvider(true, blockTagsProvider);

        generator.addProvider(true, new JolCraftDataMapProvider(packOutput, lookup));

        generator.addProvider(true, new JolCraftRecipeProvider.Runner(packOutput, lookup));

        generator.addProvider(true, new JolCraftModelProvider(packOutput));

        generator.addProvider(true, new JolCraftItemTagProvider(packOutput, lookup, blockTagsProvider.contentsGetter()));

        generator.addProvider(true, new AdvancementProvider(
                packOutput, lookup, List.of(new JolCraftAdvancementProvider())
        ));
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(true , new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(JolCraftBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookup));

        BlockTagsProvider blockTagsProvider = new JolCraftBlockTagProvider(packOutput, lookup);
        generator.addProvider(true, blockTagsProvider);

        generator.addProvider(true, new JolCraftDataMapProvider(packOutput, lookup));

        generator.addProvider(true, new JolCraftRecipeProvider.Runner(packOutput, lookup));

        generator.addProvider(true, new JolCraftModelProvider(packOutput));

        generator.addProvider(true, new JolCraftItemTagProvider(packOutput, lookup, blockTagsProvider.contentsGetter()));

        generator.addProvider(true, new AdvancementProvider(
                packOutput, lookup, List.of(new JolCraftAdvancementProvider())
        ));
    }
}
