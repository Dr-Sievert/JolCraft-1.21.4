package net.sievert.jolcraft.entity.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.sound.JolCraftSounds;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbstractDwarfEntity extends WanderingTrader {

    public AbstractDwarfEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

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

    //Breeding
    private int inLove;
    @Nullable
    private UUID loveCause;

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

    @Override
    protected void customServerAiStep(ServerLevel p_376777_) {
        if (this.getAge() != 0) {
            this.inLove = 0;
        }

        super.customServerAiStep(p_376777_);
    }

    @Override
    public void aiStep() {
        super.aiStep();
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

    //Interactions
    protected InteractionResult handleCommonInteractions(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
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
    private int getTypeVariant() {
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
