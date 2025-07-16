package net.sievert.jolcraft.worldgen.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.sievert.jolcraft.worldgen.JolCraftBlockPredicateTypes;

public class DarknessPredicate implements BlockPredicate {
    public static final MapCodec<DarknessPredicate> CODEC =
            Codec.INT.fieldOf("max_brightness")
                    .xmap(DarknessPredicate::new, p -> p.maxBrightness);

    private final int maxBrightness;
    public DarknessPredicate(int maxBrightness) { this.maxBrightness = maxBrightness; }

    @Override
    public boolean test(WorldGenLevel level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) <= maxBrightness;
    }

    @Override
    public BlockPredicateType<?> type() {
        return JolCraftBlockPredicateTypes.DARKNESS.value();
    }
}
