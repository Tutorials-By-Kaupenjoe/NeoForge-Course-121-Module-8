package net.kaupenjoe.mccourse.entity.custom;

import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.ModEntities;
import net.kaupenjoe.mccourse.item.ModItems;
import net.kaupenjoe.mccourse.item.custom.WarturtleArmorItem;
import net.kaupenjoe.mccourse.screen.custom.WarturtleMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

public class WarturtleEntity extends TamableAnimal implements ContainerListener, HasCustomInventoryScreen {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState sitDownAnimationState = new AnimationState();
    public final AnimationState sitPoseAnimationState = new AnimationState();
    public final AnimationState sitUpAnimationState = new AnimationState();

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK =
            SynchedEntityData.defineId(WarturtleEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Boolean> HAS_TIER_1_CHEST =
            SynchedEntityData.defineId(WarturtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_TIER_2_CHEST =
            SynchedEntityData.defineId(WarturtleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_TIER_3_CHEST =
            SynchedEntityData.defineId(WarturtleEntity.class, EntityDataSerializers.BOOLEAN);

    protected SimpleContainer inventory;

    private final int TIER_1_CHEST_SLOT = 2;
    private final int TIER_2_CHEST_SLOT = 3;
    private final int TIER_3_CHEST_SLOT = 4;

    public WarturtleEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.createInventory();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(ModItems.TOMATO.get()), true));

        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.25d, 18f, 7f));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1d));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 4f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 35D)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 2f)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.TOMATO.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.WARTURTLE.get().create(level);
    }

    /* ANIMATIONS */
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isVisuallySitting()) {
            this.sitUpAnimationState.stop();
            if (this.isVisuallySittingDown()) {
                this.sitDownAnimationState.startIfStopped(this.tickCount);
                this.sitPoseAnimationState.stop();
            } else {
                this.sitDownAnimationState.stop();
                this.sitPoseAnimationState.startIfStopped(this.tickCount);
            }
        } else {
            this.sitDownAnimationState.stop();
            this.sitPoseAnimationState.stop();
            this.sitUpAnimationState.animateWhen(this.isInPoseTransition() && this.getPoseTime() >= 0L, this.tickCount);
        }
    }

    public boolean isInPoseTransition() {
        long i = this.getPoseTime();
        return i < (long) (this.isSitting() ? 40 : 52);
    }

    public boolean isVisuallySitting() {
        return this.getPoseTime() < 0L != this.isSitting();
    }

    private boolean isVisuallySittingDown() {
        return this.isSitting() && this.getPoseTime() < 40L && this.getPoseTime() >= 0L;
    }

    public void resetLastPoseChangeTick(long pLastPoseChangeTick) {
        this.entityData.set(LAST_POSE_CHANGE_TICK, pLastPoseChangeTick);
    }

    public long getPoseTime() {
        return this.level().getGameTime() - Math.abs(this.entityData.get(LAST_POSE_CHANGE_TICK));
    }

    private void resetLastPoseChangeTickToFullStand(long pLastPoseChangedTick) {
        this.resetLastPoseChangeTick(Math.max(0L, pLastPoseChangedTick - 52L - 1L));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    /* RIGHT CLICKING */
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.APPLE;

        if(item == itemForTaming && !isTame()) {
            if(this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!EventHooks.onAnimalTame(this, pPlayer)) {
                    super.tame(pPlayer);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte)7);

                    toggleSitting();
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && pHand == InteractionHand.MAIN_HAND && !isFood(itemstack) && !pPlayer.isSecondaryUseActive()) {
            toggleSitting();
            return InteractionResult.SUCCESS;
        } else if (this.isTame()) {
            this.openCustomInventoryScreen(pPlayer);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(pPlayer, pHand);
    }

    /* SITTING */
    public boolean isSitting() {
        return this.entityData.get(LAST_POSE_CHANGE_TICK) < 0L;
    }

    public void toggleSitting() {
        if (this.isSitting()) {
            standUp();
        } else {
            sitDown();
        }
    }

    public void sitDown() {
        if (!this.isSitting()) {
            this.makeSound(SoundEvents.CAMEL_SIT);
            this.setPose(Pose.SITTING);
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(-this.level().getGameTime());
        }

        setOrderedToSit(true);
        setInSittingPose(true);
    }

    public void standUp() {
        if (this.isSitting()) {
            this.makeSound(SoundEvents.CAMEL_STAND);
            this.setPose(Pose.STANDING);
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(this.level().getGameTime());
        }

        setOrderedToSit(false);
        setInSittingPose(false);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(LAST_POSE_CHANGE_TICK, 0L);

        pBuilder.define(HAS_TIER_1_CHEST, false);
        pBuilder.define(HAS_TIER_2_CHEST, false);
        pBuilder.define(HAS_TIER_3_CHEST, false);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putLong("LastPoseTick", this.entityData.get(LAST_POSE_CHANGE_TICK));

        ListTag listtag = new ListTag();
        for (int x = 0; x < this.inventory.getContainerSize(); x++) {
            ItemStack itemstack = this.inventory.getItem(x);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)(x));
                listtag.add(itemstack.save(this.registryAccess(), compoundtag));
            }
        }
        pCompound.put("Items", listtag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        long i = pCompound.getLong("LastPoseTick");
        if (i < 0L) {
            this.setPose(Pose.SITTING);
        }
        this.resetLastPoseChangeTick(i);

        this.createInventory();
        ListTag listtag = pCompound.getList("Items", 10);

        for (int x = 0; x < listtag.size(); x++) {
            CompoundTag compoundtag = listtag.getCompound(x);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData) {
        this.resetLastPoseChangeTickToFullStand(pLevel.getLevel().getGameTime());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    /* INVENTORY */
    @Override
    public void containerChanged(Container container) {
        if(container.getItem(TIER_1_CHEST_SLOT).is(Items.CHEST) && !hasTier1Chest()) {
            setChest(TIER_1_CHEST_SLOT, true);
        }
        if(container.getItem(TIER_2_CHEST_SLOT).is(Items.CHEST) && !hasTier2Chest()) {
            setChest(TIER_2_CHEST_SLOT, true);
        }
        if(container.getItem(TIER_3_CHEST_SLOT).is(Items.CHEST) && !hasTier3Chest()) {
            setChest(TIER_3_CHEST_SLOT, true);
        }

        if(!container.getItem(TIER_1_CHEST_SLOT).is(Items.CHEST) && hasTier1Chest()) {
            setChest(TIER_1_CHEST_SLOT, false);
            dropChestInventory(TIER_1_CHEST_SLOT);
        }
        if(!container.getItem(TIER_2_CHEST_SLOT).is(Items.CHEST) && hasTier2Chest()) {
            setChest(TIER_2_CHEST_SLOT, false);
            dropChestInventory(TIER_2_CHEST_SLOT);
        }
        if(!container.getItem(TIER_3_CHEST_SLOT).is(Items.CHEST) && hasTier3Chest()) {
            setChest(TIER_3_CHEST_SLOT, false);
            dropChestInventory(TIER_3_CHEST_SLOT);
        }

        if(container.getItem(0).getItem() instanceof WarturtleArmorItem) {
            setBodyArmorItem(container.getItem(0));
        }
        if(container.getItem(0).isEmpty() && isWearingBodyArmor()) {
            setBodyArmorItem(ItemStack.EMPTY);
        }
    }

    private void dropChestInventory(int slot) {
        if(slot == TIER_1_CHEST_SLOT) {
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(5, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(6, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(7, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(8, 64));
        }

        if(slot == TIER_2_CHEST_SLOT) {
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(9, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(10, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(11, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(12, 64));
        }

        if(slot == TIER_3_CHEST_SLOT) {
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(13, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(14, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(15, 64));
            Containers.dropItemStack(this.level(), this.getX(), this.getY() + 1, this.getZ(), inventory.removeItem(16, 64));
        }
    }

    public boolean hasTier1Chest() {
        return this.entityData.get(HAS_TIER_1_CHEST);
    }

    public boolean hasTier2Chest() {
        return this.entityData.get(HAS_TIER_2_CHEST);
    }

    public boolean hasTier3Chest() {
        return this.entityData.get(HAS_TIER_3_CHEST);
    }

    public void setChest(int slot, boolean chested) {
        if(slot == TIER_1_CHEST_SLOT) {
            this.entityData.set(HAS_TIER_1_CHEST, chested);
        } else if(slot == TIER_2_CHEST_SLOT) {
            this.entityData.set(HAS_TIER_2_CHEST, chested);
        } else if(slot == TIER_3_CHEST_SLOT) {
            this.entityData.set(HAS_TIER_3_CHEST, chested);
        } else {
            MCCourseMod.LOGGER.error("Can't give chest to a Slot that doesn't exist!");
        }
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (player.containerMenu != player.inventoryMenu) {
                player.closeContainer();
            }

            serverPlayer.openMenu(new SimpleMenuProvider((ix, playerInventory, playerEntityx) ->
                    new WarturtleMenu(ix, playerInventory, this.inventory, this, 4), this.getDisplayName()), buf -> {
                buf.writeUUID(getUUID());
            });
        }
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; j++) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
    }

    public final int getInventorySize() {
        return getInventorySize(4);
    }

    public static int getInventorySize(int columns) {
        return columns * 3 + 5;
    }

    public boolean hasInventoryChanged(Container inventory) {
        return this.inventory != inventory;
    }

    /* ARMOR */
    public boolean hasArmorOn() {
        return isWearingBodyArmor();
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.canArmorAbsorb(damageSource)) {
            super.actuallyHurt(damageSource, damageAmount);
        } else {
            ItemStack itemstack = this.getBodyArmorItem();
            itemstack.hurtAndBreak(Mth.ceil(damageAmount), this, EquipmentSlot.BODY);

            if(itemstack.getItem() instanceof WarturtleArmorItem warturtleArmorItem) {
                int damagereducton = warturtleArmorItem.getDefense() / 2; // depends on what armor
                super.actuallyHurt(damageSource, Math.max(0, damageAmount - damagereducton));
            }
        }
    }

    private boolean canArmorAbsorb(DamageSource damageSource) {
        return this.hasArmorOn() && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }
}