package net.sievert.jolcraft.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.client.model.animal.MuffhornModel;
import net.sievert.jolcraft.entity.client.model.block.StrongboxModel;
import net.sievert.jolcraft.entity.client.model.dwarf.*;
import net.sievert.jolcraft.entity.client.render.block.StrongboxRenderer;
import net.sievert.jolcraft.entity.custom.animal.MuffhornEntity;
import net.sievert.jolcraft.entity.custom.dwarf.*;
import net.sievert.jolcraft.screen.JolCraftMenuTypes;
import net.sievert.jolcraft.screen.custom.StrongboxScreen;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class JolCraftModEvents {

   //Register Model Layers
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

        //Dwarves
        event.registerLayerDefinition(DwarfModel.LAYER_LOCATION, DwarfModel::createBodyLayer);
        event.registerLayerDefinition(DwarfGuildmasterModel.LAYER_LOCATION, DwarfGuildmasterModel::createBodyLayer);
        event.registerLayerDefinition(DwarfHistorianModel.LAYER_LOCATION, DwarfHistorianModel::createBodyLayer);
        event.registerLayerDefinition(DwarfMerchantModel.LAYER_LOCATION, DwarfMerchantModel::createBodyLayer);
        event.registerLayerDefinition(DwarfScrapperModel.LAYER_LOCATION, DwarfScrapperModel::createBodyLayer);
        event.registerLayerDefinition(DwarfBrewmasterModel.LAYER_LOCATION, DwarfBrewmasterModel::createBodyLayer);
        event.registerLayerDefinition(DwarfGuardModel.LAYER_LOCATION, DwarfGuardModel::createBodyLayer);
        event.registerLayerDefinition(DwarfKeeperModel.LAYER_LOCATION, DwarfKeeperModel::createBodyLayer);

        //Animals
        event.registerLayerDefinition(MuffhornModel.LAYER_LOCATION, MuffhornModel::createBodyLayer);
        event.registerLayerDefinition(
                MuffhornModel.BABY_LAYER_LOCATION,
                () -> MuffhornModel.createBodyLayer().apply(MuffhornModel.BABY_TRANSFORMER)
        );

        //Blocks
        event.registerLayerDefinition(StrongboxModel.LAYER_LOCATION, StrongboxModel::createBodyLayer);

    }

    //Register Attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        //Dwarves
        event.put(JolCraftEntities.DWARF.get(), DwarfEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_GUILDMASTER.get(), DwarfGuildmasterEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_HISTORIAN.get(), DwarfHistorianEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_MERCHANT.get(), DwarfMerchantEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_SCRAPPER.get(), DwarfScrapperEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_BREWMASTER.get(), DwarfBrewmasterEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_GUARD.get(), DwarfGuardEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_KEEPER.get(), DwarfKeeperEntity.createAttributes().build());

        //Animals
        event.put(JolCraftEntities.MUFFHORN.get(), MuffhornEntity.createAttributes().build());
    }

    /*
    //Spawning
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {

    } */

}
