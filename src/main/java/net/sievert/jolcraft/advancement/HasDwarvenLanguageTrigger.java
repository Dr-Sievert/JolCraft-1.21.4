package net.sievert.jolcraft.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.DwarvenLanguage;
import net.sievert.jolcraft.capability.JolCraftCapabilities;

import java.util.Optional;

public class HasDwarvenLanguageTrigger extends SimpleCriterionTrigger<HasDwarvenLanguageTrigger.TriggerInstance> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "has_dwarven_language");

    public static final HasDwarvenLanguageTrigger INSTANCE =
            CriteriaTriggers.register("has_dwarven_language", new HasDwarvenLanguageTrigger());

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        DwarvenLanguage cap = player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);
        if (cap != null && cap.knowsLanguage()) {
            this.trigger(player, instance -> true);
        }
    }

    public static Criterion<TriggerInstance> hasLanguage() {
        return JolCraftCriteriaTriggers.HAS_DWARVEN_LANGUAGE.createCriterion(new TriggerInstance(Optional.empty()));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
                ).apply(instance, TriggerInstance::new)
        );
    }
}
