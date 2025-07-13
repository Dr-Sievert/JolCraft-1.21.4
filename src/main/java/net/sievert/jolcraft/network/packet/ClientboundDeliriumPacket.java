package net.sievert.jolcraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

/**
 * Sent from server to client to trigger Delirium hallucination/muffle.
 */
public record ClientboundDeliriumPacket(int durationTicks) implements CustomPacketPayload {
    public static final Type<ClientboundDeliriumPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "delirium"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDeliriumPacket> CODEC =
            CustomPacketPayload.codec(ClientboundDeliriumPacket::write, ClientboundDeliriumPacket::read);

    public static ClientboundDeliriumPacket read(FriendlyByteBuf buf) {
        return new ClientboundDeliriumPacket(buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(durationTicks);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
