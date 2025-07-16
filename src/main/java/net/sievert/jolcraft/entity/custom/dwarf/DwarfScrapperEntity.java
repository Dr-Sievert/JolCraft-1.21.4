package net.sievert.jolcraft.entity.custom.dwarf;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.ai.goal.*;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

import javax.annotation.Nullable;
import java.util.*;

public class DwarfScrapperEntity extends AbstractDwarfEntity {

    public DwarfScrapperEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(JolCraftItems.COPPER_SPANNER.get()));
        this.instanceTrades = MAIN_TRADES;
    }

    // Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfScrapperEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.TEMPT_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public boolean canTrade() {
        return true;
    }

    @Override
    public boolean hasRandomTrades(){ return true; }


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

    // --- Trade tables ---
    // Only static main trades (one per level)
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> MAIN_TRADES = AbstractDwarfEntity.toIntMap(ImmutableMap.of(
            1, new VillagerTrades.ItemListing[] { new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.COPPER_SPANNER.get(), 8, 15, 1, 3, 10) },
            2, new VillagerTrades.ItemListing[] { new JolCraftDwarfTrades.GoldForItems(JolCraftItems.SCRAP.get(), 1, 256, 5, 1) },
            3, new VillagerTrades.ItemListing[] { new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.IRON_SPANNER.get(), 24, 32, 1, 3, 40) },
            4, new VillagerTrades.ItemListing[] { new JolCraftDwarfTrades.GoldForItems(JolCraftItems.SCRAP_HEAP.get(), 1, 64, 50, 4, 7) },
            5, new VillagerTrades.ItemListing[] { new JolCraftDwarfTrades.ItemsAndGoldToItems(JolCraftItems.SCRAP_HEAP.get(), 1, 15, JolCraftItems.RUSTAGATE.get(), 1, 1, 0, 0.05F) }
    ));

    // Salvage pool (full pool for randomization)
    public static final VillagerTrades.ItemListing[] SALVAGE_POOL = new VillagerTrades.ItemListing[] {
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.EXPIRED_POTION.get(), 1, 5, 3, 1, 3),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.OLD_FABRIC.get(), 1, 5, 3, 1, 3),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_PICKAXE.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_AMULET.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_BELT.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_COINS.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.RUSTY_TONGS.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.INGOT_MOULD.get(), 1, 5, 3, 1, 4),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.DEEPSLATE_MUG.get(), 1, 5, 3, 3, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_TABLET.get(), 1, 5, 3, 3, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_DEEPSLATE_PICKAXE_HEAD.get(), 1, 5, 3, 3, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_DEEPSLATE_GEAR.get(), 1, 5, 3, 3, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_DEEPSLATE_PLATES.get(), 1, 5, 3, 3, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_DEEPSLATE_PICKAXE_HEAD.get(), 1, 5, 3, 1, 5),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.MITHRIL_SALVAGE.get(), 1, 5, 3, 5, 10),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_MITHRIL_PLATE.get(), 1, 5, 3, 5, 10),
            new JolCraftDwarfTrades.GoldForItems(JolCraftItems.BROKEN_MITHRIL_SWORD.get(), 1, 5, 3, 5, 10)

    };

    @Override
    protected void updateTrades() {
        super.updateTrades();
        fillRandomSalvageOffers(getVillagerData().getLevel());
    }


    private void fillRandomSalvageOffers(int level) {
        // Remove all previous salvage offers
        this.getOffers().removeIf(this::isSalvageOffer);

        int quota = Math.min(level * 2, SALVAGE_POOL.length);

        List<VillagerTrades.ItemListing> pool = new ArrayList<>(List.of(SALVAGE_POOL));
        Collections.shuffle(pool, new Random(this.random.nextLong()));

        int added = 0;
        for (VillagerTrades.ItemListing salvage : pool) {
            if (added >= quota) break;
            MerchantOffer offer = salvage.getOffer(this, this.random);
            if (offer != null) {
                this.getOffers().add(offer);
                added++;
            }
        }
    }




    private boolean isSalvageOffer(MerchantOffer offer) {
        for (VillagerTrades.ItemListing salvage : SALVAGE_POOL) {
            MerchantOffer test = salvage.getOffer(this, this.random);
            if (test != null &&
                    ItemStack.isSameItemSameComponents(offer.getResult(), test.getResult()) &&
                    ItemStack.isSameItemSameComponents(offer.getBaseCostA(), test.getBaseCostA())
                // Optionally: && offer.getCostB().isEmpty() == test.getCostB().isEmpty()
            ) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void restock() {
        if (this.level().isClientSide) return;
        int level = this.getVillagerData().getLevel();

        // Only remove salvage trades
        this.getOffers().removeIf(this::isSalvageOffer);

        fillRandomSalvageOffers(level);

        this.lastRestockGameTime = this.level().getGameTime();
        this.level().playSound(null, this.blockPosition(), getRestockSound(), SoundSource.NEUTRAL, 1.0F, 0.95F);
    }


    @Override
    public void rerollTrades() {
        this.getOffers().clear();
        int currentLevel = this.getVillagerData().getLevel();

        // Add all main trades up to current level
        for (int level = 1; level <= currentLevel; level++) {
            VillagerTrades.ItemListing[] listings = MAIN_TRADES.get(level);
            if (listings != null) {
                for (VillagerTrades.ItemListing listing : listings) {
                    MerchantOffer offer = listing.getOffer(this, this.random);
                    if (offer != null) {
                        this.getOffers().add(offer);
                    }
                }
            }
        }

        // Fill salvage trades for current level only
        fillRandomSalvageOffers(currentLevel);

        this.level().playSound(null, this.blockPosition(), getRerollSound(), SoundSource.NEUTRAL, 1.0F, 1.05F);
    }





    // Sound
    @Override
    public float getVoicePitch() {
        return 1.4F; // lower pitch for scrapper
    }

    @Nullable
    @Override
    protected SoundEvent getRestockSound() {
        return SoundEvents.VILLAGER_WORK_TOOLSMITH;
    }

    @Nullable
    @Override
    protected SoundEvent getRerollSound() {
        return SoundEvents.VILLAGER_WORK_TOOLSMITH;
    }

}
