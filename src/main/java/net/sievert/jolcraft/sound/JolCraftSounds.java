package net.sievert.jolcraft.sound;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;

import java.util.function.Supplier;

public class JolCraftSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, JolCraft.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_DEEPSLATE =
            SOUND_EVENTS.register("armor_equip_deepslate", () ->
                    SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "armor_equip_deepslate")));
    //Random
    public static final Supplier<SoundEvent> LEVEL_UP = registerSoundEvent("level_up");
    public static final Supplier<SoundEvent> POOF = registerSoundEvent("poof");

    //Dwarf
    public static final Supplier<SoundEvent> DWARF_AMBIENT = registerSoundEvent("dwarf_ambient");
    public static final Supplier<SoundEvent> DWARF_HURT = registerSoundEvent("dwarf_hurt");
    public static final Supplier<SoundEvent> DWARF_DEATH = registerSoundEvent("dwarf_death");
    public static final Supplier<SoundEvent> DWARF_YES = registerSoundEvent("dwarf_yes");
    public static final Supplier<SoundEvent> DWARF_NO = registerSoundEvent("dwarf_no");
    public static final Supplier<SoundEvent> DWARF_TRADE = registerSoundEvent("dwarf_trade");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
