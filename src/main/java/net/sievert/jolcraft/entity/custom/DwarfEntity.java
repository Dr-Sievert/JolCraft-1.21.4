package net.sievert.jolcraft.entity.custom;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;
import org.jetbrains.annotations.Nullable;

public class DwarfEntity extends AbstractDwarfEntity {

    public DwarfEntity(EntityType<? extends WanderingTrader> entityType, Level level) {
        super(entityType, level);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    //EntityDataAccessors

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.BOOLEAN);

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
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.SEA_PICKLE, 2, 1, 5, 1),
                            new JolCraftDwarfTrades.GoldForItems(Items.SLIME_BALL, 4, 1, 5, 1)
                    },
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.GUNPOWDER, 1, 1, 8, 1),
                            new JolCraftDwarfTrades.GoldForItems(Items.PODZOL, 3, 3, 6, 1)
                    }
            )
    );

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {

        return new Int2ObjectOpenHashMap<>(pMap);
    }

    @Override
    protected void updateTrades() {
        VillagerTrades.ItemListing[] trades1 = TRADES.get(1);
        VillagerTrades.ItemListing[] trades2 = TRADES.get(2);
        if (trades1 != null && trades2 != null) {
            MerchantOffers offers = this.getOffers();
            this.addOffersFromItemListings(offers, trades1, 5);
            this.addOffersFromItemListings(offers, trades2, 2);
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
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, stack -> stack.is(Items.GOLD_INGOT), false));
        this.goalSelector.addGoal(6, new DwarfFollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
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

    //Interaction and babies

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!itemstack.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby() && !this.isFood(itemstack)) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }

            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.SUCCESS;
        } else {
            if(this.isBaby()){
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.5F);
            }
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return super.mobInteract(player, hand);
        }
    }

    //Attacking and variants

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    // Variant
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
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
