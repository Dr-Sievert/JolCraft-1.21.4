package net.sievert.jolcraft.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.BarleyCropBlock;
import net.sievert.jolcraft.block.custom.HopsCropBottomBlock;
import net.sievert.jolcraft.block.custom.HopsCropTopBlock;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.Set;

public class JolCraftBlockLootTableProvider extends BlockLootSubProvider {
    protected JolCraftBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        dropSelf(JolCraftBlocks.BARLEY_BLOCK.get());
        dropSelf(JolCraftBlocks.MUFFHORN_FUR_BLOCK.get());

        this.add(JolCraftBlocks.BARLEY_CROP.get(),
                createCropDrops(
                        JolCraftBlocks.BARLEY_CROP.get(),
                        JolCraftItems.BARLEY.get(),
                        JolCraftItems.BARLEY_SEEDS.get(),
                        BarleyCropBlock.AGE,
                        7
                )
        );

        this.add(JolCraftBlocks.FERMENTING_CAULDRON.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(Blocks.CAULDRON))
                        )
        );


        addHopsCropDrops(
                JolCraftBlocks.ASGARNIAN_CROP_BOTTOM.get(),
                JolCraftBlocks.ASGARNIAN_CROP_TOP.get(),
                JolCraftItems.ASGARNIAN_SEEDS.get(),
                JolCraftItems.ASGARNIAN_HOPS.get(),
                HopsCropBottomBlock.AGE,
                HopsCropBottomBlock.MAX_AGE,
                HopsCropTopBlock.TOP_AGE,
                4
        );

        addHopsCropDrops(
                JolCraftBlocks.DUSKHOLD_CROP_BOTTOM.get(),
                JolCraftBlocks.DUSKHOLD_CROP_TOP.get(),
                JolCraftItems.DUSKHOLD_SEEDS.get(),
                JolCraftItems.DUSKHOLD_HOPS.get(),
                HopsCropBottomBlock.AGE,
                HopsCropBottomBlock.MAX_AGE,
                HopsCropTopBlock.TOP_AGE,
                4
        );

        addHopsCropDrops(
                JolCraftBlocks.KRANDONIAN_CROP_BOTTOM.get(),
                JolCraftBlocks.KRANDONIAN_CROP_TOP.get(),
                JolCraftItems.KRANDONIAN_SEEDS.get(),
                JolCraftItems.KRANDONIAN_HOPS.get(),
                HopsCropBottomBlock.AGE,
                HopsCropBottomBlock.MAX_AGE,
                HopsCropTopBlock.TOP_AGE,
                4
        );

        addHopsCropDrops(
                JolCraftBlocks.YANILLIAN_CROP_BOTTOM.get(),
                JolCraftBlocks.YANILLIAN_CROP_TOP.get(),
                JolCraftItems.YANILLIAN_SEEDS.get(),
                JolCraftItems.YANILLIAN_HOPS.get(),
                HopsCropBottomBlock.AGE,
                HopsCropBottomBlock.MAX_AGE,
                HopsCropTopBlock.TOP_AGE,
                4
        );

    }

    protected LootTable.Builder createCropDrops(Block cropBlock, Item cropItem, Item seedItem, IntegerProperty ageProperty, int maxAge) {
        LootItemCondition.Builder mature = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(cropBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));
        return createCropDrops(cropBlock, cropItem, seedItem, mature);
    }


    private void addHopsCropDrops(
            Block bottom,
            Block top,
            Item seed,
            Item hops,
            IntegerProperty bottomAge,
            int bottomMaxAge,
            IntegerProperty topAge,
            int topMaxAge
    ) {
        // --- Bottom Block: always drops 1 seed, +1 extra seed at max age (20% chance)
        LootItemCondition.Builder isMature = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(bottom)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(bottomAge, bottomMaxAge));

        this.add(bottom,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(seed))
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(seed)
                                        .when(isMature)
                                        .when(LootItemRandomChanceCondition.randomChance(0.2F))
                                )
                        )
        );

        // --- Top Block: only drops hops if at max visual age
        LootItemCondition.Builder topAgeMax = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(top)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(topAge, topMaxAge));

        this.add(top,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(hops).when(topAgeMax))
                        )
        );
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return JolCraftBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
