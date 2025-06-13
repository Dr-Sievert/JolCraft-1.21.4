package net.sievert.jolcraft.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.sound.JolCraftSounds;
import org.jetbrains.annotations.Nullable;

public class DwarfGuardEntity extends AbstractDwarfEntity {

    public DwarfGuardEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setLeftHanded(false);
        // Set default main-hand item to iron axe
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        //this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        //this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);

    }

    //Animations and particles
    private void setupAnimationStates() {
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
        if (this.level().isClientSide()) {
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

    //Behavior
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DwarfBlockGoal(this));
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(4, new DwarfUseItemGoal<>(this, PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), SoundEvents.PLAYER_BURP, mob -> mob.getHealth() < mob.getMaxHealth(), 300));
        this.goalSelector.addGoal(5, new DwarfBreedGoal(this, 1.0, AbstractDwarfEntity.class));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, stack -> stack.is(Items.GOLD_INGOT), false));
        this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new MoveToBlockGoal(this, 0.8, 8) {
            @Override
            protected boolean isValidTarget(LevelReader level, BlockPos pos) {
                return level.getBlockState(pos).is(Blocks.COBBLED_DEEPSLATE);
            }
        });
        this.targetSelector.addGoal(1, new DwarfNonPlayerAlertGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Raider.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractPiglin.class, false));
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
        if (blockCooldown > 0) blockCooldown--;
    }

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

    @Override
    public void aiStep() {
        super.aiStep();
        tickBlockCooldown();
    }

    //Interaction
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return this.handleCommonInteractions(player, hand);
    }

    //Sounds
    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        // Suppress if blocking
        if (this.isBlockCooldownReady()) {
            return null;
        }
        return JolCraftSounds.DWARF_HURT.get();
    }

    //Spawning
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
        this.setLeftHanded(false);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    //Unused

   /* @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    } */


}