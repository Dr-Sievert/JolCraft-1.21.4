package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
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
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

public class DwarfEntity extends AbstractDwarfEntity {

    public DwarfEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.instanceTrades = createRandomizedDwarfTrades();
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30d)
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
    public boolean neverEndorse(Player player) {
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

    //Trades
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> createRandomizedDwarfTrades() {
        return AbstractDwarfEntity.toIntMap(ImmutableMap.of(
                // Novice
                1, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(Items.STICK, 1, 4, 2, 8, 6, 500),
                        new JolCraftDwarfTrades.ItemForItem(Items.OAK_PLANKS, 2, Items.STICK,  4, 3, 1),
                        new JolCraftDwarfTrades.BountyItemForItem(JolCraftItems.PARCHMENT.get(), 1, JolCraftItems.BOUNTY.get(), 1, 1, 0, 1),

                },
                // Apprentice
                2, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.TreasureMapForGold(
                                8, // min cost
                                JolCraftTags.Structures.ON_FORGE_EXPLORER_MAPS,
                                "filled_map.forge",
                                MapDecorationTypes.TARGET_X,
                                1, 10
                        )
                },
                // Journeyman
                3, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.GoldForItems(Items.SMITHING_TABLE, 1, 3, 4, 1)
                },
                // Expert
                4, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsAndGoldToItemsWithData(
                                JolCraftItems.LEGENDARY_PAGE.get(), 20,
                                30,
                                JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get(), 1,
                                1, 0, 0F,
                                (stack) -> stack.set(JolCraftDataComponents.LORE_LINE_ID, "ancient_gemcraft")
                        ),
                },
                // Master
                5, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsAndGoldToItems(Items.PURPLE_DYE, 1, 30, JolCraftItems.GUILD_SIGIL.get(), 1, 1, 0, 0.05F)
                }
        ));
    }



}
