package net.sievert.jolcraft.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.attribute.JolCraftAttributes;
import net.sievert.jolcraft.entity.client.model.animal.MuffhornModel;
import net.sievert.jolcraft.entity.client.model.blockentity.StrongboxModel;
import net.sievert.jolcraft.entity.client.model.dwarf.*;
import net.sievert.jolcraft.entity.client.model.object.RadiantModel;
import net.sievert.jolcraft.entity.custom.animal.MuffhornEntity;
import net.sievert.jolcraft.entity.custom.dwarf.*;

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
        event.registerLayerDefinition(DwarfArtisanModel.LAYER_LOCATION, DwarfArtisanModel::createBodyLayer);
        event.registerLayerDefinition(DwarfExplorerModel.LAYER_LOCATION, DwarfExplorerModel::createBodyLayer);
        event.registerLayerDefinition(DwarfMinerModel.LAYER_LOCATION, DwarfMinerModel::createBodyLayer);

        //Animals
        event.registerLayerDefinition(MuffhornModel.LAYER_LOCATION, MuffhornModel::createBodyLayer);
        event.registerLayerDefinition(
                MuffhornModel.BABY_LAYER_LOCATION,
                () -> MuffhornModel.createBodyLayer().apply(MuffhornModel.BABY_TRANSFORMER)
        );

        //Objects
        event.registerLayerDefinition(RadiantModel.LAYER_LOCATION, RadiantModel::createBodyLayer);

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
        event.put(JolCraftEntities.DWARF_ARTISAN.get(), DwarfArtisanEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_EXPLORER.get(), DwarfExplorerEntity.createAttributes().build());
        event.put(JolCraftEntities.DWARF_MINER.get(), DwarfMinerEntity.createAttributes().build());

        //Animals
        event.put(JolCraftEntities.MUFFHORN.get(), MuffhornEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, JolCraftAttributes.XP_BOOST);
        event.add(EntityType.PLAYER, JolCraftAttributes.SLOW_RESIST);
        event.add(EntityType.PLAYER, JolCraftAttributes.EXTRA_CROP);
        event.add(EntityType.PLAYER, JolCraftAttributes.EXTRA_CHEST_LOOT);
        event.add(EntityType.PLAYER, JolCraftAttributes.RADIANT);
        event.add(EntityType.PLAYER, JolCraftAttributes.ARMOR_UNBREAKING);
        event.add(EntityType.PLAYER, JolCraftAttributes.MAGIC_RESISTANCE);
        event.add(EntityType.PLAYER, JolCraftAttributes.ARMOR_INCREASE);
        event.add(EntityType.PLAYER, JolCraftAttributes.ATTACK_DAMAGE_INCREASE);
        event.add(EntityType.PLAYER, JolCraftAttributes.MOVEMENT_SPEED_BOOST_DAY);
        event.add(EntityType.PLAYER, JolCraftAttributes.MOVEMENT_SPEED_BOOST_NIGHT);
    }





}
