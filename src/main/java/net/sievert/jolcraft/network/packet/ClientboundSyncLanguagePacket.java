package net.sievert.jolcraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

public record ClientboundSyncLanguagePacket(boolean knowsLanguage) implements CustomPacketPayload {
    public static final Type<ClientboundSyncLanguagePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "sync_language"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncLanguagePacket> CODEC =
            CustomPacketPayload.codec(ClientboundSyncLanguagePacket::write, ClientboundSyncLanguagePacket::read);

    public static ClientboundSyncLanguagePacket read(FriendlyByteBuf buf) {
        return new ClientboundSyncLanguagePacket(buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(knowsLanguage);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
