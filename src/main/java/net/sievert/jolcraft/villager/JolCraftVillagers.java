package net.sievert.jolcraft.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;

public class JolCraftVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, JolCraft.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, JolCraft.MOD_ID);

    public static final Holder<PoiType> FAKE_POI = POI_TYPES.register("dwarf_poi", () ->
            new PoiType(ImmutableSet.of(), 1, 1)
    ); // No block states — impossible to claim


    public static final Holder<VillagerProfession> TRADER = VILLAGER_PROFESSIONS.register("trader", () ->
            new VillagerProfession(
                    "trader",
                    poi -> false,  // won't claim any POI
                    poi -> false,
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_TOOLSMITH // or your own sound
            )
    );


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}