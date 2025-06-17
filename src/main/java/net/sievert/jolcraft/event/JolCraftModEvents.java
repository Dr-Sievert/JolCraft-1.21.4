package net.sievert.jolcraft.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.client.model.DwarfGuardModel;
import net.sievert.jolcraft.entity.client.model.DwarfHistorianModel;
import net.sievert.jolcraft.entity.client.model.DwarfModel;
import net.sievert.jolcraft.entity.client.model.DwarfScrapperModel;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfEntity;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfHistorianEntity;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfScrapperEntity;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class JolCraftModEvents {

   //Register Model Layers

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DwarfModel.LAYER_LOCATION, DwarfModel::createBodyLayer);
        event.registerLayerDefinition(DwarfGuardModel.LAYER_LOCATION, DwarfGuardModel::createBodyLayer);
        event.registerLayerDefinition(DwarfHistorianModel.LAYER_LOCATION, DwarfHistorianModel::createBodyLayer);
        event.registerLayerDefinition(DwarfScrapperModel.LAYER_LOCATION, DwarfScrapperModel::createBodyLayer);
    }

    //Register Attributes

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(JolCraftEntities.DWARF.get(), DwarfEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_GUARD.get(), DwarfGuardEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_HISTORIAN.get(), DwarfHistorianEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_SCRAPPER.get(), DwarfScrapperEntity.createAttributes().build());
    }

    //Spawning
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {

    }







}
