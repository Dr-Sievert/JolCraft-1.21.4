package net.sievert.jolcraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.network.packet.ClientboundSyncLanguagePacket;

public class JolCraftNetworking {

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(JolCraft.MOD_ID)
                .versioned("1.0")
                .playToClient(
                        ClientboundSyncLanguagePacket.TYPE,
                        ClientboundSyncLanguagePacket.CODEC,
                        JolCraftNetworking::handleSyncLanguage
                );
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSyncLanguage(ClientboundSyncLanguagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            MyClientLanguageData.setKnows(packet.knowsLanguage());
            System.out.println("[SYNC] Received packet: knowsLanguage = " + packet.knowsLanguage());

        });
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(payload);
    }
}
