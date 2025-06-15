package net.sievert.jolcraft.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.sievert.jolcraft.capability.DwarvenLanguage;
import net.sievert.jolcraft.capability.DwarvenLanguageImpl;
import net.sievert.jolcraft.capability.JolCraftCapabilities;

public class JolCraftCapabilityEvents {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                JolCraftCapabilities.DWARVEN_LANGUAGE,
                EntityType.PLAYER,
                new CapabilityWithNBT()
        );
    }

    // This class implements both capability provider and serializer
    private static class CapabilityWithNBT implements ICapabilityProvider<Player, Void, DwarvenLanguage>, INBTSerializable<CompoundTag> {
        private final DwarvenLanguageImpl impl = new DwarvenLanguageImpl();

        @Override
        public DwarvenLanguage getCapability(Player player, Void context) {
            return impl;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            return impl.serializeNBT(provider);
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            impl.deserializeNBT(provider, tag);
        }
    }
}
