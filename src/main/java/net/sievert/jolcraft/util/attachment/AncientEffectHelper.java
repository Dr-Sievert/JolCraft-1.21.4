package net.sievert.jolcraft.util.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.sievert.jolcraft.effect.JolCraftEffects;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.network.client.data.MyClientAncientLanguageData;

public class AncientEffectHelper {
    public static final ResourceLocation SGA_FONT = ResourceLocation.withDefaultNamespace("alt");

    /**
     * Returns a Component: readable if the player has Ancient Memory (effect or permanent), otherwise SGA runes.
     * SERVER-side version.
     */
    public static Component getAncientText(Player player, Component readable) {
        if (hasAncientMemoryServer(player)) {
            return readable;
        } else {
            return readable.copy().withStyle(style -> style.withFont(SGA_FONT));
        }
    }

    /**
     * True if player has Ancient Memory effect or permanent knowledge. SERVER-side.
     */
    public static boolean hasAncientMemoryServer(Player player) {
        return player != null && (player.hasEffect(JolCraftEffects.ANCIENT_MEMORY)
                || player.isCreative()
                || net.sievert.jolcraft.attachment.AncientDwarvenLanguage.get(player).knowsLanguage());
    }

    public static boolean hasAncientMemoryServerBypassCreative(Player player) {
        return player != null && (player.hasEffect(JolCraftEffects.ANCIENT_MEMORY)
                || net.sievert.jolcraft.attachment.AncientDwarvenLanguage.get(player).knowsLanguage());
    }

    /**
     * CLIENT-side version: check local effect + client sync boolean.
     * Used for tooltips, GUIs, render, etc.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean hasAncientMemoryClient() {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.hasEffect(JolCraftEffects.ANCIENT_MEMORY)
                || player.isCreative()
                || MyClientAncientLanguageData.knowsLanguage());
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean hasAncientMemoryClientBypassCreative() {
        Player player = Minecraft.getInstance().player;
        return player != null && MyClientAncientLanguageData.knowsLanguage();
    }
}
