package net.sievert.jolcraft.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.screen.custom.StrongboxMenu;
import net.sievert.jolcraft.sound.JolCraftSounds;  // Import your custom sounds

import javax.annotation.Nullable;

public class StrongboxBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity, MenuProvider {
    private NonNullList<ItemStack> items = NonNullList.withSize(18, ItemStack.EMPTY); // 2x9

    private final ContainerOpenersCounter openersCounter =
            new ContainerOpenersCounter() {
                @Override
                protected void onOpen(Level level, BlockPos pos, BlockState state) {
                    StrongboxBlockEntity.playSound(level, pos, JolCraftSounds.STRONGBOX_OPEN.get());
                }
                @Override
                protected void onClose(Level level, BlockPos pos, BlockState state) {
                    StrongboxBlockEntity.playSound(level, pos, JolCraftSounds.STRONGBOX_CLOSE.get());
                }
                @Override
                protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
                    StrongboxBlockEntity.this.signalOpenCount(level, pos, state, oldCount, newCount);
                }
                @Override
                protected boolean isOwnContainer(Player player) {
                    if (!(player.containerMenu instanceof StrongboxMenu menu)) return false;
                    return menu.getBlockEntity() == StrongboxBlockEntity.this;
                }
            };

    private final ChestLidController lidController = new ChestLidController();

    public StrongboxBlockEntity(BlockPos pos, BlockState state) {
        super(JolCraftBlockEntities.STRONGBOX.get(), pos, state);
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
        this.lootTable = lootTable;
        System.out.println("Loot table set: " + lootTable);  // For debugging purposes
    }

    @Override
    public int getContainerSize() { return items.size(); }

    @Override
    public NonNullList<ItemStack> getItems() { return items; }

    @Override
    public void setItems(NonNullList<ItemStack> items) { this.items = items; }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.jolcraft.strongbox");
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return lidController.getOpenness(partialTicks);
    }
    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, StrongboxBlockEntity be) {
        be.lidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if (id == 1) {
            this.lidController.shouldBeOpen(param > 0);
            return true;
        }
        return super.triggerEvent(id, param);
    }
    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, eventParam);
    }

    public void recheckOpen() {
        if (!this.remove && this.level != null && !this.level.isClientSide) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
        level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, provider);
        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, provider);
        }
    }

    // --- CUSTOM MENU: returns YOUR menu, not vanilla
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new StrongboxMenu(id, inv, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.jolcraft.strongbox");
    }

    private boolean silkTouched = false;

    public void setSilkTouched(boolean value) {
        this.silkTouched = value;
    }

    public boolean wasSilkTouched() {
        return this.silkTouched;
    }

}
