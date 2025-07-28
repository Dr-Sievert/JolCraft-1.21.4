package net.sievert.jolcraft;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.*;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.client.CoinPouchTooltipRenderer;
import net.sievert.jolcraft.data.JolCraftAttachments;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.data.JolCraftStats;
import net.sievert.jolcraft.data.custom.item.CoinPouchTooltip;
import net.sievert.jolcraft.effect.JolCraftEffects;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.attribute.JolCraftAttributes;
import net.sievert.jolcraft.entity.client.render.animal.MuffhornRenderer;
import net.sievert.jolcraft.entity.client.render.block.StrongboxRenderer;
import net.sievert.jolcraft.entity.client.render.dwarf.*;
import net.sievert.jolcraft.entity.client.render.object.RadiantRenderer;
import net.sievert.jolcraft.event.JolCraftClientGameEvents;
import net.sievert.jolcraft.item.JolCraftCreativeModeTabs;
import net.sievert.jolcraft.item.armor.JolCraftEquipmentAssets;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.loot.JolCraftLootModifiers;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.client.data.ClientDeliriumData;
import net.sievert.jolcraft.item.potion.JolCraftPotions;
import net.sievert.jolcraft.recipe.JolCraftRecipes;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.lapidary_bench.LapidaryBenchScreen;
import net.sievert.jolcraft.screen.custom.strongbox.LockMenu;
import net.sievert.jolcraft.screen.custom.strongbox.LockScreen;
import net.sievert.jolcraft.screen.custom.strongbox.StrongboxScreen;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.server.ServerTickHandler;
import net.sievert.jolcraft.worldgen.JolCraftBlockPredicateTypes;
import net.sievert.jolcraft.worldgen.JolCraftProcessors;
import net.sievert.jolcraft.worldgen.JolCraftStructures;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
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
        JolCraftStats.register(modEventBus);
        JolCraftEquipmentAssets.register(modEventBus);
        JolCraftRecipes.register(modEventBus);
        JolCraftAttributes.register(modEventBus);
        JolCraftStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(JolCraftNetworking::register);
        modEventBus.addListener(JolCraftCriteriaTriggers::register);

        //Server tick
        ServerTickHandler.register();

        // Register the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(JolCraftStats::initializeStats);
    }

    public static ResourceLocation locate(String path) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, path);
    }


}
