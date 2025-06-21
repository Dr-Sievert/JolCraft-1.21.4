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
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.client.data.MyClientReputationData;
import net.sievert.jolcraft.network.packet.ClientboundSyncLanguagePacket;
import net.sievert.jolcraft.network.packet.ClientboundSyncReputationPacket;
import net.sievert.jolcraft.network.packet.ClientboundDwarfEndorseAnimationPacket;
import net.sievert.jolcraft.network.packet.ClientboundSyncEndorsementsPacket;

import java.util.Set;

public class JolCraftNetworking {

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(JolCraft.MOD_ID)
                .versioned("1.0")
                .playToClient(
                        ClientboundSyncLanguagePacket.TYPE,
                        ClientboundSyncLanguagePacket.CODEC,
                        JolCraftNetworking::handleSyncLanguage
                )
                .playToClient(
                        ClientboundSyncReputationPacket.TYPE,
                        ClientboundSyncReputationPacket.CODEC,
                        JolCraftNetworking::handleSyncReputation
                )
                .playToClient(
                        ClientboundDwarfEndorseAnimationPacket.TYPE,
                        ClientboundDwarfEndorseAnimationPacket.CODEC,
                        JolCraftNetworking::handleDwarfEndorseAnimation
                )
                .playToClient(
                        ClientboundSyncEndorsementsPacket.TYPE,
                        ClientboundSyncEndorsementsPacket.CODEC,
                        JolCraftNetworking::handleSyncEndorsements
                );
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncLanguage(ClientboundSyncLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientLanguageData.setKnows(packet.knowsLanguage());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncReputation(ClientboundSyncReputationPacket packet, IPayloadContext context) {
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
    private static void handleSyncEndorsements(ClientboundSyncEndorsementsPacket packet, IPayloadContext context) {
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
