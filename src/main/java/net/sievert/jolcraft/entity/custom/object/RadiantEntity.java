package net.sievert.jolcraft.entity.custom.object;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class RadiantEntity extends Entity implements TraceableEntity {

    @Nullable
    private BlockState lastReplacedBlockState = null;
    @Nullable
    private BlockPos currentLightPos = null;
    public BlockPos oldPos = null;

    // === Owner Tracking ===
    @Nullable private UUID ownerUUID;
    @Nullable private Entity cachedOwner;

    // === Animation State ===
    public final net.minecraft.world.entity.AnimationState idleAnimationState = new net.minecraft.world.entity.AnimationState();
    private int idleAnimationTimeout = 0;

    public RadiantEntity(EntityType<? extends RadiantEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        // Handle idle animation client-side
        if (level().isClientSide()) {
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = 120;
                idleAnimationState.start(this.tickCount);
            } else {
                --idleAnimationTimeout;
            }
            return;
        }

        BlockPos newPos = this.blockPosition();

        // If the block position has changed
        if (currentLightPos == null || !currentLightPos.equals(newPos)) {
            // Revert previous light block to its original state
            if (currentLightPos != null && lastReplacedBlockState != null &&
                    level().getBlockState(currentLightPos).is(Blocks.LIGHT)) {
                level().setBlock(currentLightPos, lastReplacedBlockState, 3);
            }

            // Save the block we're about to replace
            BlockState stateAtNew = level().getBlockState(newPos);
            if ((stateAtNew.isAir() || stateAtNew.is(Blocks.WATER) || stateAtNew.is(Blocks.LIGHT)) && getRadiantLightLevel() > 0) {
                lastReplacedBlockState = stateAtNew.is(Blocks.LIGHT) ? Blocks.AIR.defaultBlockState() : stateAtNew;

                boolean isWater = stateAtNew.getFluidState().getType() == Fluids.WATER;
                BlockState newLight = Blocks.LIGHT.defaultBlockState()
                        .setValue(LightBlock.LEVEL, getRadiantLightLevel())
                        .setValue(LightBlock.WATERLOGGED, isWater);

                level().setBlock(newPos, newLight, 3);
                currentLightPos = newPos.immutable();
            }
        } else {
            // Update light level if changed
            BlockState state = level().getBlockState(newPos);
            if (state.is(Blocks.LIGHT) && state.getValue(LightBlock.LEVEL) != getRadiantLightLevel()) {
                boolean waterlogged = state.getValue(LightBlock.WATERLOGGED);
                BlockState updated = state.setValue(LightBlock.LEVEL, getRadiantLightLevel())
                        .setValue(LightBlock.WATERLOGGED, waterlogged);
                level().setBlock(newPos, updated, 3);
            }
        }
    }


    @Override
    public void remove(RemovalReason reason) {
        if (currentLightPos != null && lastReplacedBlockState != null &&
                level().getBlockState(currentLightPos).is(Blocks.LIGHT)) {
            level().setBlock(currentLightPos, lastReplacedBlockState, 3);
        }
        super.remove(reason);
    }



    // --- Light Level ---
    private int radiantLightLevel = 15; // Default: max light

    /** Returns the current light level emitted (0-15). */
    public int getRadiantLightLevel() {
        return radiantLightLevel;
    }

    /** Sets the light level emitted (0-15). Clamps to valid range. */
    public void setRadiantLightLevel(int level) {
        this.radiantLightLevel = Math.max(0, Math.min(15, level));
    }

    // === OWNER GET/SET ===
    @Override
    @Nullable
    public Entity getOwner() {
        if (cachedOwner != null && !cachedOwner.isRemoved()) {
            return cachedOwner;
        }
        if (ownerUUID != null && level() instanceof ServerLevel serverLevel) {
            cachedOwner = serverLevel.getEntity(ownerUUID);
            return cachedOwner;
        }
        return null;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    // === SAVE/SYNC ===
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No synched data needed yet
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // TODO: Serialize ownerUUID if you want to persist ownership
    }

    // === INVULNERABILITY/PHYSICS ===
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false; // Invulnerable
    }

    @Override public boolean canBeCollidedWith() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean isNoGravity() { return true; }
    @Override public void move(MoverType type, Vec3 vec) { /* Immobile */ }

}
