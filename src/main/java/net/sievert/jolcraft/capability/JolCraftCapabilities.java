package net.sievert.jolcraft.capability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.sievert.jolcraft.JolCraft;


public class JolCraftCapabilities {
    public static final EntityCapability<DwarvenLanguage, Void> DWARVEN_LANGUAGE =
            EntityCapability.createVoid(
                    ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarven_language"),
                    DwarvenLanguage.class
            );
}