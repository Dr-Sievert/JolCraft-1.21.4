package net.sievert.jolcraft.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfEntity;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfHistorianEntity;

import java.util.function.Supplier;

public class JolCraftEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, JolCraft.MOD_ID);

    //Dwarves

    public static ResourceKey<EntityType<?>> DWARF_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("dwarf"));

    public static ResourceKey<EntityType<?>> DWARF_GUARD_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("dwarf_guard"));

    public static ResourceKey<EntityType<?>> DWARF_HISTORIAN_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("dwarf_historian"));

    public static final Supplier<EntityType<DwarfEntity>> DWARF =
            ENTITY_TYPES.register("dwarf", () -> EntityType.Builder.of(DwarfEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_KEY));

    public static final Supplier<EntityType<DwarfGuardEntity>> DWARF_GUARD =
            ENTITY_TYPES.register("dwarf_guard", () -> EntityType.Builder.of(DwarfGuardEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_GUARD_KEY));

    public static final Supplier<EntityType<DwarfHistorianEntity>> DWARF_HISTORIAN =
            ENTITY_TYPES.register("dwarf_historian", () -> EntityType.Builder.of(DwarfHistorianEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_HISTORIAN_KEY));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
