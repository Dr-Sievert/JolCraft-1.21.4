package net.sievert.jolcraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.network.client.data.MyClientAncientLanguageData;

public record ClientboundSyncAncientLanguagePacket(boolean knowsLanguage) implements CustomPacketPayload {
    public static final Type<ClientboundSyncAncientLanguagePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "sync_ancient_language"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncAncientLanguagePacket> CODEC =
            CustomPacketPayload.codec(ClientboundSyncAncientLanguagePacket::write, ClientboundSyncAncientLanguagePacket::read);

    public static ClientboundSyncAncientLanguagePacket read(FriendlyByteBuf buf) {
        return new ClientboundSyncAncientLanguagePacket(buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(knowsLanguage);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // This is your client-side handler
    public void handle() {
        MyClientAncientLanguageData.setKnows(knowsLanguage);
    }
}
