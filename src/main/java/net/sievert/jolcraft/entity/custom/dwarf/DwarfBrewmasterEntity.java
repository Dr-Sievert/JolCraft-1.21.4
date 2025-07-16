package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.sound.JolCraftSoundHelper;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

import javax.annotation.Nullable;

public class DwarfBrewmasterEntity extends AbstractDwarfEntity {


    public DwarfBrewmasterEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.GLASS_MUG.get()));
        this.instanceTrades = createRandomizedBrewmasterTrades();
    }

    // ----------------------------
    // Attributes
    // ----------------------------

    public static AttributeSupplier.Builder createAttributes() {
        return DwarfEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30d)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    // ----------------------------
    // Core
    // ----------------------------

    @Override
    public boolean canTrade() { return true; }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_BREWMASTER.get());
    }

    @Override
    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_brewmaster");
    }

    @Override
    public float getVoicePitch() { return 0.9F; }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FirePanicGoal(this, 1.3));
        this.targetSelector.addGoal(2, new DwarfNonPlayerAlertGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(3, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(4, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(5, new DwarfBreedGoal(this, 1.0, AbstractDwarfEntity.class));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, stack -> stack.is(JolCraftItems.GOLD_COIN), false));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new MoveToBlockGoal(this, 0.8, 8) {
            @Override
            protected boolean isValidTarget(LevelReader level, BlockPos pos) {
                return level.getBlockState(pos).is(Blocks.COBBLED_DEEPSLATE);
            }
        });
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // 🧠 Language check
        InteractionResult langCheck = this.languageCheck(player);
        if (langCheck != InteractionResult.SUCCESS) return langCheck;

        // Reputation check
        InteractionResult repCheck = this.reputationCheck(player, 1);
        if (repCheck != InteractionResult.SUCCESS) return repCheck;

        // Call parent for all other interactions (contracts, trades, etc)
        return super.mobInteract(player, hand);
    }

    // ----------------------------
    // Trade Randomization
    // ----------------------------

    /** Generates a new randomized trade set for this Brewmaster instance.
     * No pre-randomization! All values are min/max, the trade does the rolling. */
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> createRandomizedBrewmasterTrades() {
        return AbstractDwarfEntity.toIntMap(ImmutableMap.of(
                // Novice
                1, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.GLASS_MUG.get(), 1, 2, 5, 2, 1, 3),
                        new JolCraftDwarfTrades.ItemsForGold(Items.SUGAR, 1, 2, 1, 2, 10, 1)
                },
                // Apprentice
                2, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.ItemsForGold(Items.CAULDRON, 7, 12, 1, 9, 10),
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BARLEY_MALT.get(), 12, 22, 10, 15, 1, 3)
                },
                // Journeyman
                3, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ASGARNIAN_HOPS.get(), 10, 20, 10, 15, 1, 3),
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DUSKHOLD_HOPS.get(), 10, 20, 10, 15, 1, 3),
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.KRANDONIAN_HOPS.get(), 10, 20, 10, 15, 1, 3),
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.YANILLIAN_HOPS.get(), 10, 20, 10, 15, 1, 3)
                },
                // Expert
                4, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_BREW.get(), 1, 5, 50, 3, 6),
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.YEAST.get(), 3, 5, 1, 2, 10, 10)
                },
                // Master
                5, new VillagerTrades.ItemListing[]{
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.EMBERGLASS.get(), 1, 10, 0, 20, 40),
                        new JolCraftDwarfTrades.ItemsAndGoldToItems(
                                JolCraftItems.EMBERGLASS.get(), 1, 20, 40,
                                JolCraftBlocks.HEARTH.get(), 1, 1, 0, 0F
                        )
                }
        ));
    }

    @Nullable
    @Override
    protected SoundEvent getRestockSound() {
        return SoundEvents.VILLAGER_WORK_CLERIC;
    }

    @Nullable
    @Override
    protected SoundEvent getRerollSound() {
        return SoundEvents.VILLAGER_WORK_CLERIC;
    }



}
