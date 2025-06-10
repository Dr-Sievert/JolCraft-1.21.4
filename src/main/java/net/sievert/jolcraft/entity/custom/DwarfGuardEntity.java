package net.sievert.jolcraft.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.sound.JolCraftSounds;
import org.jetbrains.annotations.Nullable;

public class DwarfGuardEntity extends DwarfEntity {

    public DwarfGuardEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
    }

    //EntityDataAccessors

    private static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(DwarfGuardEntity.class, EntityDataSerializers.BOOLEAN);

    //Animations

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;

    public final AnimationState blockAnimationState = new AnimationState();
    private boolean hasStartedBlockAnimation = false;

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 83;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking()) {
            if (attackAnimationTimeout == 0) {
                attackAnimationTimeout = 10;
                attackAnimationState.start(this.tickCount);
            } else {
                --this.attackAnimationTimeout;
            }
        } else {
            attackAnimationTimeout = 0;
            attackAnimationState.stop();
        }

        if (isBlocking()) {
            if (!hasStartedBlockAnimation) {
                blockAnimationState.start(this.tickCount);
                hasStartedBlockAnimation = true;
            }
        } else {
            hasStartedBlockAnimation = false;
        }

    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }

    }

    //Goals

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DwarfBlockGoal(this));
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(4, new DwarfUseItemGoal<>(this, PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), SoundEvents.PLAYER_BURP, mob -> mob.getHealth() < mob.getMaxHealth(), 100));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> stack.is(Items.GOLD_INGOT), false));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByNonPlayerTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, false));
    }

    //Attributes

    public static AttributeSupplier.Builder createAttributes() {
        return DwarfEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ARMOR, 15.0);

    }

    //Remove Trade
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.FAIL;
    }

    //Blocking

    public void setBlocking(boolean blocking) {
        this.entityData.set(BLOCKING, blocking);
    }

    public boolean isBlocking() {
        return this.entityData.get(BLOCKING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BLOCKING, false);
    }
    private boolean shouldStartBlocking = false;

    public void markForBlocking() {
        this.shouldStartBlocking = true;
    }

    public boolean consumeBlockFlag() {
        if (this.shouldStartBlocking) {
            this.shouldStartBlocking = false;
            return true;
        }
        return false;
    }

    public int blockCooldown = 0;

    public boolean isBlockCooldownReady() {
        return blockCooldown <= 0;
    }

    public void setBlockCooldown(int ticks) {
        this.blockCooldown = ticks;
    }

    public void tickBlockCooldown() {
        if (blockCooldown > 0) blockCooldown--;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        tickBlockCooldown();
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {

        // Suppress if blocking
        if (this.isBlockCooldownReady()) {
            return null;
        }

        return JolCraftSounds.DWARF_HURT.get();
    }



}