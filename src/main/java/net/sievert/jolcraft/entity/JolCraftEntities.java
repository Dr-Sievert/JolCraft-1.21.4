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
import net.sievert.jolcraft.entity.custom.dwarf.*;

import java.util.function.Supplier;

public class JolCraftEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, JolCraft.MOD_ID);

    //Dwarves

    public static ResourceKey<EntityType<?>> DWARF_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf"));
    public static ResourceKey<EntityType<?>> DWARF_GUILDMASTER_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_guildmaster"));
    public static ResourceKey<EntityType<?>> DWARF_HISTORIAN_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_historian"));
    public static ResourceKey<EntityType<?>> DWARF_MERCHANT_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_merchant"));
    public static ResourceKey<EntityType<?>> DWARF_SCRAPPER_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_scrapper"));
    public static ResourceKey<EntityType<?>> DWARF_BREWMASTER_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_brewmaster"));
    public static ResourceKey<EntityType<?>> DWARF_GUARD_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_guard"));
    public static ResourceKey<EntityType<?>> DWARF_KEEPER_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID,"dwarf_keeper"));

    public static final Supplier<EntityType<DwarfEntity>> DWARF =
            ENTITY_TYPES.register("dwarf", () -> EntityType.Builder.of(DwarfEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_KEY));

    public static final Supplier<EntityType<DwarfGuildmasterEntity>> DWARF_GUILDMASTER =
            ENTITY_TYPES.register("dwarf_guildmaster", () -> EntityType.Builder.of(DwarfGuildmasterEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_GUILDMASTER_KEY));

    public static final Supplier<EntityType<DwarfHistorianEntity>> DWARF_HISTORIAN =
            ENTITY_TYPES.register("dwarf_historian", () -> EntityType.Builder.of(DwarfHistorianEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_HISTORIAN_KEY));

    public static final Supplier<EntityType<DwarfMerchantEntity>> DWARF_MERCHANT =
            ENTITY_TYPES.register("dwarf_merchant", () -> EntityType.Builder.of(DwarfMerchantEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_MERCHANT_KEY));

    public static final Supplier<EntityType<DwarfScrapperEntity>> DWARF_SCRAPPER =
            ENTITY_TYPES.register("dwarf_scrapper", () -> EntityType.Builder.of(DwarfScrapperEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_SCRAPPER_KEY));

    public static final Supplier<EntityType<DwarfBrewmasterEntity>> DWARF_BREWMASTER =
            ENTITY_TYPES.register("dwarf_brewmaster", () -> EntityType.Builder.of(DwarfBrewmasterEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_BREWMASTER_KEY));

    public static final Supplier<EntityType<DwarfGuardEntity>> DWARF_GUARD =
            ENTITY_TYPES.register("dwarf_guard", () -> EntityType.Builder.of(DwarfGuardEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_GUARD_KEY));

    public static final Supplier<EntityType<DwarfKeeperEntity>> DWARF_KEEPER =
            ENTITY_TYPES.register("dwarf_keeper", () -> EntityType.Builder.of(DwarfKeeperEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 1.6f).build(DWARF_KEEPER_KEY));





    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
