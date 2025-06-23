package net.sievert.jolcraft.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks; // Assuming this contains FERMENTING_CAULDRON
import java.util.function.Supplier;

public class JolCraftBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, JolCraft.MOD_ID);


    public static final Supplier<BlockEntityType<FermentingCauldronBlockEntity>> FERMENTING_CAULDRON =
            BLOCK_ENTITIES.register("fermenting_cauldron", () ->
                    new BlockEntityType<>(FermentingCauldronBlockEntity::new, JolCraftBlocks.FERMENTING_CAULDRON.get())
            );


    // Add this method for ticking block entities
    public static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            if (type == FERMENTING_CAULDRON.get()) {
                return (lvl, pos, st, be) -> ((FermentingCauldronBlockEntity) be).tick();
            }
        }
        return null;
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
