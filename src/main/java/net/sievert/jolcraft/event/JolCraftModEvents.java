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
