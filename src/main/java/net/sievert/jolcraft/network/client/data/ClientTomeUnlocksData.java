package net.sievert.jolcraft.network.client.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClientTomeUnlocksData {
    private static Set<String> UNLOCKS = Collections.emptySet();

    public static void setUnlocks(Set<String> unlocks) {
        UNLOCKS = Collections.unmodifiableSet(new HashSet<>(unlocks));
    }

    public static boolean hasUnlock(String id) {
        return UNLOCKS.contains(id);
    }

    public static Set<String> getAllUnlocks() {
        return UNLOCKS;
    }

    public static void clear() {
        UNLOCKS = Collections.emptySet();
    }
}
