package net.sievert.jolcraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.network.client.data.ClientAncientLanguageData;
import net.sievert.jolcraft.network.client.data.ClientDeliriumData;
import net.sievert.jolcraft.network.client.data.ClientLanguageData;
import net.sievert.jolcraft.network.client.data.ClientReputationData;
import net.sievert.jolcraft.network.packet.*;
import net.sievert.jolcraft.screen.custom.dwarf.DwarfMerchantMenu;
import net.sievert.jolcraft.screen.custom.dwarf.DwarfMerchantScreen;

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
                )
                .playToClient(
                        ClientboundTomeUnlocksPacket.TYPE,
                        ClientboundTomeUnlocksPacket.CODEC,
                        JolCraftNetworking::handleSyncTomeUnlocks
                )
                .playToClient(
                        ClientboundDwarfMerchantOffersPacket.TYPE,
                        ClientboundDwarfMerchantOffersPacket.CODEC,
                        JolCraftNetworking::handleDwarfMerchantOffers
                )
                .playToServer(
                        ServerboundDwarfSelectTradePacket.TYPE,
                        ServerboundDwarfSelectTradePacket.CODEC,
                        JolCraftNetworking::handleServerboundDwarfSelectTrade
                )
                .playToClient(
                        ClientboundPlaySoundPacket.TYPE,
                        ClientboundPlaySoundPacket.CODEC,
                        JolCraftNetworking::handlePlaySound
                );


    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePlaySound(ClientboundPlaySoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            var player = mc.player;
            if (player == null) return;

            // 1.21+ registry access
            var optHolder = net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.get(packet.soundId());
            if (optHolder.isEmpty()) return;
            var sound = optHolder.get().value();

            player.level().playLocalSound(
                    packet.x(), packet.y(), packet.z(),
                    sound,
                    packet.source(),
                    packet.volume(),
                    packet.pitch(),
                    false
            );
        });
    }


    public static void handleServerboundDwarfSelectTrade(ServerboundDwarfSelectTradePacket packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // Ensure on server thread
        context.enqueueWork(() -> {
            // Get the player sending the packet
            var player = context.player();
            if (player == null) return; // Defensive: should always exist

            // Get the menu (your custom merchant menu)
            if (player.containerMenu instanceof DwarfMerchantMenu menu) {
                // Optionally, check menu containerId matches (packet can carry it if you want extra safety)
                menu.setSelectionHint(packet.getItem());
                menu.tryMoveItems(packet.getItem());
            }
        });
    }


    @OnlyIn(Dist.CLIENT)
    private static void handleDwarfMerchantOffers(ClientboundDwarfMerchantOffersPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            AbstractContainerMenu abstractContainerMenu = mc.player.containerMenu;
            if (packet.containerId() == abstractContainerMenu.containerId && abstractContainerMenu instanceof DwarfMerchantMenu dwarfMenu) {
                dwarfMenu.setOffers(packet.offers());
                dwarfMenu.setXp(packet.dwarfXp());
                dwarfMenu.setMerchantLevel(packet.dwarfLevel());
                dwarfMenu.setShowProgressBar(packet.showProgress());
                dwarfMenu.setCanRestock(packet.canRestock());
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncTomeUnlocks(ClientboundTomeUnlocksPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            net.sievert.jolcraft.network.client.data.ClientTomeUnlocksData.setUnlocks(packet.unlocks());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleDelirium(ClientboundDeliriumPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientDeliriumData.setMuffleTicks(packet.durationTicks());
        });
    }


    @OnlyIn(Dist.CLIENT)
    private static void handleSyncLanguage(ClientboundLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLanguageData.setKnows(packet.knowsLanguage());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncAncientLanguage(ClientboundAncientLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientAncientLanguageData.setKnows(packet.knowsLanguage());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncReputation(ClientboundReputationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientReputationData.setTier(packet.tier());
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleDwarfEndorseAnimation(ClientboundDwarfEndorseAnimationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientReputationData.setEndorsementAnimation(packet.entityId(), true);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncEndorsements(ClientboundEndorsementsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Set<ResourceLocation> set = packet.endorsements();
            ClientReputationData.setEndorsements(set);
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
