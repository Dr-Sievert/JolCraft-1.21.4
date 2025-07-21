package net.sievert.jolcraft.util.attachment;

import net.minecraft.world.entity.player.Player;
import net.sievert.jolcraft.data.custom.unlock.TomeUnlock;

import java.util.Set;

public class TomeUnlockHelper {

    // --- Known Unlock IDs ---
    public static final String BREW_MULTIPLE_HOPS = "forgotten_brew_formulas";
    public static final String CUTTING_GEMS = "ancient_gemcraft";

    // --- SERVER SIDE ---

    public static boolean hasUnlockServer(Player player, String unlockId) {
        return player != null && (player.isCreative() || TomeUnlock.get(player).hasUnlock(unlockId));
    }

    public static boolean hasUnlockServerBypassCreative(Player player, String unlockId) {
        return player != null && TomeUnlock.get(player).hasUnlock(unlockId);
    }

    public static void grantUnlock(Player player, String unlockId) {
        if (player != null) {
            TomeUnlock.get(player).addUnlock(unlockId);
        }
    }

    public static Set<String> getAllUnlocksServer(Player player) {
        return player != null ? TomeUnlock.get(player).getUnlocks() : Set.of();
    }
}
