package net.sievert.jolcraft.util.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.data.custom.attachment.lang.AncientDwarvenLanguage;
import net.sievert.jolcraft.network.client.data.ClientAncientLanguageData;

public class AncientDwarvenLanguageHelper {

    /**
     * SERVER-SIDE: Checks if the player knows Ancient Dwarvish or is in creative mode.
     */
    public static boolean knowsAncientDwarvishServer(Player player) {
        return player != null && (player.isCreative() || AncientDwarvenLanguage.get(player).knowsLanguage());
    }

    public static boolean knowsAncientDwarvishServerBypassCreative(Player player) {
        return player != null && AncientDwarvenLanguage.get(player).knowsLanguage();
    }

    /**
     * SERVER-SIDE: Set ancient language knowledge.
     */
    public static void setKnowsAncientDwarvishServer(Player player, boolean value) {
        if (player == null) return;
        var lang = AncientDwarvenLanguage.get(player);
        lang.setKnowsLanguage(value);
    }

    /**
     * CLIENT-SIDE: Checks if the local player knows Ancient Dwarvish or is in creative mode.
     * Safe for tooltips, renders, and GUIs.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean knowsAncientDwarvishClient() {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.isCreative() || ClientAncientLanguageData.knowsLanguage());
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean knowsAncientDwarvishClientBypassCreative() {
        Player player = Minecraft.getInstance().player;
        return player != null && ClientAncientLanguageData.knowsLanguage();
    }
}
