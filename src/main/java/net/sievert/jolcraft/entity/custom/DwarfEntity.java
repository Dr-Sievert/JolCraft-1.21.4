package net.sievert.jolcraft.entity.custom;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class DwarfEntity extends AbstractDwarfEntity {

    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.VILLAGER_DATA);

    @Nullable
    private Player lastTradedPlayer;
    private int dwarfXp;
    private boolean increaseLevelOnUpdate = false;
    private int updateMerchantTimer = 0;
    private static final Logger LOGGER = LogUtils.getLogger();
    private boolean increaseProfessionLevelOnUpdate = false;


    public DwarfEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        //this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TORCH));
        this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
    }

    //EntityDataAccessors

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean assignProfessionWhenSpawned;
    public boolean assignProfessionWhenSpawned() {
        return this.assignProfessionWhenSpawned;
    }


    //Animations

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;


    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 83;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 8;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    //Trades

    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.STICK, 1, 1, 6, 1),
                            new JolCraftDwarfTrades.GoldForItems(Items.TORCH, 1, 4, 4, 1)
                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.BREAD, 1, 1, 5, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.SMITHING_TABLE, 2, 4, 4, 1)
                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), 3, 1, 10, 1),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.QUILL_EMPTY.get(), 2, 10, 4, 1)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.DIAMOND, 1, 1, 10, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.EMERALD, 1, 10, 10, 1)
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.NETHERITE_BLOCK, 1, 1, 5, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.NETHERITE_SCRAP, 1, 3, 5, 1)
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
            this.addOffersFromItemListings(this.getOffers(), listings, 2); // 2 = max trades for that level
        }
    }

    private long lastRestockGameTime = 0L;
    private static final long RESTOCK_INTERVAL_TICKS = 6000L;

    private boolean shouldRestock() {
        return this.level() instanceof ServerLevel serverLevel &&
                serverLevel.getGameTime() >= lastRestockGameTime + RESTOCK_INTERVAL_TICKS;
    }

    public void restock() {
        if (this.getOffers().isEmpty()) return;

        boolean needsRestock = false;

        for (MerchantOffer offer : this.getOffers()) {
            if (offer.needsRestock()) {
                offer.resetUses();
                needsRestock = true;
            }
        }

        if (needsRestock) {
            this.lastRestockGameTime = this.level().getGameTime();
            this.playSound(SoundEvents.VILLAGER_WORK_FISHERMAN, 0.8F, 0.5F);
        }
    }

    @Override
    public int getVillagerXp() {
        return this.dwarfXp;
    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        VillagerData.CODEC
                .encodeStart(NbtOps.INSTANCE, this.getVillagerData())
                .resultOrPartial(LOGGER::error)
                .ifPresent(p_35454_ -> compound.put("VillagerData", p_35454_));
        compound.putInt("Xp", this.dwarfXp);
        if (this.assignProfessionWhenSpawned) {
            compound.putBoolean("AssignProfessionWhenSpawned", true);
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("VillagerData", 10)) {
            VillagerData.CODEC
                    .parse(NbtOps.INSTANCE, compound.get("VillagerData"))
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(p_323354_ -> this.entityData.set(DATA_VILLAGER_DATA, p_323354_));
        }
        if (compound.contains("Xp", 3)) {
            this.dwarfXp = compound.getInt("Xp");
        }
        if (compound.contains("AssignProfessionWhenSpawned")) {
            this.assignProfessionWhenSpawned = compound.getBoolean("AssignProfessionWhenSpawned");
        }
    }

    private boolean shouldIncreaseLevel() {
        int i = this.getVillagerData().getLevel();
        return VillagerData.canLevelUp(i) && this.dwarfXp >= VillagerData.getMaxXpPerLevel(i);
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        int i = 3 + this.random.nextInt(4);
        this.dwarfXp = this.dwarfXp + offer.getXp();
        this.lastTradedPlayer = this.getTradingPlayer();
        if (this.shouldIncreaseLevel()) {
            this.updateMerchantTimer = 40;
            this.increaseProfessionLevelOnUpdate = true;
            i += 5;
        }

        if (offer.shouldRewardExp()) {
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    private static final int[] XP_THRESHOLDS = {0, 10, 50, 100, 150};

    private int getMaxXpPerLevel(int level) {
        if (level < XP_THRESHOLDS.length) {
            return XP_THRESHOLDS[level];
        } else {
            return XP_THRESHOLDS[XP_THRESHOLDS.length - 1]; // or scale further
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
        builder.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    public void setVillagerData(VillagerData p_35437_) {
        VillagerData villagerdata = this.getVillagerData();
        if (villagerdata.getProfession() != p_35437_.getProfession()) {
            this.offers = null;
        }

        this.entityData.set(DATA_VILLAGER_DATA, p_35437_);
    }

    public VillagerData getVillagerData() {
        return this.entityData.get(DATA_VILLAGER_DATA);
    }


    private void resendOffersToTradingPlayer() {
        MerchantOffers merchantoffers = this.getOffers();
        Player player = this.getTradingPlayer();
        if (player != null && !merchantoffers.isEmpty()) {
            player.sendMerchantOffers(
                    player.containerMenu.containerId,
                    merchantoffers,
                    this.getVillagerData().getLevel(),
                    this.getVillagerXp(),
                    this.showProgressBar(),
                    this.canRestock()
            );
        }
    }


    protected void increaseMerchantCareer() {
        int current = this.getVillagerData().getLevel();
        if (VillagerData.canLevelUp(current)) {
            int next = current + 1;
            this.setVillagerData(this.getVillagerData().setLevel(next));
            this.updateTrades();
            this.resendOffersToTradingPlayer();
        }
    }

    @Override
    public void customServerAiStep(ServerLevel level) {
        if (this.assignProfessionWhenSpawned) {
            this.assignProfessionWhenSpawned = false;
        }

        if (!this.isTrading() && this.updateMerchantTimer > 0) {
            this.updateMerchantTimer--;
            if (this.updateMerchantTimer <= 0) {
                if (this.increaseProfessionLevelOnUpdate) {
                    this.increaseMerchantCareer();
                    this.increaseProfessionLevelOnUpdate = false;

                }
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.4F);
            }
        }

        if (this.shouldRestock()) {
            this.restock();
            lastRestockGameTime = this.level().getGameTime(); // Reset timer
        }

        super.customServerAiStep(level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Signing logic
        if (this.signingTicks > 0) {
            this.signingTicks--;

            if (this.signingTicks == 15 || this.signingTicks == 25) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.NEUTRAL, 1.0F, 1.2F);
            }

            if (this.signingTicks == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.setNoAi(false);

                if (!this.level().isClientSide && this.signingPlayer != null) {
                    // Create the contract item
                    ItemStack contract = new ItemStack(JolCraftItems.CONTRACT_SIGNED.get());

                    // Calculate direction from dwarf to player's center
                    Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                    Vec3 target = this.signingPlayer.position().add(0.0, this.signingPlayer.getBbHeight() * 0.5, 0.0);
                    Vec3 velocity = target.subtract(start).normalize().scale(0.4); // tweak speed here

                    // Create and launch item
                    ItemEntity thrown = new ItemEntity(this.level(), start.x, start.y, start.z, contract);
                    thrown.setDeltaMovement(velocity);
                    thrown.setPickUpDelay(10); // small delay to avoid instant pickup
                    this.level().addFreshEntity(thrown);

                    // Play sound effect on throw
                    this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F);
                }
                signingPlayer = null;
            }
        }
    }

    //Goals

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FirePanicGoal(this, 1.3));
        this.targetSelector.addGoal(2, new HurtByNonPlayerTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(3, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(4, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(5, new DwarfBreedGoal(this, 1.0));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, stack -> stack.is(JolCraftItems.GOLD_COIN), false));
        this.goalSelector.addGoal(6, new DwarfFollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new MoveToBlockGoal(this, 0.8, 8) {
            @Override
            protected boolean isValidTarget(LevelReader level, BlockPos pos) {
                return level.getBlockState(pos).is(Blocks.COBBLED_DEEPSLATE); // Or a custom anchor block
            }
        });
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

    //Interaction

    private int signingTicks = 0;
    private Player signingPlayer;

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack playerhand = player.getItemInHand(hand);

        if (!playerhand.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby() && !this.isFood(playerhand)) {


            //Sign contract
            if (playerhand.is(JolCraftItems.CONTRACT_WRITTEN) && !playerhand.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            /* Future achievement?
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            */
                if (this.getMainHandItem().isEmpty() && this.isInLove()) {
                    //Make dwarf stop
                    this.setNoAi(true);

                    // Play accepting sound (optional)
                    this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

                    // Replace player's contract with empty hand
                    this.usePlayerItem(player, hand, playerhand);

                    // Visually equip contract to dwarf's hand
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(JolCraftItems.CONTRACT_WRITTEN.get()));

                    // Set up signing delay
                    this.signingTicks = 40;
                    this.signingPlayer = player;

                    return InteractionResult.SUCCESS;
                }
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
        }

        //Breeding
        if (this.isFood(playerhand)) {
            if(this.isInLove()){
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
            int i = this.getAge();
            if (!this.level().isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, playerhand);
                this.setInLove(player);
                this.playEatingSound();
                return InteractionResult.SUCCESS_SERVER;
            }

            if (this.isBaby()) {
                this.usePlayerItem(player, hand, playerhand);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                this.playEatingSound();
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.CONSUME;
        }

        //Trading
        if (!playerhand.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby() && !playerhand.is(JolCraftItems.CONTRACT_WRITTEN)) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), this.getVillagerData().getLevel());
            }
            return InteractionResult.SUCCESS;
        }

        //If nothing happens play "no" sound
        else {
            if(this.isBaby()){
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.5F);
                return InteractionResult.CONSUME;
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }
    }

    //Attacking

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    //Sounds

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return JolCraftSounds.DWARF_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return JolCraftSounds.DWARF_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return JolCraftSounds.DWARF_DEATH.get();
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return JolCraftSounds.DWARF_YES.get();
    }

    protected SoundEvent getTradeUpdatedSound(boolean isYesSound) {
        return isYesSound ? JolCraftSounds.DWARF_YES.get() : JolCraftSounds.DWARF_NO.get();
    }

}
