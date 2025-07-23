package net.sievert.jolcraft.util.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.data.custom.unlock.TomeUnlock;
import net.sievert.jolcraft.network.client.data.ClientTomeUnlocksData;

import java.util.Set;

public class TomeUnlockHelper {

    // --- Known Unlock IDs ---
    public static final String BREW_MULTIPLE_HOPS = "forgotten_brew_formulas";
    public static final String CUTTING_GEMS = "ancient_gemcraft";

    // --- SERVER SIDE ---

    // Creative OR unlock
    public static boolean hasUnlockServer(Player player, String unlockId) {
        return player != null && (player.isCreative() || TomeUnlock.get(player).hasUnlock(unlockId));
    }

    // Only unlock (NOT creative)
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

    // --- CLIENT SIDE ---

    @OnlyIn(Dist.CLIENT)
    public static boolean hasUnlockClient(String unlockId) {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.isCreative() || ClientTomeUnlocksData.hasUnlock(unlockId));
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean hasUnlockClientBypassCreative(String unlockId) {
        Player player = Minecraft.getInstance().player;
        return player != null && ClientTomeUnlocksData.hasUnlock(unlockId);
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<String> getAllUnlocksClient() {
        return ClientTomeUnlocksData.getAllUnlocks();
    }
}
