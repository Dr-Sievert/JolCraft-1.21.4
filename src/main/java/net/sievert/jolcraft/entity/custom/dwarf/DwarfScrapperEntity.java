package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

import java.util.*;

public class DwarfScrapperEntity extends AbstractDwarfEntity {

    public DwarfScrapperEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.COPPER_SPANNER.get()));
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfScrapperEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 0.0);

    }

    //Behavior
    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_SCRAPPER.get());
    }

    @Override
    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_scrapper");
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
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> MAIN_TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.COPPER_SPANNER.get(), 10, 1, 3, 10)
                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.SCRAP.get(), 2, 32, 5, 1)
                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.IRON_SPANNER.get(), 30, 1, 1, 40)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.SCRAP_HEAP.get(), 1, 32, 10, 4)

                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.SCRAP_HEAP.get(), 1, 15, JolCraftItems.RUSTAGATE.get(), 1, 1, 0, 0.05F)
                    }
            )
    );

    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> SALVAGE_TRADES = toIntMap(
            ImmutableMap.of(

                    // Novice
                    1, new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DEEPSLATE_MUG.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.EXPIRED_POTION.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.OLD_FABRIC.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_PICKAXE.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_AMULET.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_BELT.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_COINS.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.RUSTY_TONGS.get(), 1, 5, 3, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.INGOT_MOULD.get(), 1, 5, 3, 3)

                    }
            )
    );


    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {

        return new Int2ObjectOpenHashMap<>(pMap);
    }


    @Override
    protected void updateTrades() {
        int level = this.getVillagerData().getLevel();
        MerchantOffers offers = this.getOffers();

        // Add 1 main trade for this level
        VillagerTrades.ItemListing[] listings = MAIN_TRADES.get(level);
        if (listings != null) {
            this.addOffersFromItemListings(offers, listings, 1);
        }

        // Add 2 new unique salvage trades per level-up
        VillagerTrades.ItemListing[] salvagePoolArray = SALVAGE_TRADES.get(1);
        if (salvagePoolArray != null) {
            List<VillagerTrades.ItemListing> shuffled = new ArrayList<>(List.of(salvagePoolArray));
            Collections.shuffle(shuffled, new Random(this.random.nextLong()));

            int added = 0;
            for (VillagerTrades.ItemListing salvage : shuffled) {
                if (added >= 2) break;

                MerchantOffer offer = salvage.getOffer(this, this.random);
                if (offer != null && offers.stream().noneMatch(existing ->
                        ItemStack.isSameItemSameComponents(existing.getBaseCostA(), offer.getBaseCostA()))) {
                    offers.add(offer);
                    added++;
                }
            }
        }
    }

    //Sound
    @Override
    public float getVoicePitch() {
        return 1.5F; // lower pitch for scrapper
    }


}