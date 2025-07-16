package net.sievert.jolcraft.util.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.sievert.jolcraft.data.custom.lang.DwarvenLanguage;
import net.sievert.jolcraft.network.client.data.MyClientLanguageData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DwarvenLanguageHelper {

    /**
     * SERVER-SIDE: Checks if the player knows Dwarvish or is in creative mode.
     */

    public static boolean knowsDwarvishServer(Player player) {
        return player != null && (player.isCreative() || DwarvenLanguage.get(player).knowsLanguage());
    }

    public static boolean knowsDwarvishServerBypassCreative(Player player) {
        return player != null &&  DwarvenLanguage.get(player).knowsLanguage();
    }

    /**
     * SERVER-SIDE: Set language.
     */
    public static void setKnowsDwarvishServer(Player player, boolean value) {
        if (player == null) return;
        var lang = DwarvenLanguage.get(player); // Uses static helper
        lang.setKnowsLanguage(value);
    }

    /**
     * CLIENT-SIDE: Checks if the local player knows Dwarvish or is in creative mode.
     * Safe for tooltips, renders, and GUIs.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean knowsDwarvishClient() {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.isCreative() || MyClientLanguageData.knowsLanguage());
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean knowsDwarvishClientBypassCreative() {
        Player player = Minecraft.getInstance().player;
        return player != null && MyClientLanguageData.knowsLanguage();
    }
}
