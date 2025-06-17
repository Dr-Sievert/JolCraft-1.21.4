package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

public class DwarfEntity extends AbstractDwarfEntity {

    public DwarfEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
        //this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        //this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        //this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
        //this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));

    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30d)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16d)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    //Behavior
    @Override
    public boolean canTrade() {
        return true;
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
        this.goalSelector.addGoal(6, new DwarfFollowParentGoal(this, 1.25));
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
        return this.handleCommonInteractions(player, hand);
    }

    //Trades
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.STICK, 1, 1, 6, 1),
                            new JolCraftDwarfTrades.GoldForItems(Items.SMITHING_TABLE, 2, 4, 4, 1)

                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.BREAD, 1, 1, 5, 10),
                            new JolCraftDwarfTrades.TreasureMapForGold(13, JolCraftTags.Structures.ON_FORGE_EXPLORER_MAPS, "filled_map.forge", MapDecorationTypes.TARGET_X, 1, 10)

                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), 3, 1, 10, 1),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.QUILL_EMPTY.get(), 2, 10, 4, 1)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.DIAMOND, 1, 1, 10, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.EMERALD, 1, 10, 10, 1)
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.NETHERITE_BLOCK, 1, 1, 5, 10),
                            new JolCraftDwarfTrades.GoldForItems(Items.NETHERITE_SCRAP, 1, 3, 5, 1)
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
            this.addOffersFromItemListings(this.getOffers(), listings, 2); // 2 = max trades for that level
        }
    }

    //Unused

/*
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
*/

}
