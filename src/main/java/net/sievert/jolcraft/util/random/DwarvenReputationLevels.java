package net.sievert.jolcraft.util.random;

import java.util.HashMap;
import java.util.Map;

public class DwarvenReputationLevels {

    private static final int[] ENDORSEMENT_THRESHOLDS = {2, 5, 9, 14};

    public static int getThresholdForTier(int tier) {
        return (tier >= 0 && tier < ENDORSEMENT_THRESHOLDS.length)
                ? ENDORSEMENT_THRESHOLDS[tier]
                : Integer.MAX_VALUE; // Already at final tier
    }

    public static boolean canAdvance(int currentTier, int endorsements) {
        return currentTier < ENDORSEMENT_THRESHOLDS.length
                && endorsements >= getThresholdForTier(currentTier);
    }

    public static int getMaxTier() {
        return ENDORSEMENT_THRESHOLDS.length;
    }

    public static String getTierName(int tier) {
        return switch (tier) {
            case 0 -> "Stranger";
            case 1 -> "Known Face";
            case 2 -> "Trusted";
            case 3 -> "Respected";
            case 4 -> "Blood-Kin";
            default -> "Unknown";
        };
    }

    public static Map<Integer, String> getAllTierNames() {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i <= getMaxTier(); i++) {
            map.put(i, getTierName(i));
        }
        return map;
    }
}

