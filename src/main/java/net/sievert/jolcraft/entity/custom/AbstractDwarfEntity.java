package net.sievert.jolcraft.entity.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.*;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.ai.goal.DwarfBlockGoal;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.sound.JolCraftSounds;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbstractDwarfEntity extends WanderingTrader {

    public AbstractDwarfEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setLeftHanded(false);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);

    }

    protected int signingTicks = 0;
    protected Player signingPlayer;

    //Animations
    public final AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public boolean hasStartedAttackAnimation = false;
    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public final AnimationState blockAnimationState = new AnimationState();
    public boolean hasStartedBlockAnimation = false;
    public void setBlocking(boolean blocking) {
        this.entityData.set(BLOCKING, blocking);
    }
    public boolean isBlocking() {
        return this.entityData.get(BLOCKING);
    }

    public final AnimationState drinkAnimationState = new AnimationState();
    public boolean hasStartedDrinkAnimation = false;
    public void setDrinking(boolean drinking) {
        this.entityData.set(DRINKING, drinking);
    }
    public boolean isDrinking() {
        return this.entityData.get(DRINKING);
    }

    protected void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 83;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        if (this.isAttacking()) {
            if (!hasStartedAttackAnimation) {
                attackAnimationState.start(this.tickCount);
                hasStartedAttackAnimation = true;
            }
        } else {
            hasStartedAttackAnimation = false;
        }
        if (isBlocking()) {
            if (!hasStartedBlockAnimation) {
                blockAnimationState.start(this.tickCount);
                hasStartedBlockAnimation = true;
            }
        } else {
            hasStartedBlockAnimation = false;
        }
        if (isDrinking()) {
            if (!hasStartedDrinkAnimation) {
                drinkAnimationState.start(this.tickCount);
                hasStartedDrinkAnimation = true;
            }
        } else {
            hasStartedDrinkAnimation = false;
        }
    }

    private Vec3 blockParticlePos = null;
    private int blockParticleTicks = 0;

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
            if (this.hasStartedBlockAnimation) {
                if (blockParticlePos == null) {
                    // Capture particle spawn location at start
                    Vec3 look = this.getLookAngle().normalize();
                    double forwardOffset = 1.0D;
                    double leftOffset = -0.4D;
                    Vec3 left = new Vec3(-look.z, 0, look.x).normalize();

                    double px = this.getX() + look.x * forwardOffset + left.x * leftOffset;
                    double py = this.getY() + 1.2D;
                    double pz = this.getZ() + look.z * forwardOffset + left.z * leftOffset;

                    blockParticlePos = new Vec3(px, py, pz);
                    blockParticleTicks = 10; // spawn for 10 ticks
                }

                if (blockParticleTicks-- > 0 && blockParticlePos != null) {
                    for (int i = 0; i < 5; i++) { // spawn multiple per tick for visual density
                        double scatterRange = 0.15D; // how far they can spread from center

                        double offsetX = blockParticlePos.x + (this.random.nextDouble() - 0.5) * 2.0 * scatterRange;
                        double offsetY = blockParticlePos.y + (this.random.nextDouble() - 0.5) * 2.0 * scatterRange;
                        double offsetZ = blockParticlePos.z + (this.random.nextDouble() - 0.5) * 2.0 * scatterRange;

                        double velocityX = (this.random.nextDouble() - 0.5) * 0.1;
                        double velocityY = (this.random.nextDouble()) * 0.1; // small upward boost
                        double velocityZ = (this.random.nextDouble() - 0.5) * 0.1;

                        DustParticleOptions dust = new DustParticleOptions(-2233622, 0.5F);
                        this.level().addParticle(dust, offsetX, offsetY, offsetZ, velocityX, velocityY, velocityZ);
                    }
                }
            } else {
                // Reset when animation ends
                blockParticlePos = null;
                blockParticleTicks = 0;
            }
        }
    }

    //Behavior
    public boolean canTrade() {
        return false;
    }

    public boolean canSign() {
        return true;
    }

    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_SIGNED.get());
    }

    @Override
    protected void customServerAiStep(ServerLevel p_376777_) {
        if (this.getAge() != 0) {
            this.inLove = 0;
        }
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

        super.customServerAiStep(p_376777_);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        tickBlockCooldown();
        if (this.getAge() != 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            this.inLove--;
            if (this.inLove % 10 == 0) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        }

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

                    // Calculate direction from dwarf to player's center
                    Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                    Vec3 target = this.signingPlayer.position().add(0.0, this.signingPlayer.getBbHeight() * 0.5, 0.0);
                    Vec3 velocity = target.subtract(start).normalize().scale(0.4); // tweak speed here

                    // Create and launch item
                    ItemEntity thrown = new ItemEntity(this.level(), start.x, start.y, start.z, this.getSignedContractItem());
                    thrown.setDeltaMovement(velocity);
                    thrown.setPickUpDelay(10); // small delay to avoid instant pickup
                    this.level().addFreshEntity(thrown);

                    // Play sound effect on throw
                    this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F);

                    //Give back old item
                    this.setItemSlot(EquipmentSlot.MAINHAND, this.previousMainHandItem);
                    this.previousMainHandItem = ItemStack.EMPTY;
                }
                signingPlayer = null;
            }
        }
    }

    //Blocking

    private boolean shouldStartBlocking = false;

    public int blockCooldown = 0;

    public boolean isBlockCooldownReady() {
        return blockCooldown <= 0;
    }

    public void setBlockCooldown(int ticks) {
        this.blockCooldown = ticks;
    }

    public void tickBlockCooldown() {
        if (this.goalSelector.getAvailableGoals().stream().anyMatch(
                goal -> goal.getGoal() instanceof DwarfBlockGoal
        )) {
            if (blockCooldown > 0) blockCooldown--;
        }
    }

    public void markForBlocking() {
        if(!this.isDrinking()){
            this.shouldStartBlocking = true;
        }
    }

    public boolean consumeBlockFlag() {
        if (this.shouldStartBlocking) {
            this.shouldStartBlocking = false;
            return true;
        }
        return false;
    }

    //Breeding
    protected int inLove;
    @Nullable
    protected UUID loveCause;

    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.BREAD);
    }

    protected void playEatingSound() {
        this.playSound(SoundEvents.PLAYER_BURP, 1.0F, this.getVoicePitch());
    }

    public boolean canFallInLove() {
        return this.inLove <= 0;
    }

    public boolean isInLove() {
        return this.inLove > 0;
    }

    public void resetLove() {
        this.inLove = 0;
    }

    public void setInLoveTime(int inLove) {
        this.inLove = inLove;
    }

    public int getInLoveTime() {
        return this.inLove;
    }

    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        int i = stack.getCount();
        UseRemainder useremainder = stack.get(DataComponents.USE_REMAINDER);
        stack.consume(1, player);
        if (useremainder != null) {
            ItemStack itemstack = useremainder.convertIntoRemainder(stack, i, player.hasInfiniteMaterials(), player::handleExtraItemsCreatedOnUse);
            player.setItemInHand(hand, itemstack);
        }
    }

    public void setInLove(@Nullable Player player) {
        this.inLove = 600;
        if (player != null) {
            this.loveCause = player.getUUID();
        }

        this.level().broadcastEntityEvent(this, (byte)18);
    }

    @Nullable
    public ServerPlayer getLoveCause() {
        if (this.loveCause == null) {
            return null;
        } else {
            Player player = this.level().getPlayerByUUID(this.loveCause);
            return player instanceof ServerPlayer ? (ServerPlayer)player : null;
        }
    }

    @Override
    public void handleEntityEvent(byte p_27562_) {
        if (p_27562_ == 18) {
            for (int i = 0; i < 7; i++) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(p_27562_);
        }
    }

    public boolean canMate(AbstractDwarfEntity partner) {
        if (partner == this) {
            return false;
        } else {
            return partner instanceof AbstractDwarfEntity && this.isInLove() && partner.isInLove();
        }
    }

    public void spawnChildFromBreeding(ServerLevel level, AbstractDwarfEntity partner) {
        AgeableMob ageablemob = this.getBreedOffspring(level, partner);
        final net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent event = new net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent(this, partner, ageablemob);
        final boolean cancelled = net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event).isCanceled();
        ageablemob = event.getChild();
        if (cancelled) {
            this.setAge(6000);
            partner.setAge(6000);
            this.resetLove();
            partner.resetLove();
            return;
        }
        if (ageablemob != null) {
            ageablemob.setBaby(true);
            ageablemob.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            this.finalizeSpawnChildFromBreeding(level, partner, ageablemob);
            level.addFreshEntityWithPassengers(ageablemob);
        }
    }

    public void finalizeSpawnChildFromBreeding(ServerLevel level, AbstractDwarfEntity dwarf, @Nullable AgeableMob baby) {
        this.setAge(6000);
        dwarf.setAge(6000);
        this.resetLove();
        dwarf.resetLove();
        level.broadcastEntityEvent(this, (byte)18);
        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            level.addFreshEntity(new ExperienceOrb(level, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Override
    protected void actuallyHurt(ServerLevel p_376120_, DamageSource p_341676_, float p_341648_) {
        this.resetLove();
        super.actuallyHurt(p_376120_, p_341676_, p_341648_);
    }

    //Entity Data Accessors
    public static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Boolean> DRINKING =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.VILLAGER_DATA);

    //Data
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(ATTACKING, false);
        builder.define(BLOCKING, false);
        builder.define(DRINKING, false);
        builder.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("InLove", this.inLove);
        compound.putInt("Variant", this.getTypeVariant());
        if (this.loveCause != null) {
            compound.putUUID("LoveCause", this.loveCause);
        }
        compound.putInt("Age", this.getAge());
        compound.putInt("ForcedAge", this.forcedAge);
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
        this.inLove = compound.getInt("InLove");
        this.loveCause = compound.hasUUID("LoveCause") ? compound.getUUID("LoveCause") : null;
        this.entityData.set(VARIANT, compound.getInt("Variant"));
        this.setAge(compound.getInt("Age"));
        this.forcedAge = compound.getInt("ForcedAge");
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

    //Trading and Villager Data
    public long lastRestockGameTime = 0L;
    public static final long RESTOCK_INTERVAL_TICKS = 6000L;
    @Nullable
    public Player lastTradedPlayer;
    public int dwarfXp;
    public boolean increaseLevelOnUpdate = false;
    public int updateMerchantTimer = 0;
    public static final Logger LOGGER = LogUtils.getLogger();
    public boolean increaseProfessionLevelOnUpdate = false;
    public boolean assignProfessionWhenSpawned;
    public boolean assignProfessionWhenSpawned() {
        return this.assignProfessionWhenSpawned;
    }

    @Override
    public int getVillagerXp() {
        return this.dwarfXp;
    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    public VillagerData getVillagerData() {
        return this.entityData.get(DATA_VILLAGER_DATA);
    }

    public void setVillagerData(VillagerData p_35437_) {
        VillagerData villagerdata = this.getVillagerData();
        if (villagerdata.getProfession() != p_35437_.getProfession()) {
            this.offers = null;
        }

        this.entityData.set(DATA_VILLAGER_DATA, p_35437_);
    }

    public boolean shouldIncreaseLevel() {
        int i = this.getVillagerData().getLevel();
        return VillagerData.canLevelUp(i) && this.dwarfXp >= VillagerData.getMaxXpPerLevel(i);
    }


    public boolean shouldRestock() {
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

    public void resendOffersToTradingPlayer() {
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

    protected ItemStack previousMainHandItem = ItemStack.EMPTY;

    //Interactions
    protected InteractionResult handleCommonInteractions(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        //Default

        // Breeding
        if (this.isFood(itemstack)) {
            int age = this.getAge();
            if(this.isInLove()){
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.CONSUME;
            }
            if (!this.level().isClientSide && age == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                this.playEatingSound();
                return InteractionResult.SUCCESS_SERVER;
            }
            if (this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-age), true);
                this.playEatingSound();
                return InteractionResult.SUCCESS;
            }
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        //Booleans

        // Sign contract
        if (canSign()) {
            if (itemstack.is(JolCraftItems.CONTRACT_WRITTEN.get()) && !itemstack.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
                if (this.isInLove()) {
                    this.setNoAi(true);
                    this.previousMainHandItem = this.getMainHandItem().copy();
                    this.usePlayerItem(player, hand, itemstack);
                    this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(JolCraftItems.CONTRACT_WRITTEN.get()));
                    this.signingTicks = 40;
                    this.signingPlayer = player;
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }


        //Trading
        if (canTrade()) {
            if (!itemstack.is(Items.VILLAGER_SPAWN_EGG)
                    && this.isAlive()
                    && !this.isTrading()
                    && !this.isBaby()
                    && !itemstack.is(JolCraftItems.CONTRACT_WRITTEN.get())
                    && !this.isFood(itemstack)) {

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
        }

        // Fallback sound
        this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, this.isBaby() ? 1.5F : 1.0F);
        return InteractionResult.PASS;
    }

    //Sounds
    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return JolCraftSounds.DWARF_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        // Suppress if blocking
        if (this.isBlockCooldownReady()) {
            return null;
        }
        return JolCraftSounds.DWARF_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return JolCraftSounds.DWARF_DEATH.get();
    }

    @Nullable
    @Override
    public SoundEvent getNotifyTradeSound() {
        return JolCraftSounds.DWARF_YES.get();
    }

    @Nullable
    @Override
    protected SoundEvent getTradeUpdatedSound(boolean isYesSound) {
        return isYesSound ? JolCraftSounds.DWARF_YES.get() : JolCraftSounds.DWARF_NO.get();
    }

    //Spawning
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {return false;}

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
        DwarfVariant variant = Util.getRandom(DwarfVariant.values(), this.random);
        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        DwarfVariant variant = Util.getRandom(DwarfVariant.values(), this.random);
        DwarfEntity baby = JolCraftEntities.DWARF.get().create(level, EntitySpawnReason.BREEDING);
        baby.setVariant(variant);
        return baby;
    }

    //Variant
    public int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public DwarfVariant getVariant() {
        return DwarfVariant.byId(this.getTypeVariant() & 255);
    }

    public void setVariant(DwarfVariant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    //Other
    @Override
    protected int getBaseExperienceReward(ServerLevel p_376688_) {
        return 1 + this.random.nextInt(3);
    }
}
