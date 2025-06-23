package net.sievert.jolcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;
import net.sievert.jolcraft.block.custom.FermentingStage;
import org.jetbrains.annotations.Nullable;

public class FermentingCauldronBlockEntity extends BlockEntity {

    private int fermentationProgress = 0;
    private final int maxFermentationProgress = 100; // constant max progress
    private int bubbleCooldown = 0;

    public FermentingCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(JolCraftBlockEntities.FERMENTING_CAULDRON.get(), pos, state);
    }

    // --- Save/load NBT ---

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("FermentationProgress", fermentationProgress);
        tag.putInt("BubbleCooldown", bubbleCooldown);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fermentationProgress = tag.getInt("FermentationProgress");
        bubbleCooldown = tag.getInt("BubbleCooldown");
    }

    // --- Client sync ---

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadAdditional(tag, null);
        }
    }

    private void syncToClient() {
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // --- Helpers for ticking logic ---
    public void tick() {
        if (level == null || level.isClientSide) return;

        // Only ferment if stage is a fermenting stage
        BlockState state = getBlockState();
        FermentingStage stage = state.getValue(FermentingCauldronBlock.STAGE);

        if (!isFermentingStage(stage)) {
            fermentationProgress = 0;
            return;
        }

        fermentationProgress++;

        if (level instanceof ServerLevel serverLevel) {
            if (bubbleCooldown <= 0) {
                double x = worldPosition.getX() + 0.5 + (serverLevel.random.nextDouble() - 0.5);
                double y = worldPosition.getY() + 1.1;
                double z = worldPosition.getZ() + 0.5 + (serverLevel.random.nextDouble() - 0.5);

                serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
                serverLevel.playSound(null, x, y, z, SoundEvents.BUBBLE_POP, SoundSource.BLOCKS, 0.3f, 1.4f);

                bubbleCooldown = 3 + serverLevel.random.nextInt(3);
            } else {
                bubbleCooldown--;
            }
        }

        updateBlockStateProgress();

        if (fermentationProgress >= maxFermentationProgress) {
            finishFermentation(stage);
            fermentationProgress = 0;
        }

        setChanged();
        syncToClient();
    }

    private boolean isFermentingStage(FermentingStage stage) {
        return switch (stage) {
            case YEAST_FERMENTING, BREW_FERMENTING -> true;
            default -> false;
        };
    }

    private void finishFermentation(FermentingStage currentStage) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        BlockState currentState = getBlockState();
        BlockState newState;

        switch (currentStage) {
            case YEAST_FERMENTING -> {
                // Yeast fermenting done → yeast ready
                newState = currentState.setValue(FermentingCauldronBlock.STAGE, FermentingStage.YEAST_READY)
                        .setValue(FermentingCauldronBlock.LEVEL, 3)
                        .setValue(FermentingCauldronBlock.FERMENTATION_PROGRESS, 0);
            }
            case BREW_FERMENTING -> {
                // Brew fermenting done → brew ready
                newState = currentState.setValue(FermentingCauldronBlock.STAGE, FermentingStage.BREW_READY)
                        .setValue(FermentingCauldronBlock.LEVEL, 3)
                        .setValue(FermentingCauldronBlock.FERMENTATION_PROGRESS, 0);
            }
            default -> {
                // Not a fermenting stage, just reset progress
                newState = currentState.setValue(FermentingCauldronBlock.FERMENTATION_PROGRESS, 0);
            }
        }

        serverLevel.setBlock(worldPosition, newState, 3);
    }

    private void updateBlockStateProgress() {
        if (level == null || level.isClientSide) return;

        BlockState currentState = getBlockState();
        int progressPercent = (int) ((fermentationProgress / (float) maxFermentationProgress) * 100);
        if (currentState.getValue(FermentingCauldronBlock.FERMENTATION_PROGRESS) != progressPercent) {
            level.setBlock(worldPosition, currentState.setValue(FermentingCauldronBlock.FERMENTATION_PROGRESS, progressPercent), 3);
        }
    }

    public float getFermentationProgressRatio() {
        return maxFermentationProgress == 0 ? 0f : (float) fermentationProgress / maxFermentationProgress;
    }
}
