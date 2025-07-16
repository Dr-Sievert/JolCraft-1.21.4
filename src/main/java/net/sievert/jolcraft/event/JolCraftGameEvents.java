package net.sievert.jolcraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.attachment.custom.block.Hearth;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;
import net.sievert.jolcraft.block.custom.FermentingStage;
import net.sievert.jolcraft.entity.custom.dwarf.AbstractDwarfEntity;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundAncientLanguagePacket;
import net.sievert.jolcraft.network.packet.ClientboundEndorsementsPacket;
import net.sievert.jolcraft.network.packet.ClientboundLanguagePacket;
import net.sievert.jolcraft.network.packet.ClientboundReputationPacket;
import net.sievert.jolcraft.item.potion.JolCraftPotions;
import net.sievert.jolcraft.sound.JolCraftSoundHelper;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.JolCraftTags;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.item.custom.SpannerItem;
import net.sievert.jolcraft.util.attachment.AncientDwarvenLanguageHelper;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.attachment.DwarvenReputationHelper;
import net.sievert.jolcraft.util.random.JolCraftAnvilHelper;
import net.sievert.jolcraft.util.random.SalvageLootHelper;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.effect.JolCraftEffects;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class JolCraftGameEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        // Sync normal Dwarvish language
        boolean knowsLang = DwarvenLanguageHelper.knowsDwarvishServerBypassCreative(serverPlayer);
        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundLanguagePacket(knowsLang));

        // Sync Ancient Dwarvish language
        boolean knowsAncient = AncientDwarvenLanguageHelper.knowsAncientDwarvishServerBypassCreative(serverPlayer);
        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundAncientLanguagePacket(knowsAncient));

        // Sync reputation tier
        int tier = DwarvenReputationHelper.getTierServerBypassCreative(serverPlayer);
        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundReputationPacket(tier));

        // Sync endorsements
        Set<ResourceLocation> endorsements = DwarvenReputationHelper.getAllEndorsementsServerBypassCreative(serverPlayer);
        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundEndorsementsPacket(endorsements));
    }


    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.WATER, JolCraftItems.DEEPMARROW_DUST.get(), JolCraftPotions.ANCIENT_MEMORY);
        builder.addMix(JolCraftPotions.ANCIENT_MEMORY, Items.REDSTONE, JolCraftPotions.LONG_ANCIENT_MEMORY);

        builder.addMix(Potions.AWKWARD, JolCraftBlocks.DUSKCAP.asItem(), JolCraftPotions.LOCKPICKING);
        builder.addMix(JolCraftPotions.LOCKPICKING, Items.REDSTONE, JolCraftPotions.LONG_LOCKPICKING);
        builder.addMix(JolCraftPotions.LOCKPICKING, Items.GLOWSTONE_DUST, JolCraftPotions.STRONG_LOCKPICKING);

        builder.addMix(Potions.AWKWARD, JolCraftItems.EARTHBLOOD_DUST.asItem(), JolCraftPotions.DWARVEN_HASTE);
        builder.addMix(JolCraftPotions.DWARVEN_HASTE, Items.REDSTONE, JolCraftPotions.LONG_DWARVEN_HASTE);
        builder.addMix(JolCraftPotions.DWARVEN_HASTE, Items.GLOWSTONE_DUST, JolCraftPotions.STRONG_DWARVEN_HASTE);


    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        ItemStack stack = event.getItemStack();

        //Cooldown gate
        if (player.getCooldowns().isOnCooldown(stack)) {
            player.displayClientMessage(
                    Component.translatable("tooltip.jolcraft.crate.cooldown").withStyle(ChatFormatting.GRAY),
                    true
            );
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        // Dwarf logic
        if (target instanceof AbstractDwarfEntity dwarf && !dwarf.isBaby()  && dwarf.canTrade()) {

            // Restock Crate
            if (stack.is(JolCraftItems.RESTOCK_CRATE.get())) {

                //Prevent clientside crash
                if (player.level().isClientSide) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                //Needs to actually have offers
                if (dwarf.getOffers().isEmpty()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.crate.no_offers_dwarf").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playDwarfNo(dwarf);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                boolean needsRestock = dwarf.getOffers().stream().anyMatch(MerchantOffer::isOutOfStock);

                if (!needsRestock && !dwarf.hasRandomTrades()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.restock_crate.no_need").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playDwarfNo(dwarf);
                } else {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.restock_crate.success").withStyle(ChatFormatting.GREEN),
                            true
                    );
                    dwarf.crateRestock();
                    JolCraftSoundHelper.playDwarfYes(dwarf);
                    if (!player.isCreative()) stack.shrink(1);
                    player.getCooldowns().addCooldown(stack, 60);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }

            // Reroll Crate
            if (stack.is(JolCraftItems.REROLL_CRATE.get())) {

                //Prevent clientside crash
                if (player.level().isClientSide) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                //Needs to actually have offers
                if (dwarf.getOffers().isEmpty()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.crate.no_offers_dwarf").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playDwarfNo(dwarf);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                if (!dwarf.canReroll()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.reroll_crate.fail").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playDwarfNo(dwarf);
                } else {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.reroll_crate.success").withStyle(ChatFormatting.GREEN),
                            true
                    );
                    dwarf.rerollTrades();
                    JolCraftSoundHelper.playDwarfYes(dwarf);
                    if (!player.isCreative()) stack.shrink(1);
                    player.getCooldowns().addCooldown(stack, 60);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }
        }

        // Villager logic
        if (target instanceof Villager villager) {


            // Restock Crate
            if (stack.is(JolCraftItems.RESTOCK_CRATE.get()) && !villager.isBaby() && villager.canRestock()) {

                //Prevent clientside crash
                if (player.level().isClientSide) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                //Needs to actually have offers
                if (villager.getOffers().isEmpty()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.crate.no_offers_villager").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playVillagerNo(villager);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                boolean needsRestock = villager.getOffers().stream().anyMatch(MerchantOffer::isOutOfStock);

                if (!needsRestock) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.restock_crate.no_need").withStyle(ChatFormatting.GRAY),
                            true
                    );
                    JolCraftSoundHelper.playVillagerNo(villager);
                } else {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.restock_crate.success").withStyle(ChatFormatting.GREEN),
                            true
                    );
                    JolCraftSoundHelper.playVillagerFisherman(villager);
                    JolCraftSoundHelper.playVillagerYes(villager);
                    villager.restock();
                    if (!player.isCreative()) stack.shrink(1);
                    player.getCooldowns().addCooldown(stack, 60);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }

            // Reroll Crate
            if (stack.is(JolCraftItems.REROLL_CRATE.get()) && !villager.isBaby()) {

                //Prevent clientside crash
                if (player.level().isClientSide) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                //Needs to actually have offers
                if (villager.getOffers().isEmpty()) {
                    player.displayClientMessage(
                            Component.translatable("tooltip.jolcraft.crate.no_offers_villager").withStyle(ChatFormatting.RED),
                            true
                    );
                    JolCraftSoundHelper.playVillagerNo(villager);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                VillagerData data = villager.getVillagerData();
                int currentLevel = data.getLevel();

                MerchantOffers accumulated = new MerchantOffers();

                for (int level = 1; level <= currentLevel; level++) {
                    villager.setVillagerData(data.setLevel(level));
                    villager.setOffers(null); // clear to force repopulation
                    MerchantOffers thisLevelOffers = villager.getOffers();

                    for (MerchantOffer offer : thisLevelOffers) {
                        accumulated.add(offer);
                    }
                }

                villager.setOffers(accumulated);
                villager.setVillagerData(data.setLevel(currentLevel)); // restore

                JolCraftSoundHelper.playVillagerFisherman(villager);
                JolCraftSoundHelper.playVillagerYes(villager);
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.reroll_crate.success").withStyle(ChatFormatting.GREEN),
                        true
                );
                if (!player.isCreative()) stack.shrink(1);
                player.getCooldowns().addCooldown(stack, 60);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }

        }

    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        var level = event.getLevel();
        var pos = event.getPos();
        var state = level.getBlockState(pos);
        var mainHandStack = player.getMainHandItem();

        // Only allow clicking on the top face
        if (mainHandStack.is(Items.ROTTEN_FLESH)) {
            BlockPos above = pos.above();

            boolean onLog = (event.getFace() == Direction.UP
                    && state.is(BlockTags.LOGS)
                    && state.hasProperty(BlockStateProperties.AXIS)
                    && state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y);

            boolean onSoil = (event.getFace() == Direction.UP
                    && (state.is(JolCraftBlocks.VERDANT_SOIL.get())));

            boolean canPlant = onLog || onSoil;

            // Must be air above
            if (canPlant && level.getBlockState(above).isAir()) {
                level.setBlock(above, JolCraftBlocks.FESTERLING_CROP.get().defaultBlockState(), 3);
                level.playSound(null, above, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!player.isCreative()) mainHandStack.shrink(1);

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }

        if (!level.isClientSide()) {
            // Check that block is full water cauldron
            if (state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) == 3) {

                // === SUGAR logic for yeast creation ===
                if (mainHandStack.is(Items.SUGAR)) {
                    BlockState fermentState = JolCraftBlocks.FERMENTING_CAULDRON.get().defaultBlockState()
                            .setValue(FermentingCauldronBlock.LEVEL, 3)
                            .setValue(FermentingCauldronBlock.STAGE, FermentingStage.YEAST_FERMENTING);
                    level.setBlock(pos, fermentState, 3);

                    level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 0.3F, 1.7F);

                    if (!player.isCreative()) mainHandStack.shrink(1);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                // === BARLEY MALT logic for malted stage ===
                if (mainHandStack.is(JolCraftItems.BARLEY_MALT.get())) {
                    BlockState maltedState = JolCraftBlocks.FERMENTING_CAULDRON.get().defaultBlockState()
                            .setValue(FermentingCauldronBlock.LEVEL, 3)
                            .setValue(FermentingCauldronBlock.STAGE, FermentingStage.MALTED);
                    // Optionally set a malted boolean property if you want

                    level.setBlock(pos, maltedState, 3);

                    level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 0.3F, 1.7F);

                    if (!player.isCreative()) mainHandStack.shrink(1);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        Hearth hearth = Hearth.get(player);
        if(hearth.hasLitThisDay()){
            hearth.setLitThisDay(false);
        }
    }

    @SubscribeEvent
    public static void onInvulnerabilityCheck(EntityInvulnerabilityCheckEvent event) {
        if (event.getEntity() instanceof DwarfGuardEntity dwarf &&
                event.getSource().getEntity() instanceof Monster &&
                dwarf.isBlockCooldownReady()) {
            dwarf.markForBlocking();
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public static void registerCustomTrades(final VillagerTradesEvent event) {
       if(event.getType() == VillagerProfession.LIBRARIAN) {
           Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

           trades.get(5).add((pTrader, pRandom) -> {
               int baseCost = 32 + pRandom.nextInt(33); // Random between 32 and 64
               return new MerchantOffer(
                       new ItemCost(Items.EMERALD, baseCost),
                       new ItemStack(JolCraftItems.DWARVEN_LEXICON.get(), 1),
                       1, 1, 0.05f
               );
           });
       }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack main = event.getItemStack();
        ItemStack offhand = player.getOffhandItem();

        if (!(offhand.getItem() instanceof SpannerItem)) return;
        if (!main.is(JolCraftTags.Items.GLOBAL_SCRAP)) return;

        if (!level.isClientSide) {
            List<ItemStack> loot = SalvageLootHelper.generateSalvageLoot(main);
            loot.forEach(stack -> level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(
                    level,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    stack
            )));

            main.shrink(1); // consume the scrap
            offhand.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            player.swing(InteractionHand.OFF_HAND, true);
            level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.5F);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        JolCraftCriteriaTriggers.HAS_ADVANCEMENT.trigger(player, event.getAdvancement().id());
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        // Check for your custom effect (replace 'DWARVEN_HASTE' with your actual effect)
        if (player.hasEffect(JolCraftEffects.DWARVEN_HASTE)) {
            MobEffectInstance effect = player.getEffect(JolCraftEffects.DWARVEN_HASTE);
            int amplifier = effect.getAmplifier();

            // +20% per level, like vanilla Haste
            float originalSpeed = event.getOriginalSpeed();
            float newSpeed = originalSpeed * (1.0F + 0.2F * (amplifier + 1));
            event.setNewSpeed(newSpeed);
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        String rename = event.getName();

        if (!left.isEmpty() && left.is(JolCraftTags.Items.LEGENDARY_ITEMS)) {
            var vanilla = JolCraftAnvilHelper.vanillaResult(left, right, rename, event.getPlayer());
            ItemStack result = vanilla.result();

            if (!result.isEmpty()) {
                // Determine the name to show: use rename, else use default name (but filter it, for safety)
                String baseName;
                if (rename != null && !rename.isEmpty()) {
                    baseName = net.minecraft.util.StringUtil.filterText(rename);
                } else {
                    baseName = left.getHoverName().getString();
                }

                // Remove any prior names to avoid conflicts
                result.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
                result.remove(net.minecraft.core.component.DataComponents.ITEM_NAME);

                // Always set gold name, even if unchanged!
                result.set(net.minecraft.core.component.DataComponents.ITEM_NAME,
                        net.minecraft.network.chat.Component.literal(baseName).withStyle(net.minecraft.ChatFormatting.GOLD));
            }

            event.setOutput(result);
            event.setCost(vanilla.cost());
            event.setMaterialCost(vanilla.materialCost());
        }

        if (!left.isEmpty() && left.is(JolCraftTags.Items.MITHRIL_ITEMS)) {
            var vanilla = JolCraftAnvilHelper.vanillaResult(left, right, rename, event.getPlayer());
            ItemStack result = vanilla.result();

            if (!result.isEmpty()) {
                // Determine the name to show: use rename, else use default name (but filter it, for safety)
                String baseName;
                if (rename != null && !rename.isEmpty()) {
                    baseName = net.minecraft.util.StringUtil.filterText(rename);
                } else {
                    baseName = left.getHoverName().getString();
                }

                // Remove any prior names to avoid conflicts
                result.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
                result.remove(net.minecraft.core.component.DataComponents.ITEM_NAME);

                // Always set gold name, even if unchanged!
                result.set(net.minecraft.core.component.DataComponents.ITEM_NAME,
                        net.minecraft.network.chat.Component.literal(baseName).withStyle(ChatFormatting.AQUA));
            }

            event.setOutput(result);
            event.setCost(vanilla.cost());
            event.setMaterialCost(vanilla.materialCost());
        }
    }


    @SubscribeEvent
    public static void onEnchantItem(PlayerEnchantItemEvent event) {
        ItemStack stack = event.getEnchantedItem();

        // Legendary naming logic
        if (!stack.isEmpty() && stack.is(JolCraftTags.Items.LEGENDARY_ITEMS)) {
            // Always set gold name (you may want to use base name logic as in your anvil event)
            String baseName = stack.getHoverName().getString();
            stack.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
            stack.remove(net.minecraft.core.component.DataComponents.ITEM_NAME);
            stack.set(net.minecraft.core.component.DataComponents.ITEM_NAME,
                    Component.literal(baseName).withStyle(ChatFormatting.GOLD));
        }

        // Mithril naming logic
        if (!stack.isEmpty() && stack.is(JolCraftTags.Items.MITHRIL_ITEMS)) {
            String baseName = stack.getHoverName().getString();
            stack.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
            stack.remove(net.minecraft.core.component.DataComponents.ITEM_NAME);
            stack.set(net.minecraft.core.component.DataComponents.ITEM_NAME,
                    Component.literal(baseName).withStyle(ChatFormatting.AQUA));
        }
    }






}
