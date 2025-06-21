package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

public class DwarfGuildmasterEntity extends AbstractDwarfEntity {

    public DwarfGuildmasterEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.CONTRACT_SIGNED.get()));
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
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_GUILDMASTER.get());
    }

    @Override
    public boolean canEndorse(Player player) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FirePanicGoal(this, 1.3));
        this.targetSelector.addGoal(2, new DwarfNonPlayerAlertGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new DwarfAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new DwarfRevengeGoal(this));
        this.goalSelector.addGoal(3, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(4, new DwarfUseItemGoal<>(this, PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), SoundEvents.PLAYER_BURP, mob -> mob.getHealth() < mob.getMaxHealth(), 300));
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
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_MERCHANT.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_HISTORIAN.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_SCRAPPER.get(), 1, 1, 0, 0.05F)

                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_BREWMASTER.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_GUARD.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_KEEPER.get(), 1, 1, 0, 0.05F)
                    },

                    //Journeyman
                    3,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ALCHEMIST.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_EXPLORER.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_MINER.get(), 1, 1, 0, 0.05F)
                    },

                    //Expert
                    4,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ARCANIST.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_ARTISAN.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_PRIEST.get(), 1, 1, 0, 0.05F)
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_BLACKSMITH.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_CHAMPION.get(), 1, 1, 0, 0.05F),
                            new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.CONTRACT_SIGNED.get(), 1, 30, JolCraftItems.CONTRACT_SMELTER.get(), 1, 1, 0, 0.05F)
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
            this.addOffersFromItemListings(this.getOffers(), listings, 3); // 2 = max trades for that level
        }
    }


}

