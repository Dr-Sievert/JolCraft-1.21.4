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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.ai.goal.*;
        import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.util.dwarf.JolCraftDwarfTrades;

import javax.annotation.Nullable;

public class DwarfMinerEntity extends AbstractDwarfEntity {

    public DwarfMinerEntity(EntityType<? extends AbstractDwarfEntity> entityType, Level level) {
        super(entityType, level);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(JolCraftItems.DEEPSLATE_PICKAXE.get()));
        this.instanceTrades = createRandomizedMinerTrades();
    }

    //Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return DwarfMinerEntity.createLivingAttributes()
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
    public ItemStack getSignedContractItem() {
        return new ItemStack(JolCraftItems.CONTRACT_MINER.get());
    }

    @Override
    public ResourceLocation getProfessionId() {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_miner");
    }

    @Nullable
    @Override
    protected SoundEvent getRestockSound() {
        return SoundEvents.VILLAGER_WORK_MASON;
    }

    @Nullable
    @Override
    protected SoundEvent getRerollSound() {
        return SoundEvents.VILLAGER_WORK_MASON;
    }

    @Override
    public float getVoicePitch() { return 1.1F; }


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
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> createRandomizedMinerTrades() {
        return AbstractDwarfEntity.toIntMap(ImmutableMap.of(
                // Novice
                1, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(Items.STICK, 1, 4, 2, 8, 6, 500),
                        new JolCraftDwarfTrades.GoldForItems(Items.SMITHING_TABLE, 1, 3, 4, 1)
                },
                // Apprentice
                2, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(Items.BREAD, 1, 3, 1, 5, 10, 10),
                },
                // Journeyman
                3, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(JolCraftItems.CONTRACT_BLANK.get(), 2, 4, 1, 10, 1, 10),
                        new JolCraftDwarfTrades.GoldForItems(JolCraftItems.QUILL_EMPTY.get(), 3, 7, 4, 1)
                },
                // Expert
                4, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(Items.DIAMOND, 1, 1, 10, 10, 10, 10),
                        new JolCraftDwarfTrades.GoldForItems(Items.EMERALD, 1, 10, 10, 1)
                },
                // Master
                5, new VillagerTrades.ItemListing[] {
                        new JolCraftDwarfTrades.ItemsForGold(Items.NETHERITE_BLOCK, 1, 1, 5, 10, 5, 10),
                        new JolCraftDwarfTrades.ItemsAndGoldToItems(Items.PURPLE_DYE, 1, 30, JolCraftItems.GUILD_SIGIL.get(), 1, 1, 0, 0.05F)
                }
        ));
    }



}


