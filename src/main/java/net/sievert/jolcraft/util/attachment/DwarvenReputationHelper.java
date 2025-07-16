package net.sievert.jolcraft.util.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.sievert.jolcraft.attachment.custom.rep.DwarvenReputation;
import net.sievert.jolcraft.network.client.data.MyClientReputationData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Set;

public class DwarvenReputationHelper {

    // --- SERVER SIDE ---

    // Normal: Creative OR tier
    public static boolean hasTierServer(Player player, int minTier) {
        return player != null && (player.isCreative() || DwarvenReputation.get(player).getTier() >= minTier);
    }
    // Strict: Only actual rep, NOT creative
    public static boolean hasTierServerBypassCreative(Player player, int minTier) {
        return player != null && DwarvenReputation.get(player).getTier() >= minTier;
    }

    // Normal: Creative OR endorsement
    public static boolean hasEndorsementServer(Player player, ResourceLocation professionId) {
        return player != null && (player.isCreative() || DwarvenReputation.get(player).hasEndorsement(professionId));
    }
    // Strict: Only actual endorsement, NOT creative
    public static boolean hasEndorsementServerBypassCreative(Player player, ResourceLocation professionId) {
        return player != null && DwarvenReputation.get(player).hasEndorsement(professionId);
    }

    // --- BYPASS CREATIVE (STRICT) ---

    public static int getEndorsementCountServerBypassCreative(Player player) {
        return player != null ? DwarvenReputation.get(player).getEndorsementCount() : 0;
    }

    public static int getTierServerBypassCreative(Player player) {
        return player != null ? DwarvenReputation.get(player).getTier() : 0;
    }

    public static Set<ResourceLocation> getAllEndorsementsServerBypassCreative(Player player) {
        return player != null ? DwarvenReputation.get(player).getEndorsements() : Set.of();
    }

    // Legacy (non-strict) accessors for completeness:
    public static int getEndorsementCountServer(Player player) {
        return player != null ? DwarvenReputation.get(player).getEndorsementCount() : 0;
    }

    public static int getTierServer(Player player) {
        return player != null ? DwarvenReputation.get(player).getTier() : 0;
    }

    public static Set<ResourceLocation> getAllEndorsementsServer(Player player) {
        return player != null ? DwarvenReputation.get(player).getEndorsements() : Set.of();
    }

    // --- CLIENT SIDE ---

    @OnlyIn(Dist.CLIENT)
    public static boolean hasClientTier(int minTier) {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.isCreative() || MyClientReputationData.getTier() >= minTier);
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean hasClientTierBypassCreative(int minTier) {
        Player player = Minecraft.getInstance().player;
        return player != null && MyClientReputationData.getTier() >= minTier;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean hasClientEndorsement(ResourceLocation professionId) {
        Player player = Minecraft.getInstance().player;
        return player != null && (player.isCreative() || MyClientReputationData.hasEndorsement(professionId));
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean hasClientEndorsementBypassCreative(ResourceLocation professionId) {
        Player player = Minecraft.getInstance().player;
        return player != null && MyClientReputationData.hasEndorsement(professionId);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getClientTier() {
        return MyClientReputationData.getTier();
    }

    @OnlyIn(Dist.CLIENT)
    public static int getClientEndorsementCount() {
        return MyClientReputationData.endorsementCount();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<ResourceLocation> getAllClientEndorsements() {
        return MyClientReputationData.getAllEndorsements();
    }
}
