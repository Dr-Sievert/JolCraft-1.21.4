package net.sievert.jolcraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

public record ClientboundSyncReputationPacket(int tier) implements CustomPacketPayload {
    public static final Type<ClientboundSyncReputationPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "sync_reputation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncReputationPacket> CODEC =
            CustomPacketPayload.codec(ClientboundSyncReputationPacket::write, ClientboundSyncReputationPacket::read);

    public static ClientboundSyncReputationPacket read(FriendlyByteBuf buf) {
        return new ClientboundSyncReputationPacket(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(tier);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
