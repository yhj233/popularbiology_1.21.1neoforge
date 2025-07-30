package com.yuntang.popularbiology.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.yuntang.popularbiology.client.aichat.ChatMannager;
import com.yuntang.popularbiology.config.CommonConfig;
import com.yuntang.popularbiology.entity.interact.BaseInteract;
import com.yuntang.popularbiology.entity.job.PetJob;
import com.yuntang.popularbiology.entity.job.api.IPetJob;
import com.yuntang.popularbiology.entity.sound.PetSoundManager;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.init.InitSensor;
import com.yuntang.popularbiology.init.InitTag;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BasePet extends TamableAnimal implements GeoEntity, InventoryCarrier, ContainerListener, RangedAttackMob {
   private static final EntityDataAccessor<String> PET_NAME;
   private static final EntityDataAccessor<CompoundTag> STATE;
   private static final EntityDataAccessor<Integer> JOB_ID;
   private static final EntityDataAccessor<Integer> ACTION;
   private static final EntityDataAccessor<Integer> PET_TEXTURE;
   private static final EntityDataAccessor<Boolean> IS_CHAT_STATE;
   private static final EntityDataAccessor<String> CHATBUBBLE_MESSAGE;
   private static final EntityDataAccessor<Integer> CHATBUBBLE_MESSAGE_TIME;
   private static final EntityDataAccessor<String> TTS_MODEL_ID;
   private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;
   private static final ImmutableList<SensorType<? extends Sensor<? super BasePet>>> SENSORS;
   private final SimpleContainer inventory = new SimpleContainer(16);
   private final ChatMannager chatInteract = new ChatMannager(this);
   private static final Logger LOGGER;
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

   @OnlyIn(Dist.CLIENT)
   public ChatMannager getChatMannager() {
      return this.chatInteract;
   }

   public BasePet(EntityType<? extends TamableAnimal> type, Level level, String name) {
      super(type, level);
      this.setPetName(name);
      this.inventory.addListener(this);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
      super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(Integer)CommonConfig.PET_MAX_HEALTH.get());
      this.setHealth((float)(Integer)CommonConfig.PET_MAX_HEALTH.get());
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((Double)CommonConfig.PET_MOVEMENT_SPEED.get());
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(Integer)CommonConfig.PET_ATTACK_DAMAGE.get());
      return pSpawnData;
   }

   public boolean doHurtTarget(Entity pEntity) {
      if (!this.level().isClientSide) {
         this.playSound(this.getSoundManager().getAttackSound(), 1.0F, 1.0F);
      }

      return super.doHurtTarget(pEntity);
   }

   protected SoundEvent getDeathSound() {
      if (!this.level().isClientSide) {
         this.playSound(this.getSoundManager().getDeathSound(), 1.0F, 1.0F);
      }

      return super.getDeathSound();
   }

   protected SoundEvent getHurtSound(DamageSource pDamageSource) {
      if (!this.level().isClientSide) {
         this.playSound(this.getSoundManager().getHurtSound(), 1.0F, 1.0F);
      }

      return super.getHurtSound(pDamageSource);
   }

   public abstract PetSoundManager getSoundManager();

   public String getChatbubbleMessage() {
      return (String)this.entityData.get(CHATBUBBLE_MESSAGE);
   }

   public void setChatbubbleMessage(String message) {
      if (!this.level().isClientSide) {
         SoundEvent soundEvent = this.getSoundManager().getRandomSound();
         this.playSound(soundEvent, 1.0F, 1.0F);
         this.entityData.set(CHATBUBBLE_MESSAGE, message);
      }

   }

   public String getTTSModelId() {
      return (String)this.entityData.get(TTS_MODEL_ID);
   }

   public void setTTSModelId(String modelId) {
      if (!this.level().isClientSide) {
         this.entityData.set(TTS_MODEL_ID, modelId);
      }

   }

   public int getChatbubbleMessageTime() {
      return (Integer)this.entityData.get(CHATBUBBLE_MESSAGE_TIME);
   }

   public void setChatbubbleMessageTime(int time) {
      if (!this.level().isClientSide) {
         this.entityData.set(CHATBUBBLE_MESSAGE_TIME, time);
      }

   }

   @Override
   public Brain<BasePet> getBrain() {
      return (Brain<BasePet>) super.getBrain();
   }

   protected Provider<BasePet> brainProvider() {
      return Brain.provider(MEMORY_MODULES, SENSORS);
   }

   protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
      Brain<BasePet> brain = this.brainProvider().makeBrain(dynamicIn);
      this.getJob().initBrain(brain, this);
      return brain;
   }

   public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
      ItemStack arrowStack = Utils.getArrow(this);
      if (!arrowStack.isEmpty()) {
         AbstractArrow abstractarrow = getMobArrow(this, arrowStack, pDistanceFactor);
         double d0 = pTarget.getX() - this.getX();
         double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
         double d2 = pTarget.getZ() - this.getZ();
         double d3 = Math.sqrt(d0 * d0 + d2 * d2);
         abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
         this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
         this.playSound(this.getSoundManager().getAttackSound(), 1.0F, 1.0F);
         this.level().addFreshEntity(abstractarrow);
         Level var14 = this.level();
         if (var14 instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)var14;
            double startX = this.getX();
            double startY = this.getY() + (double)this.getBbHeight() / 2.0D;
            double startZ = this.getZ();
            double endX = pTarget.getX();
            double endY = pTarget.getY() + (double)pTarget.getBbHeight() / 2.0D;
            double endZ = pTarget.getZ();
            int particleCount = 10;

            for(int i = 0; i < particleCount; ++i) {
               double t = (double)i / (double)(particleCount - 1);
               double x = startX + (endX - startX) * t;
               double y = startY + (endY - startY) * t;
               double z = startZ + (endZ - startZ) * t;
               serverLevel.sendParticles(ParticleTypes.CRIT, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.1D);
            }
         }

         arrowStack.shrink(1);
      }
   }

   public static AbstractArrow getMobArrow(LivingEntity pShooter, ItemStack pArrowStack, float pVelocity) {
      ArrowItem arrowitem = (ArrowItem)pArrowStack.getItem();
      AbstractArrow abstractarrow = arrowitem.createArrow(pShooter.level(), pArrowStack, pShooter, pArrowStack);
 //     abstractarrow.setEnchantmentEffectsFromEntity(pShooter, pVelocity);
 //     if (pArrowStack.is(Items.TIPPED_ARROW) && abstractarrow instanceof Arrow) {
 //        ((Arrow)abstractarrow).setEffectsFromItem(pArrowStack);
 //     }

      return abstractarrow;
   }

   public void refreshBrain(ServerLevel serverLevelIn) {
      Brain<BasePet> brain = this.getBrain();
      brain.stopAll(serverLevelIn, this);
      this.brain = brain.copyWithoutBehaviors();
      this.getJob().initBrain(this.getBrain(), this);
   }

   protected void customServerAiStep() {
      this.getJob().tickBrain(this.getBrain(), this);
      this.getBrain().tick((ServerLevel)this.level(), this);
      super.customServerAiStep();
   }

   public void setState(PetState state) {
      if (!this.level().isClientSide) {
         this.entityData.set(STATE, state.serializeNBT());
      }

   }

   public PetState getState() {
      return PetState.deserializeNBT((CompoundTag)this.entityData.get(STATE));
   }

   public int getAction() {
      return (Integer)this.entityData.get(ACTION);
   }

   public void setAction(int action) {
      if (!this.level().isClientSide) {
         this.entityData.set(ACTION, action);
      }

   }

   public boolean getIsChatState() {
      return (Boolean)this.entityData.get(IS_CHAT_STATE);
   }

   public void setChatState(boolean talking) {
      if (!this.level().isClientSide) {
         this.entityData.set(IS_CHAT_STATE, talking);
      }

   }

   public void containerChanged(Container pContainer) {
      if (!this.level().isClientSide) {
         ItemStack stack = pContainer.getItem(0);
         if (!stack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
         } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
         }

         IPetJob job = (IPetJob)PetJob.JOB_MAP.values().stream().filter((j) -> {
            return j.canAssume(this);
         }).max(Comparator.comparingInt(IPetJob::getPriority)).orElse(PetJob.getJobFromId(0));
         if (!job.equals(this.getJob())) {
            this.setJobId(PetJob.getIdFromJob(job));
            this.refreshBrain((ServerLevel)this.level());
         }
      }

   }

   public IPetJob getJob() {
      return PetJob.getJobFromId((Integer)this.entityData.get(JOB_ID));
   }

   public void setJobId(int job) {
      if (!this.level().isClientSide) {
         this.entityData.set(JOB_ID, job);
      }

   }

   public int getJobId() {
      return (Integer)this.entityData.get(JOB_ID);
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      this.getInteractAction().execute(this.level(), this, player, hand);
      super.mobInteract(player, hand);
      return InteractionResult.SUCCESS;
   }

   protected abstract BaseInteract getInteractAction();

   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(PET_NAME, "Unnamed");
      builder.define(STATE, PetState.FOLLOW.serializeNBT());
      builder.define(JOB_ID, PetJob.NONE.getId());
      builder.define(ACTION, 0);
      builder.define(PET_TEXTURE, 0);
      builder.define(IS_CHAT_STATE, false);
      builder.define(CHATBUBBLE_MESSAGE, "");
      builder.define(CHATBUBBLE_MESSAGE_TIME, 0);      
      builder.define(TTS_MODEL_ID, ""); 
   }

   public String getPetName() {
      return (String)this.entityData.get(PET_NAME);
   }

   public void setPetName(String name) {
      if (!this.level().isClientSide) {
         this.entityData.set(PET_NAME, name);
      }

   }

   public void setPetTexture(int texture) {
      if (!this.level().isClientSide) {
         this.entityData.set(PET_TEXTURE, texture);
      }

   }

   public int getNextPetTexture() {
      int nextTexture = (Integer)this.entityData.get(PET_TEXTURE) + 1;
      if (nextTexture > 1) {
         nextTexture = 0;
      }

      return nextTexture;
   }

   public int getPetTexture() {
      return (Integer)this.entityData.get(PET_TEXTURE);
   }

   public SimpleContainer getInventory() {
      return this.inventory;
   }

   public boolean wantsToPickUp(ItemStack stack) {
      return stack.is(InitTag.ENTITY_PICKABLE_ITEMS);
   }

   protected void pushEntities() {
      super.pushEntities();
      if (this.isTame()) {
         List<Entity> entityList = this.level().getEntities(this, this.getBoundingBox().inflate(0.5D, 0.0D, 0.5D), this::canPickup);
         if (!entityList.isEmpty() && this.isAlive()) {
            Iterator var2 = entityList.iterator();

            while(var2.hasNext()) {
               Entity entityPickup = (Entity)var2.next();
               if (entityPickup instanceof ItemEntity) {
                  this.pickupItem((ItemEntity)entityPickup);
               }
            }
         }
      }

   }

   public boolean canPickup(Entity entity) {
      if (!(entity instanceof ItemEntity)) {
         return false;
      } else {
         ItemEntity itemEntity = (ItemEntity)entity;
         if (itemEntity.isAlive() && !itemEntity.hasPickUpDelay()) {
            ItemStack stack = itemEntity.getItem();
            return !stack.isEmpty() && stack.is(InitTag.ENTITY_PICKABLE_ITEMS);
         } else {
            return false;
         }
      }
   }

   public boolean pickupItem(ItemEntity itemEntity) {
      if (!itemEntity.isRemoved() && this.isAlive()) {
         ItemStack itemStack = itemEntity.getItem();
         if (itemStack.isEmpty()) {
            return false;
         } else {
            if (!this.level().isClientSide) {
               if (itemEntity.hasPickUpDelay()) {
                  return false;
               }

               ItemStack remaining = this.inventory.addItem(itemStack.copy());
               if (remaining.getCount() != itemStack.getCount()) {
                  this.take(itemEntity, itemStack.getCount() - remaining.getCount());
                  this.onItemPickup(itemEntity);
                  if (remaining.isEmpty()) {
                     itemEntity.discard();
                  } else {
                     itemStack.setCount(remaining.getCount());
                  }

                  this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F);
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public void registerControllers(ControllerRegistrar controllers) {
      AnimationController<BasePet> main = new AnimationController(this, "main", 5, (state) -> {
         RawAnimation builder = RawAnimation.begin();
         if (this.getState() == PetState.SIT) {
            builder.thenLoop("sit");
         } else if (this.getAction() == 5) {
            builder.thenPlayAndHold("sword_attack");
         } else if (this.getAction() != 1 && this.getAction() != 2 && this.getAction() != 3 && this.getAction() != 4) {
            if (state.isMoving()) {
               builder.thenLoop("run");
            } else {
               builder.thenLoop("idle");
            }
         } else {
            builder.thenLoop("use_mainhand");
         }

         state.setAndContinue(builder);
         return PlayState.CONTINUE;
      });
      AnimationController<BasePet> sub = new AnimationController(this, "sub", 1, (state) -> {
         return PlayState.STOP;
      });
      controllers.add(new AnimationController[]{main, sub});
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   public BasePet getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
      return null;
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      this.writeInventoryToTag(tag, null);
      tag.put("state", this.getState().serializeNBT());
      tag.putInt("job", this.getJobId());
      this.getBrain().getMemory(MemoryModuleType.HOME).ifPresent((home) -> {
         Utils.putGlobalPos(tag, "home", home);
      });
      tag.putInt("texture", this.getPetTexture());
      tag.putString("tts_model_id", this.getTTSModelId());
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.readInventoryFromTag(tag, null);
      if (tag.contains("state")) {
         this.setState(PetState.deserializeNBT(tag.getCompound("state")));
      }

      if (tag.contains("job")) {
         this.setJobId(tag.getInt("job"));
      }

      if (tag.contains("home")) {
         this.getBrain().setMemory(MemoryModuleType.HOME, Utils.getGlobalPos(tag, "home", (ServerLevel)this.level()));
      }

      if (tag.contains("texture")) {
         this.setPetTexture(tag.getInt("texture"));
      }

      if (tag.contains("tts_model_id")) {
         this.setTTSModelId(tag.getString("tts_model_id"));
      }

   }

   public static Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.ATTACK_KNOCKBACK, 0.0D);
   }

   public void tick() {
      super.tick();
      if (this.getAction() != 0) {
         this.getNavigation().stop();
      }

      if (this.getChatbubbleMessageTime() > 0) {
         this.setChatbubbleMessageTime(this.getChatbubbleMessageTime() - 1);
      }

   }

   static {
      PET_NAME = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.STRING);
      STATE = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.COMPOUND_TAG);
      JOB_ID = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.INT);
      ACTION = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.INT);
      PET_TEXTURE = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.INT);
      IS_CHAT_STATE = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.BOOLEAN);
      CHATBUBBLE_MESSAGE = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.STRING);
      CHATBUBBLE_MESSAGE_TIME = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.INT);
      TTS_MODEL_ID = SynchedEntityData.defineId(BasePet.class, EntityDataSerializers.STRING);
      LOGGER = LogUtils.getLogger();
      MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.WALK_TARGET, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.HOME, new MemoryModuleType[]{(MemoryModuleType)InitMemory.HARVEST_POS.get(), (MemoryModuleType)InitMemory.PLANT_POS.get(), (MemoryModuleType)InitMemory.PICKABLE_ITEM.get(), (MemoryModuleType)InitMemory.CONTAINER_POS.get()});
      SENSORS = ImmutableList.of(SensorType.HURT_BY, SensorType.NEAREST_LIVING_ENTITIES, (SensorType)InitSensor.PET_CROP_SENSOR.get(), (SensorType)InitSensor.PET_FARMLAND_SENSOR.get(), (SensorType)InitSensor.PET_ITEM_ENTITY_SENSOR.get(), (SensorType)InitSensor.PET_CONTAINER_SENSOR.get(), (SensorType)InitSensor.PET_ATTACKBLE_ENTITY_SENSOR.get());
   }
}
