package net.sievert.jolcraft.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.sievert.jolcraft.JolCraft;

import java.util.Optional;

public class EndorsementGainTrigger extends SimpleCriterionTrigger<EndorsementGainTrigger.TriggerInstance> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "endorsement_gain");

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceLocation professionId) {
        this.trigger(player, instance -> {
            boolean match = instance.professionId().equals(professionId);
            return match;
        });
    }

    public static Criterion<TriggerInstance> endorsedBy(ResourceLocation professionId) {
        return JolCraftCriteriaTriggers.ENDORSEMENT_GAIN.createCriterion(
                new TriggerInstance(Optional.empty(), professionId)
        );
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceLocation professionId)
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ResourceLocation.CODEC.fieldOf("profession").forGetter(TriggerInstance::professionId)
        ).apply(instance, TriggerInstance::new));
    }
}
