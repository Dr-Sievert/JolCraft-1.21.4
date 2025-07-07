package net.sievert.jolcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.util.bounty.BountyData;

import java.util.function.UnaryOperator;

public class JolCraftDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, JolCraft.MOD_ID);


    //Language

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> LORE_LINE_ID =
            register("lore_line_id", builder -> builder.persistent(Codec.STRING));

    //Reputation

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> REP_OWNER =
            register("rep_owner", builder -> builder.persistent(Codec.STRING));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> REP_TIER =
            register("rep_tier", builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> REP_ENDORSEMENTS =
            register("rep_endorsements", builder -> builder.persistent(Codec.INT));

    //Bounty

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BOUNTY_TIER =
            register("bounty_tier", builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BountyData>> BOUNTY_DATA =
            register("bounty_data", builder -> builder.persistent(BountyData.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BOUNTY_FILL =
            register("bounty_fill", builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BOUNTY_COMPLETE =
            register("bounty_complete", builder -> builder.persistent(Codec.BOOL));

    //Brewing

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> HOPS =
            register("hops", builder -> builder.persistent(Codec.STRING));

    //Strongbox

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> LOOT_TABLE =
            register("loot_table", builder -> builder.persistent(Codec.STRING));

    // Similarly for LOOT_SEED
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> LOOT_SEED =
            register("loot_seed", builder -> builder.persistent(Codec.STRING));


    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
