package net.sievert.jolcraft.client.data;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class MyClientReputationData {

    // Track endorsement/animation state per entity (by their UUID or entityId)
    private static final Map<Integer, Boolean> endorsementAnimationStates = new HashMap<>();
    private static int tier = 0;
    private static Set<ResourceLocation> CLIENT_ENDORSEMENTS = Collections.emptySet();

    public static void setTier(int newTier) {
        tier = newTier;
        System.out.println("[CLIENT] Synced reputation tier: " + newTier);
    }

    public static int getTier() {
        return tier;
    }

    public static void setEndorsementAnimation(int entityId, boolean running) {
        endorsementAnimationStates.put(entityId, running);
        System.out.println("[CLIENT] Endorsement animation for entity " + entityId + " set to: " + running);
    }

    public static boolean isEndorsementAnimationActive(int entityId) {
        return endorsementAnimationStates.getOrDefault(entityId, false);
    }

    public static void setEndorsements(Set<ResourceLocation> endorsements) {
        CLIENT_ENDORSEMENTS = Collections.unmodifiableSet(new HashSet<>(endorsements));
        System.out.println("[CLIENT] Updated endorsement set: " + CLIENT_ENDORSEMENTS);
    }

    public static boolean hasEndorsement(ResourceLocation profId) {
        return CLIENT_ENDORSEMENTS.contains(profId);
    }

    public static int endorsementCount() {
        return CLIENT_ENDORSEMENTS.size();
    }

    public static Set<ResourceLocation> getAllEndorsements() {
        return CLIENT_ENDORSEMENTS;
    }

    public static void clear() {
        endorsementAnimationStates.clear();
        CLIENT_ENDORSEMENTS = Collections.emptySet();
        System.out.println("[CLIENT] Cleared all client reputation animation state.");
    }
}
