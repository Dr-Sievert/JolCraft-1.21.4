package net.sievert.jolcraft.util;

import java.util.Random;
import net.minecraft.util.RandomSource;

public class RandomAdapter extends Random {
    private final RandomSource source;

    public RandomAdapter(RandomSource source) {
        this.source = source;
    }

    @Override
    public int nextInt() {
        return source.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return source.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return source.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return source.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return source.nextFloat();
    }

    @Override
    public double nextDouble() {
        return source.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return source.nextGaussian();
    }
}
