package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

public class DwarfHistorianEntity extends AbstractDwarfEntity {

    public DwarfHistorianEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.DWARVEN_TOME.get()));
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfHistorianEntity.createLivingAttributes()
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
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_COMMON.get(), 1, 10, 5, 3),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_COMMON.get(), 1, 10, 5, 6),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.PARCHMENT.get(), 1, 3, 6, 1)

                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_UNCOMMON.get(), 1, 10, 35, 5),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_UNCOMMON.get(), 1, 10, 35, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.FILLED_MAP, 1, 3, 6, 2)

                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_RARE.get(), 1, 10, 75, 10),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_RARE.get(), 1, 10, 75, 20),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.QUILL_EMPTY.get(), 2, 1, 6, 8)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DWARVEN_TOME_EPIC.get(), 1, 10, 125, 25),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.ANCIENT_DWARVEN_TOME_EPIC.get(), 1, 10, 125, 50),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), 2, 1, 5, 15)
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DEEPMARROW.get(), 1, 3, 0, 32),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.DEEPMARROW, 1, 5, JolCraftItems.DEEPMARROW_DUST.get(), 3, 3, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.DEEPSLATE_PLATE.get(), 1, 15, JolCraftItems.REPUTATION_TABLET_0.get(), 1, 1, 0, 0.05F)
                    }
            )
    );

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {

        return new Int2ObjectOpenHashMap<>(pMap);
    }

    @Override
    protected void updateTrades() {
        int level = this.getVillagerData().getLevel();
        VillagerTrades.ItemListing[] listings = TRADES.get(level);
        if (listings != null) {
            this.addOffersFromItemListings(this.getOffers(), listings, 3);
        }
    }

    //Sound
    @Override
    public float getVoicePitch() {
        return 1.1F; // lower pitch for historian
    }


}