package net.sievert.jolcraft.network.client.data;

public class ClientDeliriumData {
    private static int muffleTicks = 0;
    private static int prevMuffleTicks = 0;

    public static void setMuffleTicks(int ticks) {
        muffleTicks = ticks;
    }

    public static int getMuffleTicks() {
        return muffleTicks;
    }

    // This helper updates prevMuffleTicks and returns the previous value
    public static int getAndStorePreviousTicks() {
        int prev = prevMuffleTicks;
        prevMuffleTicks = muffleTicks;
        return prev;
    }

    // Tick down
    public static void tick() {
        if (muffleTicks > 0) muffleTicks--;
    }
}
