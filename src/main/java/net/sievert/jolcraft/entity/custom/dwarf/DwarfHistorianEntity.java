package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

import javax.annotation.Nullable;

public class DwarfHistorianEntity extends AbstractDwarfEntity {

    public DwarfHistorianEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.DWARVEN_TOME.get()));
        this.instanceTrades = createRandomizedHistorianTrades();
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfHistorianEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);

    }

    //Behavior
    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_HISTORIAN.get());
    }

    @Override
    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_historian");
    }

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

    //Trades
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> createRandomizedHistorianTrades() {
        return AbstractDwarfEntity.toIntMap(ImmutableMap.of(


                        //Novice
                        1,
                        new VillagerTrades.ItemListing[]{
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_COMMON.get(), 1, 10, 5, 3),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_UNCOMMON.get(), 1, 10, 35, 6),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_RARE.get(), 1, 10, 75, 10),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_EPIC.get(), 1, 10, 125, 22),
                                new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.PARCHMENT.get(), 1, 2, 1, 3, 6, 1),
                        },

                        //Apprentice
                        2,
                        new VillagerTrades.ItemListing[]{
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_COMMON.get(), 1, 10, 5, 6),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_UNCOMMON.get(), 1, 10, 35, 8),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_RARE.get(), 1, 10, 75, 14),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_EPIC.get(), 1, 10, 125, 28),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get(), 1, 10, 250, 35),
                                new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DEEPMARROW.get(), 1, 3, 1, 32),
                                new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), 2, 4, 1, 2, 5, 1),
                        },

                        //Journeyman
                        3,
                        new VillagerTrades.ItemListing[]{
                                new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.UNIDENTIFIED_DWARVEN_TOME.get(), 8, 1, 3, 1),
                                new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.QUILL_EMPTY.get(), 1, 3, 1, 2, 6, 1)

                        },

                        //Expert
                        4,
                        new VillagerTrades.ItemListing[]{
                                new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.ANCIENT_UNIDENTIFIED_DWARVEN_TOME.get(), 13, 1, 3, 1),
                                new JolCraftDwarfTrades.ItemsForGold(Items.INK_SAC, 3, 6, 1, 2, 6, 1)
                        },

                        //Master
                        5,
                        new VillagerTrades.ItemListing[]{
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.ANCIENT_DWARVEN_TOME_COMMON, 1, 1, JolCraftItems.LEGENDARY_PAGE.get(), 1, 100, 0, 0.05F),
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.ANCIENT_DWARVEN_TOME_UNCOMMON, 1, 2, JolCraftItems.LEGENDARY_PAGE.get(), 2, 100, 0, 0.05F),
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.ANCIENT_DWARVEN_TOME_RARE, 1, 3, JolCraftItems.LEGENDARY_PAGE.get(), 3, 100, 0, 0.05F),
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.ANCIENT_DWARVEN_TOME_EPIC, 1, 4, JolCraftItems.LEGENDARY_PAGE.get(), 4, 100, 0, 0.05F),
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY, 1, 5, JolCraftItems.LEGENDARY_PAGE.get(), 5, 100, 0, 0.05F),
                                new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.LEGENDARY_PAGE, 10, 15, JolCraftItems.LEGENDARY_ANCIENT_UNIDENTIFIED_DWARVEN_TOME.get(), 1, 10, 0, 0.05F),

                                new JolCraftDwarfTrades.ItemsAndGoldToItemsWithData(
                                        JolCraftItems.LEGENDARY_PAGE.get(), 20,
                                        30,
                                        JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get(), 1,
                                        1, 1, 0F,
                                        (stack) -> stack.set(JolCraftDataComponents.LORE_LINE_ID, "mithril_forge_technique")
                                ),

                                new JolCraftDwarfTrades.ItemsAndGoldToItemsWithData(
                                        JolCraftItems.LEGENDARY_PAGE.get(), 20,
                                        30,
                                        JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get(), 1,
                                        1, 1, 0F,
                                        (stack) -> stack.set(JolCraftDataComponents.LORE_LINE_ID, "ancient_gemcraft")
                                ),

                                new JolCraftDwarfTrades.ItemsAndGoldToItemsWithData(
                                        JolCraftItems.LEGENDARY_PAGE.get(), 20,
                                        30,
                                        JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get(), 1,
                                        1, 1, 0F,
                                        (stack) -> stack.set(JolCraftDataComponents.LORE_LINE_ID, "coin_press_manual")
                                )

                        }
                )
        );
    }

    @Override
    protected void updateTrades() {
        int level = this.getVillagerData().getLevel();
        if (instanceTrades != null) {
            VillagerTrades.ItemListing[] listings = instanceTrades.get(level);
            if (listings != null) {
                MerchantOffers offers = this.getOffers();
                for (VillagerTrades.ItemListing listing : listings) {
                    MerchantOffer offer = listing.getOffer(this, this.random);
                    if (offer != null) {
                        offers.add(offer);
                    }
                }
            }
        }
    }

    //Sound
    @Override
    public float getVoicePitch() {
        return 1.1F;
    }

    @Nullable
    @Override
    protected SoundEvent getRestockSound() {
        return SoundEvents.VILLAGER_WORK_LIBRARIAN;
    }

    @Nullable
    @Override
    protected SoundEvent getRerollSound() {
        return SoundEvents.VILLAGER_WORK_LIBRARIAN;
    }


}