package net.sievert.jolcraft;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.attachment.JolCraftAttachments;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.effect.JolCraftEffects;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.client.render.animal.MuffhornRenderer;
import net.sievert.jolcraft.entity.client.render.block.StrongboxRenderer;
import net.sievert.jolcraft.entity.client.render.dwarf.*;
import net.sievert.jolcraft.item.JolCraftCreativeModeTabs;
import net.sievert.jolcraft.item.armor.JolCraftEquipmentAssets;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.loot.JolCraftLootModifiers;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.client.data.MyClientDeliriumData;
import net.sievert.jolcraft.item.potion.JolCraftPotions;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.strongbox.LockMenu;
import net.sievert.jolcraft.screen.custom.strongbox.LockScreen;
import net.sievert.jolcraft.screen.custom.strongbox.StrongboxScreen;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.ServerTickHandler;
import net.sievert.jolcraft.worldgen.JolCraftBlockPredicateTypes;
import net.sievert.jolcraft.worldgen.processor.JolCraftProcessors;
import net.sievert.jolcraft.worldgen.structure.JolCraftStructures;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
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
    public JolCraft(IEventBus modEventBus, ModContainer modContainer) {

        JolCraftBlocks.register(modEventBus);
        JolCraftItems.register(modEventBus);
        JolCraftEntities.register(modEventBus);
        JolCraftBlockEntities.register(modEventBus);
        JolCraftMenuTypes.register(modEventBus);
        JolCraftCreativeModeTabs.register(modEventBus);
        JolCraftDataComponents.register(modEventBus);
        JolCraftLootModifiers.register(modEventBus);
        JolCraftSounds.register(modEventBus);
        JolCraftEffects.register(modEventBus);
        JolCraftPotions.register(modEventBus);
        JolCraftProcessors.register(modEventBus);
        JolCraftBlockPredicateTypes.register(modEventBus);
        JolCraftAttachments.register(modEventBus);
        JolCraftEquipmentAssets.register(modEventBus);
        JolCraftStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        // Register server events
        NeoForge.EVENT_BUS.register(this);

        //Client tick
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, this::onClientTick);

        ServerTickHandler.register();

        // Register items to creative tabs (if needed)
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(JolCraftNetworking::register);
        modEventBus.addListener(JolCraftCriteriaTriggers::register);



        // Register the config file
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
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(JolCraftBlockEntities.STRONGBOX.get(), StrongboxRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(JolCraftMenuTypes.STRONGBOX_MENU.get(), StrongboxScreen::new);
            event.register(JolCraftMenuTypes.LOCK_MENU.get(), LockScreen::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            //Dwarves
            EntityRenderers.register(JolCraftEntities.DWARF.get(), DwarfRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_GUILDMASTER.get(), DwarfGuildmasterRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_HISTORIAN.get(), DwarfHistorianRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_MERCHANT.get(), DwarfMerchantRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_SCRAPPER.get(), DwarfScrapperRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_BREWMASTER.get(), DwarfBrewmasterRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_GUARD.get(), DwarfGuardRenderer::new);
            EntityRenderers.register(JolCraftEntities.DWARF_KEEPER.get(), DwarfKeeperRenderer::new);

            //Animals
            EntityRenderers.register(JolCraftEntities.MUFFHORN.get(), MuffhornRenderer::new);

            //Blocks
            //ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.STRONGBOX.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.VERDANT_FARMLAND.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.BARLEY_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.DEEPSLATE_BULBS_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.ASGARNIAN_CROP_BOTTOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.ASGARNIAN_CROP_TOP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.DUSKHOLD_CROP_BOTTOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.DUSKHOLD_CROP_TOP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.KRANDONIAN_CROP_BOTTOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.KRANDONIAN_CROP_TOP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.YANILLIAN_CROP_BOTTOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.YANILLIAN_CROP_TOP.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.FERMENTING_CAULDRON.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.DUSKCAP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.POTTED_DUSKCAP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.FESTERLING_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.FESTERLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(JolCraftBlocks.POTTED_FESTERLING.get(), RenderType.cutout());

        }

    }

    public static ResourceLocation locate(String path) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, path);
    }

    @OnlyIn(Dist.CLIENT)
    private void onClientTick(ClientTickEvent event) {
        // Only handle at the end of each tick
        if (event instanceof ClientTickEvent.Post) {
            Minecraft mc = Minecraft.getInstance();

            // Existing lock screen logic
            if (mc.player != null && mc.screen instanceof LockScreen lockScreen) {
                LockMenu menu = lockScreen.getMenu();
                if (menu != null) {
                    menu.tick();
                }
            }

            // Delirium "rising edge" logic
            // Track previous muffleTicks value
            int prevMuffleTicks = MyClientDeliriumData.getAndStorePreviousTicks(); // You need to implement this or use a static field here
            int currentMuffleTicks = MyClientDeliriumData.getMuffleTicks();

            if (prevMuffleTicks == 0 && currentMuffleTicks > 0) {
                // Burst just started: play hallucination sound
                if (mc.player != null) {
                    mc.player.playSound(SoundEvents.AMBIENT_CAVE.value(), 0.7F + mc.level.random.nextFloat() * 0.4F, 0.8F + mc.level.random.nextFloat() * 0.4F);
                }
            }
            MyClientDeliriumData.tick();
        }
    }



}
