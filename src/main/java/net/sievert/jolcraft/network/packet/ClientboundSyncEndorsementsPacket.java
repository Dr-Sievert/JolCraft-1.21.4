package net.sievert.jolcraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent from server to client to sync the player's full set of profession endorsements.
 */
public record ClientboundSyncEndorsementsPacket(Set<ResourceLocation> endorsements) implements CustomPacketPayload {

    public static final Type<ClientboundSyncEndorsementsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "endorsement_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncEndorsementsPacket> CODEC =
            CustomPacketPayload.codec(ClientboundSyncEndorsementsPacket::write, ClientboundSyncEndorsementsPacket::read);

    public static ClientboundSyncEndorsementsPacket read(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Set<ResourceLocation> endorsements = new HashSet<>();
        for (int i = 0; i < size; i++) {
            endorsements.add(buf.readResourceLocation());
        }
        return new ClientboundSyncEndorsementsPacket(endorsements);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(endorsements.size());
        for (ResourceLocation rl : endorsements) {
            buf.writeResourceLocation(rl);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
