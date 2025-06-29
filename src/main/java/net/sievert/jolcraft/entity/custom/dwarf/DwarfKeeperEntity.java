package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.phys.Vec3;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.data.BountyData;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.BountyGenerator;
import net.sievert.jolcraft.util.BountyReward;
import net.sievert.jolcraft.util.RandomAdapter;
import net.sievert.jolcraft.villager.JolCraftDwarfTrades;

import java.util.List;

public class DwarfKeeperEntity extends AbstractDwarfEntity {

    public DwarfKeeperEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.BARLEY.get()));
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

    //Core
    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_KEEPER.get());
    }

    @Override
    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_keeper");
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
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

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean client = this.level().isClientSide;

        // 🧠 Language check
        InteractionResult langCheck = this.languageCheck(player);
        if (langCheck != InteractionResult.SUCCESS) {
            return langCheck;
        }

        // Reputation check
        InteractionResult repCheck = this.reputationCheck(player, 1);
        if (repCheck != InteractionResult.SUCCESS) {
            return repCheck;
        }


        // Call parent for all other interactions (contracts, trades, etc)
        return super.mobInteract(player, hand);
    }


    //Trades
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = toIntMap(
            ImmutableMap.of(

                    //Novice
                    1,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.BONE_MEAL, 1, 3, 5, 1),
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BARLEY.get(), 5, 10, 1, 1)
                    },

                    //Apprentice
                    2,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.ItemsForGold(Items.BREAD, 1, 1, 5, 10),
                            new JolCraftDwarfTrades.ItemsForGold(Items.TORCHFLOWER_SEEDS, 1, 1, 5, 10),
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
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.ASGARNIAN_SEEDS.get(), 5, 1, 3, 0),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.DUSKHOLD_SEEDS.get(), 5, 1, 3, 0),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.KRANDONIAN_SEEDS.get(), 5, 1, 3, 0),
                            new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.YANILLIAN_SEEDS.get(), 5, 1, 3, 0),
                    },

                    //Master
                    5,
                    new VillagerTrades.ItemListing[]{
                            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.VERDANITE.get(), 30, 1, 0, 1),
                            new JolCraftDwarfTrades.ItemsForGold(Items.EMERALD, 1, 1, 1, 0)
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
            this.addOffersFromItemListings(this.getOffers(), listings, 4); // 2 = max trades for that level
        }
    }

}

