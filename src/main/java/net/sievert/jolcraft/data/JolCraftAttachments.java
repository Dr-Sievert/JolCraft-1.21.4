package net.sievert.jolcraft.data;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.data.custom.attachment.block.HearthImpl;
import net.sievert.jolcraft.data.custom.attachment.lang.AncientDwarvenLanguageImpl;
import net.sievert.jolcraft.data.custom.attachment.lang.DwarvenLanguageImpl;
import net.sievert.jolcraft.data.custom.attachment.rep.DwarvenReputationImpl;
import net.sievert.jolcraft.data.custom.attachment.unlock.TomeUnlockImpl;

import java.util.function.Supplier;

public class JolCraftAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, JolCraft.MOD_ID);

    public static final Supplier<AttachmentType<DwarvenLanguageImpl>> DWARVEN_LANGUAGE =
            ATTACHMENT_TYPES.register("dwarven_language", () ->
                    AttachmentType.serializable(DwarvenLanguageImpl::new)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<AncientDwarvenLanguageImpl>> ANCIENT_DWARVEN_LANGUAGE =
            ATTACHMENT_TYPES.register("ancient_dwarven_language", () ->
                    AttachmentType.serializable(AncientDwarvenLanguageImpl::new)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<DwarvenReputationImpl>> DWARVEN_REP =
            ATTACHMENT_TYPES.register("dwarven_reputation", () ->
                    AttachmentType.serializable(DwarvenReputationImpl::new)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<TomeUnlockImpl>> TOME_UNLOCK =
            ATTACHMENT_TYPES.register("tome_unlock", () ->
                    AttachmentType.serializable(TomeUnlockImpl::new)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<HearthImpl>> HEARTH =
            ATTACHMENT_TYPES.register("hearth", () ->
                    AttachmentType.serializable(HearthImpl::new)
                            .copyOnDeath()
                            .build()
            );


    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
