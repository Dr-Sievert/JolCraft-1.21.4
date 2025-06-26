package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.capability.DwarvenReputationImpl;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.client.data.MyClientReputationData;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundSyncEndorsementsPacket;
import net.sievert.jolcraft.network.packet.ClientboundSyncReputationPacket;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.DwarvenReputationLevels;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

public class DwarfGuildmasterEntity extends AbstractDwarfEntity {

    public DwarfGuildmasterEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.CONTRACT_SIGNED.get()));
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30d)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16d)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    //Behavior
    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_GUILDMASTER.get());
    }

    @Override
    public boolean neverEndorse(Player player) {
        return true;
    }

    @Override
    public float getVoicePitch() {
        return 0.8F; // deeper voice for guildmaster
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FirePanicGoal(this, 1.3));
        this.targetSelector.addGoal(2, new DwarfNonPlayerAlertGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(3, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(4, new DwarfUseItemGoal<>(this, PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), SoundEvents.PLAYER_BURP, mob -> mob.getHealth() < mob.getMaxHealth(), 300));
        this.goalSelector.addGoal(4, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(5, new DwarfBreedGoal(this, 1.0, AbstractDwarfEntity.class));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, stack -> stack.is(JolCraftItems.GOLD_COIN), false));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new MoveToBlockGoal(this, 0.8, 8) {
            @Override
            protected boolean isValidTarget(LevelReader level, BlockPos pos) {
                return level.getBlockState(pos).is(Blocks.COBBLED_DEEPSLATE);
            }
        });
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean client = this.level().isClientSide;

        // 🧠 Language check - ensures only players who know the Dwarvish language can interact
        InteractionResult langCheck = this.languageCheck(player);
        if (langCheck != InteractionResult.SUCCESS) {
            return langCheck;
        }

        // Sync Guildmaster level with player reputation tier
        DwarvenReputationImpl reptier = player.getData(JolCraftAttachments.DWARVEN_REP.get());
        if (reptier != null) {
            int playerTier = reptier.getTier(); // 0=Stranger, 1=Known Face, ... 4=Blood-Kin
            int targetLevel = Math.min(playerTier + 1, 5); // VillagerData levels: 1..5
            int guildmasterLevel = this.getVillagerData().getLevel();
            while (guildmasterLevel < targetLevel && guildmasterLevel < 5) {
                this.increaseMerchantCareer();
                guildmasterLevel++;
            }
        }

        // Only handle reputation tablets; let all other logic fall through
        if (!itemstack.is(JolCraftTags.Items.REPUTATION_TABLETS)) {
            return super.mobInteract(player, hand);
        }

        // Fetch current tier and endorsement count on both sides
        int currentTier = client
                ? MyClientReputationData.getTier()
                : (player.getData(JolCraftAttachments.DWARVEN_REP.get()) != null
                ? player.getData(JolCraftAttachments.DWARVEN_REP.get()).getTier()
                : 0);

        int endorsementCount = client
                ? MyClientReputationData.endorsementCount()
                : (player.getData(JolCraftAttachments.DWARVEN_REP.get()) != null
                ? player.getData(JolCraftAttachments.DWARVEN_REP.get()).getEndorsementCount()
                : 0);

        // Check for max tier already reached
        if (currentTier >= DwarvenReputationLevels.getMaxTier()) {
            if (client) {
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.reputation.max_tier").withStyle(ChatFormatting.GRAY), true
                );
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        // Check if can advance (using util method)
        if (!DwarvenReputationLevels.canAdvance(currentTier, endorsementCount)) {
            int needed = DwarvenReputationLevels.getThresholdForTier(currentTier);
            if (client) {
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.reputation.not_enough_endorsements", needed, endorsementCount)
                                .withStyle(ChatFormatting.GRAY), true);
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        // Block if busy (performing another action)
        if (this.isPerformingAction()) {
            if (client) {
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.dwarf.busy").withStyle(ChatFormatting.GRAY), true);
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }


        // Paid check (all else valid, just not paid)
        if (!this.isPaid()) {
            if (!client) {
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.dwarf.not_paid").withStyle(ChatFormatting.GRAY), true);
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        // Only accept the correct tablet for the next tier
        Item correctTablet = switch (currentTier) {
            case 0 -> JolCraftItems.REPUTATION_TABLET_0.get();
            case 1 -> JolCraftItems.REPUTATION_TABLET_1.get();
            case 2 -> JolCraftItems.REPUTATION_TABLET_2.get();
            case 3 -> JolCraftItems.REPUTATION_TABLET_3.get();
            default -> null;
        };

        if (correctTablet == null || !itemstack.is(correctTablet)) {
            if (client) {
                player.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.reputation.wrong_tablet").withStyle(ChatFormatting.GRAY), true);
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        // At this point: eligible to level up!
        // Show the signing/endorsement animation, throw tablet back, update, play sound

        // Remove tablet from player and animate
        ItemStack tabletUsed = itemstack.copy();
        this.previousMainHandItem = this.getMainHandItem().copy();
        this.setItemSlot(EquipmentSlot.MAINHAND, tabletUsed.copy());
        this.usePlayerItem(player, hand, itemstack);
        this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

        this.usedItem = tabletUsed; // <-- this is crucial for the lambda!
        beginAction(player, 40, AbstractDwarfEntity.ACTION_REPUTATION_ENDORSEMENT, tabletUsed, previousMainHandItem, () -> {
            this.setInspecting(false);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

            if (!this.level().isClientSide && this.currentActionPlayer != null) {
                // On server: bump the tier, update data, sync packets, throw new tablet
                DwarvenReputationImpl rep = this.currentActionPlayer.getData(JolCraftAttachments.DWARVEN_REP.get());
                if (rep != null && rep.getTier() == currentTier) {
                    rep.setTier(currentTier + 1);
                    if (player instanceof ServerPlayer serverPlayer) {
                        JolCraftCriteriaTriggers.REPUTATION_TIER.trigger(serverPlayer);
                    }

                    // 🎯 Level up Guildmaster
                    int newPlayerTier = rep.getTier();
                    int guildmasterLevel = this.getVillagerData().getLevel();
                    if (guildmasterLevel <= newPlayerTier && guildmasterLevel < 5) {
                        this.increaseMerchantCareer();
                    }

                    // Sync new tier and endorsements to client
                    if (this.currentActionPlayer instanceof ServerPlayer serverPlayer) {
                        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncReputationPacket(rep.getTier()));
                        JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncEndorsementsPacket(rep.getEndorsements()));
                        serverPlayer.displayClientMessage(Component.translatable("tooltip.jolcraft.reputation.level_up").withStyle(ChatFormatting.GOLD), true);
                    }

                    // Create updated tablet item
                    // Select new tablet for the next tier
                    ItemStack updatedTablet = switch (rep.getTier()) {
                        case 1 -> new ItemStack(JolCraftItems.REPUTATION_TABLET_1.get());
                        case 2 -> new ItemStack(JolCraftItems.REPUTATION_TABLET_2.get());
                        case 3 -> new ItemStack(JolCraftItems.REPUTATION_TABLET_3.get());
                        case 4 -> new ItemStack(JolCraftItems.REPUTATION_TABLET_4.get());
                        default -> ItemStack.EMPTY;
                    };
                    updatedTablet.set(JolCraftDataComponents.REP_ENDORSEMENTS.get(), rep.getEndorsementCount());
                    updatedTablet.set(JolCraftDataComponents.REP_TIER.get(), rep.getTier());
                    updatedTablet.set(JolCraftDataComponents.REP_OWNER.get(), this.currentActionPlayer.getName().getString());

                    // Throw the new tablet to the player
                    Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                    Vec3 target = this.currentActionPlayer.position().add(0.0, this.currentActionPlayer.getBbHeight() * 0.5, 0.0);
                    Vec3 velocity = target.subtract(start).normalize().scale(0.4);

                    ItemEntity thrown = new ItemEntity(this.level(), start.x, start.y, start.z, updatedTablet);
                    thrown.setDeltaMovement(velocity);
                    thrown.setPickUpDelay(10);
                    this.level().addFreshEntity(thrown);
                    this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F);

                    this.setItemSlot(EquipmentSlot.MAINHAND, this.previousMainHandItem);
                    this.previousMainHandItem = ItemStack.EMPTY;
                }
            }
        });

        return client ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void tick() {
        super.tick();
        if (ACTION_REPUTATION_ENDORSEMENT.equals(currentActionId)) {
            if (currentActionTicks == 2) {
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.LEVEL_UP.get(), SoundSource.NEUTRAL, 1.2F, 1.0F);
                this.spawnColoredParticles(0.35F, 0.35F, 0.35F, 1.25F, 64, 2.5D);
            }
        }
    }



    //Trades
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_MERCHANT.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_HISTORIAN.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_SCRAPPER.get(), 1, 1, 0, 0.05F)

                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_BREWMASTER.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_GUARD.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_KEEPER.get(), 1, 1, 0, 0.05F)
                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ALCHEMIST.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_EXPLORER.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_MINER.get(), 1, 1, 0, 0.05F)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ARCANIST.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ARTISAN.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_PRIEST.get(), 1, 1, 0, 0.05F)
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_BLACKSMITH.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_CHAMPION.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_SMELTER.get(), 1, 1, 0, 0.05F)
                    }
            )
    );

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {

        return new Int2ObjectOpenHashMap<>(pMap);
    }

    @Override
    protected void updateTrades() {
        int level = this.getVillagerData().getLevel();
        VillagerTrades.ItemListing[] listings = TRADES.get(level);
        if (listings != null) {
            this.addOffersFromItemListings(this.getOffers(), listings, 3); // 2 = max trades for that level
        }
    }


}

