package net.sievert.jolcraft;


import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.client.render.*;
import net.sievert.jolcraft.item.JolCraftCreativeModeTabs;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.loot.JolCraftLootModifiers;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.world.processor.JolCraftProcessors;
import net.sievert.jolcraft.world.structure.JolCraftStructures;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(JolCraft.MOD_ID)
public class JolCraft
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "jolcraft";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public JolCraft(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // For registration and init stuff.
        JolCraftItems.register(modEventBus);
        JolCraftCreativeModeTabs.register(modEventBus);

        JolCraftDataComponents.register(modEventBus);

        JolCraftEntities.register(modEventBus);

        JolCraftLootModifiers.register(modEventBus);

        JolCraftSounds.register(modEventBus);

        JolCraftProcessors.register(modEventBus);

        JolCraftAttachments.register(modEventBus);

        JolCraftStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);


        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(JolCraftNetworking::register);

        modEventBus.addListener(JolCraftCriteriaTriggers::register);



        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add items to creative tabs
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        /*
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS){
            event.accept(JolCraftItems.DWARF_SPAWN_EGG);
            event.accept(JolCraftItems.DWARF_GUARD_SPAWN_EGG);
            event.accept(JolCraftItems.DWARF_HISTORIAN_SPAWN_EGG);
            event.accept(JolCraftItems.DWARF_SCRAPPER_SPAWN_EGG);
            event.accept(JolCraftItems.DWARF_MERCHANT_SPAWN_EGG);
        }
        */
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(JolCraftEntities.DWARF.get(), DwarfRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_GUILDMASTER.get(), DwarfGuildmasterRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_GUARD.get(), DwarfGuardRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_HISTORIAN.get(), DwarfHistorianRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_SCRAPPER.get(), DwarfScrapperRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_MERCHANT.get(), DwarfMerchantRenderer::new);

            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ResourceLocation locate(String path) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, path);
    }
}
