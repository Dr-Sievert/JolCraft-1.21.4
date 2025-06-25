package net.sievert.jolcraft.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.sievert.jolcraft.JolCraft;

import java.util.Optional;

public class HasAdvancementTrigger extends SimpleCriterionTrigger<HasAdvancementTrigger.TriggerInstance> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "has_advancement");

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }
    public void trigger(ServerPlayer player, ResourceLocation advancementId) {
        AdvancementHolder holder = player.server.getAdvancements().get(advancementId);
        if (holder != null && player.getAdvancements().getOrStartProgress(holder).isDone()) {
            this.trigger(player, instance -> instance.advancement().equals(advancementId));
        }
    }


    public static Criterion<TriggerInstance> has(ResourceLocation advancementId) {
        return JolCraftCriteriaTriggers.HAS_ADVANCEMENT.createCriterion(new TriggerInstance(Optional.empty(), advancementId));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceLocation advancement)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ResourceLocation.CODEC.fieldOf("advancement").forGetter(TriggerInstance::advancement)
        ).apply(instance, TriggerInstance::new));
    }
}
