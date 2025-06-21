package net.sievert.jolcraft.entity.custom.dwarf;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.*;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.capability.DwarvenReputationImpl;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.ai.goal.DwarfBlockGoal;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfBeardColor;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfEyeColor;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfVariant;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.network.packet.ClientboundDwarfEndorseAnimationPacket;
import net.sievert.jolcraft.network.packet.ClientboundSyncReputationPacket;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.network.JolCraftNetworking;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AbstractDwarfEntity extends AbstractVillager {

    public AbstractDwarfEntity(EntityType<? extends AbstractVillager> entityType, Level level) {
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

    public final AnimationState inspectingAnimationState = new AnimationState();
    public boolean hasStartedInspectingAnimation = false;
    public void setInspecting(boolean inspecting) {
        this.entityData.set(INSPECTING, inspecting);
    }
    public boolean isInspecting() {
        return this.entityData.get(INSPECTING);
    }

    public final AnimationState blockingAnimationState = new AnimationState();
    public boolean hasStartedBlockingAnimation = false;
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
            this.idleAnimationTimeout = 90;
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
            if (!hasStartedBlockingAnimation) {
                blockingAnimationState.start(this.tickCount);
                hasStartedBlockingAnimation = true;
            }
        } else {
            hasStartedBlockingAnimation = false;
        }

        if (isDrinking()) {
            if (!hasStartedDrinkAnimation) {
                drinkAnimationState.start(this.tickCount);
                hasStartedDrinkAnimation = true;
            }
        } else {
            hasStartedDrinkAnimation = false;
        }

        if (isInspecting()) {
            if (!hasStartedInspectingAnimation) {
                inspectingAnimationState.start(this.tickCount);
                hasStartedInspectingAnimation = true;
            }
        } else {
            hasStartedInspectingAnimation = false;
        }

    }

    private Vec3 blockParticlePos = null;
    private int blockParticleTicks = 0;

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()) {
            this.setupAnimationStates();

            //Blocking particles
            if (this.hasStartedBlockingAnimation) {
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

            //Transformation particles
            if (this.contractTicks == 39) {
                // First subtle gray poof
                this.spawnColoredParticles(0.35F, 0.35F, 0.35F, 0.7F, 16, 0.5D);
            }
            if (this.contractTicks == 20) {
                // Second, slightly bigger gray poof
                this.spawnColoredParticles(0.35F, 0.35F, 0.35F, 0.8F, 24, 0.7D);
            }
            if (this.contractTicks == 2) {
                // Final: big gray cloud
                this.spawnColoredParticles(0.35F, 0.35F, 0.35F, 1.25F, 64, 2.5D);
            }



        }


    }

    //Behavior

    protected ItemStack previousMainHandItem = ItemStack.EMPTY;
    protected ItemStack usedItem = ItemStack.EMPTY; // Used for whatever item the player handed in
    protected int signingTicks = 0;
    protected Player signingPlayer;
    protected int contractTicks = 0;
    protected Player contractPlayer;
    protected int tabletTicks = 0;
    protected Player tabletPlayer;

    public boolean canTrade() {
        return false;
    }

    public boolean canSign() {
        return true;
    }

    public boolean canEndorse(Player player) {
        // Default: only allow if profession level is 5 (master)
        return this.getVillagerData().getLevel() >= 1;
    }

    // Flag to track if the dwarf is performing an action (e.g., processing a bounty crate)
    private boolean performingAction = false;

    // Getter and Setter for performingAction
    public boolean isPerformingAction() {
        return performingAction;
    }

    public void setPerformingAction(boolean performingAction) {
        this.performingAction = performingAction;
    }


    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_SIGNED.get());
    }

    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "none");
    }

    //Blocks dwarf state
    protected boolean blacklistedProperties() {
        return !this.isAlive() || this.isTrading();
    }

    //Blocks all interactions globally (e.g., spawn eggs)
    protected boolean isGloballyBlacklistedItem(ItemStack stack) {
        return stack.is(JolCraftTags.Items.SPAWN_EGGS);
    }


    // Items handled in the abstract class (e.g., contract, food)
    protected boolean isCommonHandledItem(ItemStack stack) {
        return stack.is(JolCraftItems.CONTRACT_WRITTEN.get()) ||
                this.isFood(stack);
    }

    // Items *delegated* to subclass (e.g., bounty crate, bounty scroll)
    protected boolean isSubclassHandledItem(ItemStack stack) {
        return stack.is(JolCraftItems.BOUNTY.get())
                || stack.is(JolCraftItems.BOUNTY_CRATE.get());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        // 🧠 Language check - ensures only players who know the Dwarvish language can interact
        boolean client = this.level().isClientSide;
        boolean knowsLanguage = client
                ? MyClientLanguageData.knowsLanguage()
                : (player.getData(JolCraftAttachments.DWARVEN_LANGUAGE) != null &&
                player.getData(JolCraftAttachments.DWARVEN_LANGUAGE).knowsLanguage());

        // ❌ Block interaction if language check fails
        if (!knowsLanguage) {
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (client) {
                player.displayClientMessage(
                        Component.translatable("chat.jolcraft.dwarf_interact.locked").withStyle(ChatFormatting.GRAY), true
                );
                return InteractionResult.CONSUME; // ✅ Allow client visuals
            }

            return InteractionResult.FAIL; // ✅ Block server interaction
        }

        // 🛑 Block if dwarf is dead, trading, holding a spawn egg, or performing an action
        if (blacklistedProperties() || isGloballyBlacklistedItem(itemstack) || isPerformingAction()) {
            return InteractionResult.FAIL; // ❌ Block interaction if any condition is met
        }

        // 🔄 Delegate to subclass for specific item handling (e.g., bounty crates, etc.)
        if (isSubclassHandledItem(itemstack)) {
            return InteractionResult.CONSUME; // ✅ Subclass consumes the interaction, no further action
        }

        // 🍞 Breeding or feeding dwarf (based on itemstack)
        if (this.isFood(itemstack)) {
            int i = this.getAge();

            // 🐺 Start love mode if dwarf is an adult and can fall in love
            if (!client && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                this.playEatingSound();
                return InteractionResult.SUCCESS_SERVER; // ✅ Valid breeding initiated on the server
            }

            // 👶 If the dwarf is a baby, age it up and trigger eating sound
            if (this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                this.playEatingSound();
                return InteractionResult.SUCCESS; // ✅ Client handles particles/sound
            }

            // ✅ Client side consumes the item and displays the correct animation
            if (client) {
                return InteractionResult.CONSUME;
            }

            // ❌ If the dwarf is neither in love nor a baby, play sound indicating invalid action
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, this.isBaby() ? 1.5F : 1.0F);
        }

        // 🔇 Fallback to generic common interaction logic
        return handleCommonInteractions(player, hand);
    }


    // ✅ Common interactions (if nothing else is handled)
    public InteractionResult handleCommonInteractions(Player player, InteractionHand hand) {
        // Check if the dwarf is currently performing an action (like processing a bounty crate)
        if (this.isPerformingAction()) {
            return InteractionResult.FAIL; // Block any interactions if an action is in progress
        }

        ItemStack itemstack = player.getItemInHand(hand);

        // 🛑 Common interactions if none of above interactions were triggered

        //Paid
        if (itemstack.is(JolCraftItems.GOLD_COIN.get()) && this.canBePaid() && !this.isBaby()) {
            this.setPaid(player);
            this.level().playSound(null, this.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 1.0F, 1.4F);
            this.usePlayerItem(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        }


        // 🖊️ Contract signing
        if (itemstack.is(JolCraftItems.CONTRACT_WRITTEN.get()) && !this.isBaby()) {
            if (!canSign() || !this.isPaid() || this.signingTicks > 0 || this.signingPlayer != null) {
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.SUCCESS; // Block fallback trade
            }

            this.setNoAi(true);
            this.previousMainHandItem = this.getMainHandItem().copy();
            this.usePlayerItem(player, hand, itemstack);
            this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(JolCraftItems.CONTRACT_WRITTEN.get()));
            this.signingTicks = 40;
            this.signingPlayer = player;
            return InteractionResult.SUCCESS_SERVER;
        }

        // 🪄 Profession Promotion with Signed Contract
        if (itemstack.is(JolCraftTags.Items.SIGNED_CONTRACTS) && !this.isBaby()) {
            if (!canPromoteToProfession() || !this.isPaid() || this.contractTicks > 0 || this.contractPlayer != null) {
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            this.setNoAi(true);
            this.previousMainHandItem = itemstack.copy();              // Save the correct contract used for transform
            this.usePlayerItem(player, hand, itemstack);               // Remove one contract from player hand
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copy()); // Dwarf visibly holds contract for animation
            this.level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL, 1.0F, 1.5F);
            this.contractTicks = 40;
            this.contractPlayer = player;
            return InteractionResult.SUCCESS_SERVER;
        }

        //Tablet
        if (itemstack.is(JolCraftTags.Items.REPUTATION_TABLETS) && !this.isBaby() && (tabletTicks == 0 || tabletPlayer == null)) {
            DwarvenReputationImpl rep = player.getData(JolCraftAttachments.DWARVEN_REP.get());
            ResourceLocation profId = this.getProfessionId();

            if (!this.canEndorse(player) && !this.isPaid()) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.translatable("tooltip.jolcraft.reputation.cannot_endorse").withStyle(ChatFormatting.GRAY), true);
                }
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return this.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

            if (!this.canEndorse(player)) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.translatable("tooltip.jolcraft.reputation.cannot_endorse").withStyle(ChatFormatting.GRAY), true);
                }
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return this.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

            if (!this.isPaid()) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.translatable("tooltip.jolcraft.dwarf.not_paid").withStyle(ChatFormatting.GRAY), true);
                }
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return this.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
            if (rep.hasEndorsement(profId)) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.translatable("tooltip.jolcraft.reputation.already_endorsed").withStyle(ChatFormatting.GRAY), true);
                }
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                return this.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

            // ✅ All requirements passed: perform endorsement (SERVER SIDE)
            if (!this.level().isClientSide) {
                this.previousMainHandItem = this.getMainHandItem().copy(); // Save the dwarf's current hand
                this.usedItem = itemstack.copy(); // Save the actual item being processed (the tablet)
                this.setItemSlot(EquipmentSlot.MAINHAND, usedItem.copy());
                this.usePlayerItem(player, hand, itemstack);
                this.tabletTicks = 40;
                this.tabletPlayer = player;
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_YES.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

                JolCraftNetworking.sendToNearbyClients(
                        this.level(), this.blockPosition(), 32,
                        new ClientboundDwarfEndorseAnimationPacket(this.getId())
                );

                if (player instanceof ServerPlayer serverPlayer) {
                    JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncReputationPacket(rep.getTier()));
                }
            }
            return this.level().isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }


        // 💼 Trade (only if hand empty and a baby)
        if (canTrade()
                && itemstack.isEmpty()
                && !this.isBaby()
                && (player.getAbilities() == null || !player.getAbilities().instabuild || player.getInventory().getSelected().isEmpty()))
        {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }

            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.FAIL;
                }

                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), this.getVillagerData().getLevel());
            }

            return InteractionResult.SUCCESS;
        }

        this.level().playSound(null, this.blockPosition(), JolCraftSounds.DWARF_NO.get(), SoundSource.NEUTRAL, 1.0F, this.isBaby() ? 1.5F : 1.0F);
        return InteractionResult.FAIL; // ❌ Block any fallback actions
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


        if (this.level().isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level().addParticle(
                            ParticleTypes.HAPPY_VILLAGER,
                            this.getRandomX(1.0),
                            this.getRandomY() + 0.5,
                            this.getRandomZ(1.0),
                            0.0, 0.0, 0.0
                    );
                }
                this.forcedAgeTimer--;
            }
        }

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

        // Paid mode ticking
        if (this.paidTicks > 0) {
            this.paidTicks--;
            // Every 30 ticks, spawn a little gold sparkle/coin effect
            if (this.paidTicks % 30 == 0) {
                this.spawnColoredParticles(1.0F, 0.84F, 0.0F, 1.0F, 3, 0.4D);
            }
        }


        // Signing logic
        if (this.signingTicks > 0) {
            this.signingTicks--;
            this.setInspecting(true); // Only touch the flag!

            // Paid status: always reset at the start of animation
            if (this.signingTicks == 39) {
                this.resetPaid();
            }

            if (this.signingTicks == 15 || this.signingTicks == 25) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.NEUTRAL, 1.0F, 1.2F);
            }

            if (this.signingTicks == 0) {
                // Only use setInspecting(false) to stop animation
                this.setInspecting(false);
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

        // Contract logic
        if (this.contractTicks > 0) {
            --this.contractTicks;
            this.setInspecting(true); // Only touch the flag!

            if (this.contractTicks == 20) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL, 1.0F, 1.5F);
            }
            if (this.contractTicks == 2) {
                this.level().playSound(null, this.blockPosition(), JolCraftSounds.POOF.get(), SoundSource.NEUTRAL, 1.5F, 1.0F);
            }
            if (this.contractTicks == 0 && !this.level().isClientSide && this.contractPlayer != null) {
                this.transformToProfession();
                this.setInspecting(false); // Animation ends here
                this.contractPlayer = null;
                this.setNoAi(false);
            }
        }


        // Tablet endorsement animation & logic
        if (this.tabletTicks > 0) {
            this.tabletTicks--;

            // Animation: only touch setInspecting(true)
            this.setInspecting(true);

            // Paid status: always reset at the start of animation
            if (this.tabletTicks == 39) {
                this.resetPaid();
            }

            // Cartographer sound milestones
            if (this.tabletTicks == 15 || this.tabletTicks == 25) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.NEUTRAL, 1.0F, 0.5F);
            }

            // End of animation: clean up, throw tablet, and sync state
            if (this.tabletTicks == 0) {
                this.setInspecting(false);

                if (this.level().isClientSide) {
                    net.sievert.jolcraft.client.data.MyClientReputationData.setEndorsementAnimation(this.getId(), false);
                }

                if (!this.level().isClientSide) {
                    if (this.tabletPlayer != null) {
                        DwarvenReputationImpl rep = this.tabletPlayer.getData(JolCraftAttachments.DWARVEN_REP.get());
                        ResourceLocation profId = this.getProfessionId();

                        // Only add the endorsement if not already present
                        boolean added = false;
                        if (!rep.hasEndorsement(profId)) {
                            rep.addEndorsement(profId);
                            added = true;
                        }

                        // Always sync tier and endorsements after animation (even if unchanged)
                        if (this.tabletPlayer instanceof ServerPlayer serverPlayer) {
                            net.sievert.jolcraft.network.JolCraftNetworking.sendToClient(serverPlayer,
                                    new net.sievert.jolcraft.network.packet.ClientboundSyncReputationPacket(rep.getTier()));
                            net.sievert.jolcraft.network.JolCraftNetworking.sendToClient(serverPlayer,
                                    new net.sievert.jolcraft.network.packet.ClientboundSyncEndorsementsPacket(rep.getEndorsements()));
                        }

                        // Only throw updated tablet if endorsement was new
                        if (added) {
                            ItemStack updatedTablet = this.usedItem.copy(); // Always use usedItem here!
                            // Lock in all relevant values at moment of endorsement
                            updatedTablet.set(JolCraftDataComponents.REP_ENDORSEMENTS.get(), rep.getEndorsementCount());
                            updatedTablet.set(JolCraftDataComponents.REP_TIER.get(), rep.getTier());
                            updatedTablet.set(JolCraftDataComponents.REP_OWNER.get(), this.tabletPlayer.getName().getString());

                            Vec3 start = this.position().add(0.0, this.getEyeHeight(), 0.0);
                            Vec3 target = this.tabletPlayer.position().add(0.0, this.tabletPlayer.getBbHeight() * 0.5, 0.0);
                            Vec3 velocity = target.subtract(start).normalize().scale(0.4);

                            ItemEntity thrown = new ItemEntity(this.level(), start.x, start.y, start.z, updatedTablet);
                            thrown.setDeltaMovement(velocity);
                            thrown.setPickUpDelay(10);
                            this.level().addFreshEntity(thrown);
                            this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F);

                            // Give back old item (usually empty hand)
                            this.setItemSlot(EquipmentSlot.MAINHAND, this.previousMainHandItem);
                            this.previousMainHandItem = ItemStack.EMPTY;
                        }
                    }
                }
                // Always clear previousMainHandItem and tabletPlayer on both sides!
                this.previousMainHandItem = ItemStack.EMPTY;
                this.tabletPlayer = null;
            }
        }





    }

    public static final Set<EntityType<?>> PROMOTABLE_DWARF_TYPES = Set.of(JolCraftEntities.DWARF.get());


    public boolean canPromoteToProfession() {
        return PROMOTABLE_DWARF_TYPES.contains(this.getType()) && this.isAlive() && !this.isBaby();
    }

    @Nullable
    public EntityType<? extends AbstractDwarfEntity> resolveProfessionType(ItemStack contractStack) {
        if (contractStack == null || contractStack.isEmpty()) return null;
        return CONTRACT_TO_PROFESSION.get(contractStack.getItem());
    }

    public static final Map<Item, EntityType<? extends AbstractDwarfEntity>> CONTRACT_TO_PROFESSION = Map.ofEntries(
            Map.entry(JolCraftItems.CONTRACT_GUILDMASTER.get(), JolCraftEntities.DWARF_GUILDMASTER.get()),
            // Tier 1
            Map.entry(JolCraftItems.CONTRACT_MERCHANT.get(), JolCraftEntities.DWARF_MERCHANT.get()),
            Map.entry(JolCraftItems.CONTRACT_HISTORIAN.get(), JolCraftEntities.DWARF_HISTORIAN.get()),
            Map.entry(JolCraftItems.CONTRACT_SCRAPPER.get(), JolCraftEntities.DWARF_SCRAPPER.get()),
            // Tier 2
            Map.entry(JolCraftItems.CONTRACT_GUARD.get(), JolCraftEntities.DWARF_GUARD.get())

           /*
            Map.entry(JolCraftItems.CONTRACT_BREWMASTER.get(), JolCraftEntities.DWARF_BREWMASTER.get()),
            Map.entry(JolCraftItems.CONTRACT_KEEPER.get(), JolCraftEntities.DWARF_KEEPER.get()),
            // Tier 3
            Map.entry(JolCraftItems.CONTRACT_MINER.get(), JolCraftEntities.DWARF_MINER.get()),
            Map.entry(JolCraftItems.CONTRACT_EXPLORER.get(), JolCraftEntities.DWARF_EXPLORER.get()),
            Map.entry(JolCraftItems.CONTRACT_ALCHEMIST.get(), JolCraftEntities.DWARF_ALCHEMIST.get()),
            // Tier 4
            Map.entry(JolCraftItems.CONTRACT_ARCANIST.get(), JolCraftEntities.DWARF_ARCANIST.get()),
            Map.entry(JolCraftItems.CONTRACT_PRIEST.get(), JolCraftEntities.DWARF_PRIEST.get()),
            Map.entry(JolCraftItems.CONTRACT_ARTISAN.get(), JolCraftEntities.DWARF_ARTISAN.get()),
            // Tier 5
            Map.entry(JolCraftItems.CONTRACT_CHAMPION.get(), JolCraftEntities.DWARF_CHAMPION.get()),
            Map.entry(JolCraftItems.CONTRACT_BLACKSMITH.get(), JolCraftEntities.DWARF_BLACKSMITH.get()),
            Map.entry(JolCraftItems.CONTRACT_SMELTER.get(), JolCraftEntities.DWARF_SMELTER.get())

            */

    );

    public void transformToProfession() {
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            EntityType<? extends AbstractDwarfEntity> professionType = resolveProfessionType(this.previousMainHandItem);


            if (professionType != null) {
                // This is the best usage for NeoForge 1.21.x
                Entity entity = professionType.create(
                        serverLevel,
                        null, // Consumer<T>
                        this.blockPosition(),
                        EntitySpawnReason.CONVERSION,
                        false, // shouldOffsetY
                        false  // shouldOffsetYMore
                );

                if (entity instanceof AbstractDwarfEntity newDwarf) {
                    newDwarf.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());

                    //Preserve data
                    newDwarf.setBeard(this.getBeard());
                    newDwarf.setEye(this.getEye());

                    serverLevel.addFreshEntity(newDwarf);
                    this.discard();
                }
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
    public void handleEntityEvent(byte id) {
        if (id == 18) {
            for (int i = 0; i < 7; i++) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
            }
        }
        if (id == 19) {
            // Gold coins or sparkle (paid)
            this.spawnColoredParticles(1.0F, 0.84F, 0.0F, 1.0F, 7, 0.5D);
        }
        else {
            super.handleEntityEvent(id);
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

    public static final EntityDataAccessor<Integer> BEARD_COLOR =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> EYE_COLOR =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(AbstractDwarfEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Boolean> INSPECTING =
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
        builder.define(BEARD_COLOR, 0);
        builder.define(EYE_COLOR, 0);
        builder.define(ATTACKING, false);
        builder.define(INSPECTING, false);
        builder.define(BLOCKING, false);
        builder.define(DRINKING, false);
        builder.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("InLove", this.inLove);
        compound.putInt("Variant", this.getTypeVariant());
        compound.putInt("Beard", this.getTypeBeard());
        compound.putInt("Eye", this.getTypeEye());
        if (this.loveCause != null) {
            compound.putUUID("LoveCause", this.loveCause);
        }
        // 🪙 Paid status
        compound.putInt("PaidTicks", this.paidTicks);
        if (this.paidCause != null) {
            compound.putUUID("PaidCause", this.paidCause);
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
        // 🪙 Paid status
        this.paidTicks = compound.getInt("PaidTicks");
        this.paidCause = compound.hasUUID("PaidCause") ? compound.getUUID("PaidCause") : null;
        this.entityData.set(VARIANT, compound.getInt("Variant"));
        this.entityData.set(BEARD_COLOR, compound.getInt("Beard"));
        this.entityData.set(EYE_COLOR, compound.getInt("Eye"));
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

    //Paying
    protected int paidTicks;
    @Nullable
    protected UUID paidCause;
    public static final int MAX_PAID_TICKS = 20 * 60; // 1 min

    public boolean isPaid() {
        return this.paidTicks > 0;
    }

    public void setPaid(@Nullable Player player) {
        this.paidTicks = MAX_PAID_TICKS;
        if (player != null) {
            this.paidCause = player.getUUID();
        }
        this.level().broadcastEntityEvent(this, (byte)19); // 19 = custom code for "paid"
    }

    public void resetPaid() {
        this.paidTicks = 0;
        this.paidCause = null;
    }

    public boolean canBePaid() {
        return this.paidTicks <= 0;
    }

    public int getPaidTicks() {
        return this.paidTicks;
    }

    @Nullable
    public ServerPlayer getPaidCause() {
        if (this.paidCause == null) return null;
        Player player = this.level().getPlayerByUUID(this.paidCause);
        return player instanceof ServerPlayer ? (ServerPlayer)player : null;
    }

    // Particles
    protected void spawnColoredParticles(float r, float g, float b, float scale, int count, double scatter) {
        if (!this.level().isClientSide()) return;

        int rgb = ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
        DustParticleOptions dust = new DustParticleOptions(rgb, scale);

        Vec3 forward = this.getLookAngle().normalize(); // Get the direction the dwarf is facing
        // Position particles above the dwarf's head and slightly back in their looking direction
        double baseX = this.getX() + forward.x * 0.6;  // Move back by half a block (smaller scale)
        double baseY = this.getY() + 1.8D; // Raised height (above the dwarf's head)
        double baseZ = this.getZ() + forward.z * 0.5;  // Move back by half a block (smaller scale)

        // Spawn particles in a scattered cloud
        for (int i = 0; i < count; i++) {
            // Apply randomness to x, y, z positions to scatter particles in a cloud
            double offsetX = baseX + (this.random.nextDouble() - 0.5D) * scatter; // Spread in X direction
            double offsetY = baseY + (this.random.nextDouble() - 0.5D) * scatter; // Spread in Y direction (vertical)
            double offsetZ = baseZ + (this.random.nextDouble() - 0.5D) * scatter; // Spread in Z direction

            // Random velocity for particles
            double velocityX = (this.random.nextDouble() - 0.5D) * 0.1D;
            double velocityY = this.random.nextDouble() * 0.1D;
            double velocityZ = (this.random.nextDouble() - 0.5D) * 0.1D;

            // Add the particle to the world
            this.level().addParticle(dust, offsetX, offsetY, offsetZ, velocityX, velocityY, velocityZ);
        }
    }

    //Loot

    // Helper: check for quest/contract items
    private boolean isSpecialDropItem(ItemStack stack) {
        return stack.is(JolCraftItems.CONTRACT_WRITTEN.get()) || stack.is(JolCraftItems.CONTRACT_SIGNED.get()) || stack.is(JolCraftItems.BOUNTY.get()) || stack.is(JolCraftItems.BOUNTY_CRATE.get());
    }

    // Helper: allow only certain gear to drop
    private boolean shouldDropEquipment(ItemStack stack) {
        // Only drop iron axes
        return stack.is(Items.DIAMOND);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        // Handle MAINHAND item
        ItemStack mainHand = this.getMainHandItem();

        if (!mainHand.isEmpty()) {
            if (isSpecialDropItem(mainHand)) {
                // Always drop special quest items (contract, bounty crate, etc)
                this.spawnAtLocation(level, mainHand);
            } else if (shouldDropEquipment(mainHand)) {
                // Drop only certain equipment (iron axe, etc)
                this.spawnAtLocation(level, mainHand);
            }
            // Otherwise: do not drop (e.g., remove from world)
            // Clear mainhand slot to avoid duplicate drops
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        // Optionally: call super to handle other drops (armor, offhand, etc) as usual
        super.dropCustomDeathLoot(level, source, recentlyHit);
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

    @Override
    protected void updateTrades() {
        // No default trades at base level
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

    //Sounds
    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
            return this.isTrading() ? JolCraftSounds.DWARF_TRADE.get() : JolCraftSounds.DWARF_AMBIENT.get();
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

    @Nullable
    @Override
    public void playCelebrateSound() {
        this.makeSound(JolCraftSounds.DWARF_YES.get());
    }


    //Spawning
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {return false;}

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
        DwarfVariant variant = Util.getRandom(DwarfVariant.values(), this.random);
        DwarfBeardColor beard = Util.getRandom(DwarfBeardColor.values(), this.random);
        DwarfEyeColor eye = Util.getRandom(DwarfEyeColor.values(), this.random);
        this.setVariant(variant);
        this.setBeard(beard);
        this.setEye(eye);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        DwarfEntity baby = JolCraftEntities.DWARF.get().create(level, EntitySpawnReason.BREEDING);
        DwarfVariant variant = Util.getRandom(DwarfVariant.values(), this.random);
        DwarfBeardColor beard = Util.getRandom(DwarfBeardColor.values(), this.random);
        DwarfEyeColor eye = Util.getRandom(DwarfEyeColor.values(), this.random);
        baby.setVariant(variant);
        baby.setBeard(beard);
        baby.setEye(eye);
        return baby;
    }

    //Randomized traits
    public int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public DwarfVariant getVariant() {
        return DwarfVariant.byId(this.getTypeVariant() & 255);
    }

    public void setVariant(DwarfVariant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    public int getTypeBeard() {
        return this.entityData.get(BEARD_COLOR);
    }

    public DwarfBeardColor getBeard() {
        return DwarfBeardColor.byId(this.getTypeBeard() & 255);
    }

    public void setBeard(DwarfBeardColor beard) {
        this.entityData.set(BEARD_COLOR, beard.getId() & 255);
    }

    public int getTypeEye() {
        return this.entityData.get(EYE_COLOR);
    }

    public DwarfEyeColor getEye() {
        return DwarfEyeColor.byId(this.getTypeEye() & 255);
    }

    public void setEye(DwarfEyeColor eye) {
        this.entityData.set(EYE_COLOR, eye.getId() & 255);
    }

    //Other
    @Override
    protected int getBaseExperienceReward(ServerLevel p_376688_) {
        return 1 + this.random.nextInt(3);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        Player player = this.getTradingPlayer();
        offer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.rewardTradeXp(offer);
        if (player instanceof ServerPlayer serverPlayer) {
            player.awardStat(Stats.TRADED_WITH_VILLAGER);
            JolCraftCriteriaTriggers.TRADE_WITH_DWARF.trigger(serverPlayer);
        }
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent(this.lastTradedPlayer, offer, this));
    }




}
