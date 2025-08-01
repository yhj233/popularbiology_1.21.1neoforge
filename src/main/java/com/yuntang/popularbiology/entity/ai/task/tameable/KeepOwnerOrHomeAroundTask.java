package com.yuntang.popularbiology.entity.ai.task.tameable;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepOwnerOrHomeAroundTask extends Behavior<BasePet> {
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES = ImmutableMap.of();
   private static final Logger LOGGER = LogManager.getLogger();
   private final float followMasterDistance;
   private final float keepHomeAroundDistance;
   private final float teleportDistance;

   public KeepOwnerOrHomeAroundTask(float followMasterDistance, float keepHomeAroundDistance, float teleportDistance) {
      super(REQUIRED_MEMORIES, 15);
      this.followMasterDistance = followMasterDistance;
      this.keepHomeAroundDistance = keepHomeAroundDistance;
      this.teleportDistance = teleportDistance;
   }

   public float getFollowMasterDistance() {
      return this.followMasterDistance;
   }

   public float getKeepHomeAroundDistance() {
      return this.keepHomeAroundDistance;
   }

   public float getTeleportDistance() {
      return this.teleportDistance;
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      if (!pet.isLeashed() && !pet.isPassenger()) {
         Optional<LivingEntity> master = Optional.ofNullable(pet.getOwner());
         Optional<GlobalPos> home = pet.getBrain().getMemory(MemoryModuleType.HOME);
         if (master.filter((m) -> {
            return !m.isSpectator() && pet.distanceTo(m) >= this.followMasterDistance && pet.getState() == PetState.FOLLOW;
         }).isPresent()) {
            return true;
         } else {
            return home.isPresent() && (float)((GlobalPos)home.get()).pos().distManhattan(pet.getOnPos()) > this.keepHomeAroundDistance && pet.getState() == PetState.WORK;
         }
      } else {
         return false;
      }
   }

   protected void start(ServerLevel world, BasePet pet, long time) {
      Optional<LivingEntity> master = Optional.ofNullable(pet.getOwner());
      if (pet.getState() == PetState.FOLLOW) {
         master.ifPresent((masterValue) -> {
            double distanceToMaster = (double)pet.distanceTo(masterValue);
            if (distanceToMaster > (double)this.teleportDistance) {
               this.TeleprotToMaster(pet);
            } else {
               BehaviorUtils.setWalkAndLookTargetMemories(pet, masterValue, 1.0F, 2);
            }

         });
      } else if (pet.getState() == PetState.WORK) {
         Optional<GlobalPos> homeOptional = pet.getBrain().getMemory(MemoryModuleType.HOME);
         if (homeOptional.isPresent()) {
            GlobalPos home = (GlobalPos)homeOptional.get();
            BlockPos homePos = home.pos();
            pet.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(homePos, 0.6F, 0));
         }
      }

   }

   private void TeleprotToMaster(BasePet pet) {
      LivingEntity master = pet.getOwner();
      if (master != null) {
         double ownerX = master.getX();
         double ownerY = master.getY();
         double ownerZ = master.getZ();

         for(int i = 0; i < 50; ++i) {
            double randomX = ownerX + (Math.random() * 6.0D - 3.0D);
            double randomY = ownerY + (Math.random() * 2.0D - 1.0D);
            double randomZ = ownerZ + (Math.random() * 6.0D - 3.0D);
            BlockPos randomPos = new BlockPos((int)randomX, (int)randomY, (int)randomZ);
            if (Utils.isSafePosition(pet, randomPos, (ServerLevel)pet.level())) {
               Level var18 = pet.level();
               if (var18 instanceof ServerLevel) {
                  ServerLevel serverLevel = (ServerLevel)var18;
                  BlockPos sourcePos = BlockPos.containing(pet.position());
                  serverLevel.playSound((Player)null, (double)sourcePos.getX(), (double)sourcePos.getY(), (double)sourcePos.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 0.5F, 1.0F);
                  serverLevel.playSound((Player)null, (double)randomPos.getX(), (double)randomPos.getY(), (double)randomPos.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 0.5F, 1.0F);
               }

               pet.teleportTo((double)randomPos.getX(), (double)randomPos.getY(), (double)randomPos.getZ());
               break;
            }
         }
      }

   }
}
