package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.data.BountyData;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.BountyGenerator;
import net.sievert.jolcraft.util.BountyReward;
import net.sievert.jolcraft.util.RandomAdapter;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DwarfMerchantEntity extends AbstractDwarfEntity {

    public DwarfMerchantEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.GOLD_COIN.get()));
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfMerchantEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 0.0);

    }

    //Behavior
    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_SIGNED.get());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FirePanicGoal(this, 1.3));
        this.targetSelector.addGoal(1, new DwarfNonPlayerAlertGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(3, new TradeWithPlayerGoal(this));
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

    private int bountyCrateTicks = 0;
    private int bountyTurnInTicks = 0;
    private Player bountyPlayer;

    public ItemStack getBountyCrateItem() {
        return new ItemStack(JolCraftItems.BOUNTY_CRATE.get());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // 🔄 Block interactions while the dwarf is processing a bounty action
        if (this.isPerformingAction()) {
            return InteractionResult.FAIL; // ❌ Block other interactions if an action is in progress
        }

        // 🎯 Handle bounty crate turn-in
        if (itemstack.is(JolCraftItems.BOUNTY_CRATE.get())) {
            // 🛑 Block if bounty ticks are already going (similar to contract signing)
            if (this.bountyTurnInTicks > 0) {
                player.displayClientMessage(Component.translatable("tooltip.jolcraft.bounty_crate.processing").withStyle(ChatFormatting.GRAY), true);
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.FAIL; // ❌ Block if a bounty action is already in progress
            }

            Boolean complete = itemstack.get(JolCraftDataComponents.BOUNTY_COMPLETE.get());
            if (complete == null || !complete) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
                player.displayClientMessage(Component.translatable("tooltip.jolcraft.bounty_crate.not_complete").withStyle(ChatFormatting.GRAY), true);
                return InteractionResult.SUCCESS;
            }

            // Set performingAction to true while processing the bounty
            this.setPerformingAction(true);

            // Process the bounty crate
            this.setNoAi(true);
            this.previousMainHandItem = itemstack.copy();
            this.usePlayerItem(player, hand, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copy());
            this.bountyTurnInTicks = 40;
            this.bountyPlayer = player;

            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.2F);

            // Set performingAction to false after processing the action
            this.setPerformingAction(false);

            return InteractionResult.SUCCESS;
        }

        // 📦 Handle bounty note submission
        if (itemstack.is(JolCraftItems.BOUNTY.get())) {
            // 🛑 Block if bounty ticks are already going (similar to contract signing)
            if (this.bountyCrateTicks > 0) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.FAIL; // ❌ Block if a bounty action is already in progress
            }

            // Set performingAction to true while processing the bounty note
            this.setPerformingAction(true);

            // Process the bounty note
            this.setNoAi(true);
            this.previousMainHandItem = itemstack.copy();
            this.usePlayerItem(player, hand, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(JolCraftItems.BOUNTY.get()));
            this.bountyCrateTicks = 40;
            this.bountyPlayer = player;

            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            // Set performingAction to false after processing the action
            this.setPerformingAction(false);

            return InteractionResult.SUCCESS;
        }

        // Call parent method to handle any other interactions (e.g., contracts, trading)
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Check if the Merchant can level up and if the timer is expired
        if (this.shouldIncreaseLevel() && this.updateMerchantTimer <= 0) {
            // Check if the Merchant has enough XP to level up
            if (this.shouldIncreaseLevel()) {
                // Increase the Merchant's level if XP is enough
                this.increaseMerchantCareer();
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.4F);
                // Set the delay to 40 ticks (similar to vanilla)
                this.updateMerchantTimer = 40; // Reset the timer after leveling up
            }
        } else if (this.updateMerchantTimer > 0) {
            // Countdown the timer only if the Merchant is not performing an action or trading
            --this.updateMerchantTimer;
        }

        if (this.bountyTurnInTicks > 0) {
            if (this.bountyTurnInTicks == 25) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_FISHERMAN, SoundSource.NEUTRAL, 1.0F, 1.2F);
            } else if (this.bountyTurnInTicks == 15) {
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.2F);
            }
            --this.bountyTurnInTicks;

            if (this.bountyTurnInTicks == 0 && !this.level().isClientSide && this.bountyPlayer != null) {
                ItemStack saved = this.previousMainHandItem;

                if (saved.is(JolCraftItems.BOUNTY_CRATE.get()) &&
                        saved.has(JolCraftDataComponents.BOUNTY_COMPLETE.get()) &&
                        saved.get(JolCraftDataComponents.BOUNTY_COMPLETE.get())) {

                    BountyData data = saved.get(JolCraftDataComponents.BOUNTY_DATA.get());
                    if (data != null) {
                        Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                        Vec3 target = this.bountyPlayer.position().add(0.0, this.bountyPlayer.getBbHeight() * 0.5, 0.0);
                        Vec3 velocity = target.subtract(start).normalize().scale(0.4);

                        ItemStack reward = BountyReward.getReward(data, this.getRandom());
                        ItemEntity thrownReward = new ItemEntity(this.level(), start.x, start.y, start.z, reward);
                        thrownReward.setDeltaMovement(velocity);
                        thrownReward.setPickUpDelay(10);
                        this.level().addFreshEntity(thrownReward);

                        int xp = switch (data.tier()) {
                            case 1 -> 10;
                            case 2 -> 35;
                            case 3 -> 50;
                            case 4 -> 65;
                            default -> 0;
                        };
                        this.dwarfXp += xp;

                        this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 1.0, this.getZ(), 3 + this.getRandom().nextInt(3)));
                        this.level().playSound(null, this.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8F, 1.2F);
                        this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.7F);
                    }

                    // Only restock bounty trades, not general offers
                    this.restockBountiesOnly();

                    this.bountyPlayer = null;
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    this.setNoAi(false);
                    this.previousMainHandItem = ItemStack.EMPTY;
                }
            }
        }

        if (this.bountyCrateTicks > 0) {
            if (this.bountyCrateTicks == 25) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.NEUTRAL, 1.0F, 1.2F);
            } else if (this.bountyCrateTicks == 15) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_FISHERMAN, SoundSource.NEUTRAL, 1.0F, 1.2F);
            }
            --this.bountyCrateTicks;

            if (this.bountyCrateTicks == 0 && !this.level().isClientSide && this.bountyPlayer != null) {
                ItemStack saved = this.previousMainHandItem;

                if (saved.is(JolCraftItems.BOUNTY.get())) {
                    Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                    Vec3 target = this.bountyPlayer.position().add(0.0, this.bountyPlayer.getBbHeight() * 0.5, 0.0);
                    Vec3 velocity = target.subtract(start).normalize().scale(0.4);

                    ItemStack crate = this.getBountyCrateItem();

                    // Determine the merchant's current tier
                    int merchantTier = this.getVillagerData().getLevel(); // Implement this method or field to return 1-5

                    // Generate bounty data for the merchant's tier
                    crate.set(JolCraftDataComponents.BOUNTY_DATA.get(),
                            BountyGenerator.generate(new RandomAdapter(this.getRandom()), merchantTier));

                    ItemEntity thrown = new ItemEntity(this.level(), start.x, start.y, start.z, crate);
                    thrown.setDeltaMovement(velocity);
                    thrown.setPickUpDelay(10);
                    this.level().addFreshEntity(thrown);
                    this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F);
                }

                this.bountyPlayer = null;
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.setNoAi(false);
                this.previousMainHandItem = ItemStack.EMPTY;
            }
        }

    }

    //Particles
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();

            if (this.bountyTurnInTicks > 0 && this.bountyTurnInTicks <= 10) {
                this.spawnColoredParticles(1.0F, 0.84F, 0.0F, 0.5F, 10, 1.0D);

                if (this.bountyTurnInTicks == 10) {
                    this.level().playLocalSound(
                            this.getX(),
                            this.getY() + 1.0D,
                            this.getZ(),
                            SoundEvents.FIREWORK_ROCKET_LARGE_BLAST,
                            SoundSource.NEUTRAL,
                            1.0F,
                            1.2F,
                            false
                    );
                }
            }
        }
    }

    //Trades
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> BOUNTY_TRADES = toIntMap(
            ImmutableMap.of(
                    //Novice
                    1, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.BountyItemForItem(
                                    JolCraftItems.PARCHMENT.get(), 1,
                                    JolCraftItems.BOUNTY.get(), 1,
                                    1, 0,
                                    1 // bountyTier = 1 for Novice
                            )
                    },

                    //Apprentice
                    2, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.BountyItemForItem(
                                    JolCraftItems.PARCHMENT.get(), 1,
                                    JolCraftItems.BOUNTY.get(), 1,
                                    1, 0,
                                    2
                            ),
                    },

                    //Journeyman
                    3, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.BountyItemForItem(
                                    JolCraftItems.PARCHMENT.get(), 1,
                                    JolCraftItems.BOUNTY.get(), 1,
                                    1, 0,
                                    3
                            )
                    },

                    //Expert
                    4, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.BountyItemForItem(
                                    JolCraftItems.PARCHMENT.get(), 1,
                                    JolCraftItems.BOUNTY.get(), 1,
                                    1, 0,
                                    4
                            )
                    },

                    //Master
                    5, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.BountyItemForItem(
                                    JolCraftItems.PARCHMENT.get(), 1,
                                    JolCraftItems.BOUNTY.get(), 1,
                                    1, 0,
                                    5
                            )
                    }
            )
    );

    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> GENERAL_TRADES = createGeneralTrades(RandomSource.create());
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> createGeneralTrades(RandomSource random) {
        return toIntMap(ImmutableMap.of(

                // Novice
                1, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(Items.TORCH, Mth.nextInt(random, 1, 2), 12, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.COAL, Mth.nextInt(random, 1, 2), 5, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.FLINT, Mth.nextInt(random, 1, 2), 5, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.COPPER_INGOT, Mth.nextInt(random, 1, 2), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.COBBLED_DEEPSLATE, Mth.nextInt(random, 1, 2), 12, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.IRON_NUGGET, Mth.nextInt(random, 1, 2), 12, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.BRICK, Mth.nextInt(random, 1, 2), 4, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.STRING, Mth.nextInt(random, 1, 2), 3, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.DEEPSLATE_MUG.get(), Mth.nextInt(random, 1, 2), 3, 3, 1)
                },

                // Apprentice
                2, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(Items.IRON_INGOT, Mth.nextInt(random, 2, 3), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.LAPIS_LAZULI, Mth.nextInt(random, 1, 2), 6, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.REDSTONE, Mth.nextInt(random, 1, 2), 6, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.FEATHER, Mth.nextInt(random, 1, 2), 3, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.LEATHER, Mth.nextInt(random, 1, 2), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.WHITE_WOOL, Mth.nextInt(random, 1, 2), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.PARCHMENT.get(), Mth.nextInt(random, 1, 2), 3, 3, 1)
                },

                // Journeyman
                3, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(Items.GOLD_INGOT, Mth.nextInt(random, 5, 7), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.EMERALD, Mth.nextInt(random, 2, 4), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.AMETHYST_SHARD, Mth.nextInt(random, 1, 2), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.BLAZE_POWDER, Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.SPIDER_EYE, Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.GUNPOWDER, Mth.nextInt(random, 1, 2), 2, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.BONE, Mth.nextInt(random, 1, 2), 3, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.INK_SAC, Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.QUILL_EMPTY.get(), Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), Mth.nextInt(random, 1, 2), 1, 3, 1)
                },

                // Expert
                4, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(Items.GOLDEN_APPLE, Mth.nextInt(random, 4, 6), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.BOOK, Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.CAULDRON, Mth.nextInt(random, 10, 14), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.ITEM_FRAME, Mth.nextInt(random, 1, 2), 1, 3, 1),
                        new JolCraftDwarfTrades.ItemsForGold(Items.ENDER_PEARL, Mth.nextInt(random, 2, 4), 1, 3, 1)
                },

                // Master
                5, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.MITHRIL_SALVAGE.get(), Mth.nextInt(random, 1, 2), 1, 1, 1)
                }
        ));
    }

    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> GEM_TRADES = toIntMap(
            ImmutableMap.of(

                    //Master
                    5, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.AEGISCORE.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.ASHFANG.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.DEEPMARROW.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.EARTHBLOOD.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.EMBERGLASS.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.FROSTVEIN.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.GRIMSTONE.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.IRONHEART.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.LUMIERE.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.MOONSHARD.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.RUSTAGATE.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.SKYBURROW.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.SUNGLEAM.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.VERDANITE.get(), 64, 1, 1, 1),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.WOECRYSTAL.get(), 64, 1, 1, 1),
                    }
            )
    );

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {

        return new Int2ObjectOpenHashMap<>(pMap);
    }

    @Override
    protected void updateTrades() {
        int level = this.getVillagerData().getLevel();
        MerchantOffers offers = this.getOffers();

        // Add the 1 bounty trade for this level
        VillagerTrades.ItemListing[] bounty = BOUNTY_TRADES.get(level);
        if (bounty != null) {
            this.addOffersFromItemListings(offers, bounty, 1);
        }

        // Add 2 general trades for this level (like torches, parchment, flint, etc.)
        VillagerTrades.ItemListing[] general = GENERAL_TRADES.get(level);
        if (general != null) {
            List<VillagerTrades.ItemListing> shuffled = new ArrayList<>(List.of(general));
            Collections.shuffle(shuffled, new Random(this.random.nextLong()));
            for (int i = 0; i < Math.min(2, shuffled.size()); i++) {
                MerchantOffer offer = shuffled.get(i).getOffer(this, this.random);
                if (offer != null) {
                    offers.add(offer);
                }
            }
        }

        // At Master level, add 1 gem trade as a high-tier extra
        if (level == 5) {
            VillagerTrades.ItemListing[] gemTrades = GEM_TRADES.get(5);
            if (gemTrades != null && gemTrades.length > 0) {
                MerchantOffer offer = gemTrades[this.random.nextInt(gemTrades.length)].getOffer(this, this.random);
                if (offer != null) {
                    offers.add(offer);
                }
            }
        }
    }

    @Override
    public void restock() {
        if (this.level().isClientSide) return;

        this.getOffers().clear(); // Remove all old trades

        int level = this.getVillagerData().getLevel();

        // 🟨 Bounty (always one)
        for (int i = 1; i <= level; i++) {
            VillagerTrades.ItemListing[] bountyListings = BOUNTY_TRADES.get(i);
            if (bountyListings != null) {
                this.addOffersFromItemListings(this.getOffers(), bountyListings, bountyListings.length);
            }
        }

        // 🟩 General (2 random trades per level tier from 1 up to current level)
        Int2ObjectMap<VillagerTrades.ItemListing[]> freshGeneralTrades = createGeneralTrades(this.random);

        for (int i = 1; i <= level; i++) {
            VillagerTrades.ItemListing[] generalPool = freshGeneralTrades.get(i);
            if (generalPool != null) {
                List<VillagerTrades.ItemListing> shuffled = new ArrayList<>(List.of(generalPool));
                Collections.shuffle(shuffled, new Random(this.random.nextLong()));
                int added = 0;
                for (VillagerTrades.ItemListing trade : shuffled) {
                    if (added >= 2) break;
                    MerchantOffer offer = trade.getOffer(this, this.random);
                    if (offer != null) {
                        this.getOffers().add(offer);
                        added++;
                    }
                }
            }
        }


        // 💎 Gem trades (master only)
        if (level == 5) {
            VillagerTrades.ItemListing[] gemTrades = GEM_TRADES.get(5);
            if (gemTrades != null) {
                List<VillagerTrades.ItemListing> shuffled = new ArrayList<>(List.of(gemTrades));
                Collections.shuffle(shuffled, new Random(this.random.nextLong()));
                int added = 0;
                for (VillagerTrades.ItemListing trade : shuffled) {
                    if (added >= 1) break;
                    MerchantOffer offer = trade.getOffer(this, this.random);
                    if (offer != null) {
                        this.getOffers().add(offer);
                        added++;
                    }
                }
            }
        }

        this.lastRestockGameTime = this.level().getGameTime();
        this.playSound(SoundEvents.VILLAGER_WORK_FISHERMAN, 1.0F, 1.2F);
    }


    public void restockBountiesOnly() {
        if (this.getOffers().isEmpty()) return;

        boolean needsRestock = false;
        for (MerchantOffer offer : this.getOffers()) {
            // Refresh only bounty-related trades
            if (offer.getResult().is(JolCraftItems.BOUNTY.get()) && offer.needsRestock()) {
                offer.resetUses();
                needsRestock = true;
            }
        }

        if (needsRestock) {
            this.playSound(SoundEvents.VILLAGER_WORK_FISHERMAN, 1.0F, 1.0F);
        }
    }

    //Sound
    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    //Spawning
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
        this.setLeftHanded(false);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }




}
