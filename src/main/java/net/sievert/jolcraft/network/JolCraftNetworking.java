package net.sievert.jolcraft.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.network.client.data.MyClientAncientLanguageData;
import net.sievert.jolcraft.network.client.data.MyClientDeliriumData;
import net.sievert.jolcraft.network.client.data.MyClientLanguageData;
import net.sievert.jolcraft.network.client.data.MyClientReputationData;
import net.sievert.jolcraft.network.packet.*;

import java.util.Set;

public class JolCraftNetworking {

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(JolCraft.MOD_ID)
                .versioned("1.0")
                .playToClient(
                        ClientboundDeliriumPacket.TYPE,
                        ClientboundDeliriumPacket.CODEC,
                        JolCraftNetworking::handleDelirium
                )
                .playToClient(
                        ClientboundLanguagePacket.TYPE,
                        ClientboundLanguagePacket.CODEC,
                        JolCraftNetworking::handleSyncLanguage
                )
                .playToClient(
                        ClientboundAncientLanguagePacket.TYPE,
                        ClientboundAncientLanguagePacket.CODEC,
                        JolCraftNetworking::handleSyncAncientLanguage
                )
                .playToClient(
                        ClientboundReputationPacket.TYPE,
                        ClientboundReputationPacket.CODEC,
                        JolCraftNetworking::handleSyncReputation
                )
                .playToClient(
                        ClientboundDwarfEndorseAnimationPacket.TYPE,
                        ClientboundDwarfEndorseAnimationPacket.CODEC,
                        JolCraftNetworking::handleDwarfEndorseAnimation
                )
                .playToClient(
                        ClientboundEndorsementsPacket.TYPE,
                        ClientboundEndorsementsPacket.CODEC,
                        JolCraftNetworking::handleSyncEndorsements
                );

    }

    @OnlyIn(Dist.CLIENT)
    private static void handleDelirium(ClientboundDeliriumPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientDeliriumData.setMuffleTicks(packet.durationTicks());
        });
    }


    @OnlyIn(Dist.CLIENT)
    private static void handleSyncLanguage(ClientboundLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientLanguageData.setKnows(packet.knowsLanguage());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncAncientLanguage(ClientboundAncientLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientAncientLanguageData.setKnows(packet.knowsLanguage());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncReputation(ClientboundReputationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientReputationData.setTier(packet.tier());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleDwarfEndorseAnimation(ClientboundDwarfEndorseAnimationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientReputationData.setEndorsementAnimation(packet.entityId(), true);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncEndorsements(ClientboundEndorsementsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Set<ResourceLocation> set = packet.endorsements();
            MyClientReputationData.setEndorsements(set);
        });
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(payload);
    }

    public static void sendToNearbyClients(Level world, BlockPos pos, double radius, CustomPacketPayload payload) {
        if (!(world instanceof ServerLevel serverLevel)) return;
        double radiusSq = radius * radius;
        for (ServerPlayer player : serverLevel.players()) {
            if (player.blockPosition().distSqr(pos) <= radiusSq) {
                sendToClient(player, payload);
            }
        }
    }
}
