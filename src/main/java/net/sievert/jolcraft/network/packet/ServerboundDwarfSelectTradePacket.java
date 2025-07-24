package net.sievert.jolcraft.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

public class ServerboundDwarfSelectTradePacket implements CustomPacketPayload {
    public static final Type<ServerboundDwarfSelectTradePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "serverbound_dwarf_select_trade"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDwarfSelectTradePacket> CODEC =
            CustomPacketPayload.codec(ServerboundDwarfSelectTradePacket::write, ServerboundDwarfSelectTradePacket::read);

    private final int item;

    public ServerboundDwarfSelectTradePacket(int item) {
        this.item = item;
    }

    public static ServerboundDwarfSelectTradePacket read(RegistryFriendlyByteBuf buf) {
        int item = buf.readVarInt();
        return new ServerboundDwarfSelectTradePacket(item);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.item);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public int getItem() {
        return item;
    }
}
