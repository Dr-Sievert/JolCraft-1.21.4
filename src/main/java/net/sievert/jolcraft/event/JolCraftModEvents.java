package net.sievert.jolcraft.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.client.dwarf.model.*;
import net.sievert.jolcraft.entity.custom.dwarf.*;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class JolCraftModEvents {

   //Register Model Layers
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DwarfModel.LAYER_LOCATION, DwarfModel::createBodyLayer);
        event.registerLayerDefinition(DwarfGuildmasterModel.LAYER_LOCATION, DwarfGuildmasterModel::createBodyLayer);
        event.registerLayerDefinition(DwarfHistorianModel.LAYER_LOCATION, DwarfHistorianModel::createBodyLayer);
        event.registerLayerDefinition(DwarfMerchantModel.LAYER_LOCATION, DwarfMerchantModel::createBodyLayer);
        event.registerLayerDefinition(DwarfScrapperModel.LAYER_LOCATION, DwarfScrapperModel::createBodyLayer);
        event.registerLayerDefinition(DwarfBrewmasterModel.LAYER_LOCATION, DwarfBrewmasterModel::createBodyLayer);
        event.registerLayerDefinition(DwarfGuardModel.LAYER_LOCATION, DwarfGuardModel::createBodyLayer);
        event.registerLayerDefinition(DwarfKeeperModel.LAYER_LOCATION, DwarfKeeperModel::createBodyLayer);

    }

    //Register Attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(JolCraftEntities.DWARF.get(), DwarfEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_GUILDMASTER.get(), DwarfGuildmasterEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_HISTORIAN.get(), DwarfHistorianEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_MERCHANT.get(), DwarfMerchantEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_SCRAPPER.get(), DwarfScrapperEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_BREWMASTER.get(), DwarfBrewmasterEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_GUARD.get(), DwarfGuardEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_KEEPER.get(), DwarfKeeperEntity.createAttributes().build());

    }

    /*
    //Spawning
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {

    } */

}
