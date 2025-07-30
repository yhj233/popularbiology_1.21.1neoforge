package com.yuntang.popularbiology.entity.ai.task.archer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class HurtRangedAttackTargetTask extends Behavior<BasePet> {
   private int actionTime = 20;
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public HurtRangedAttackTargetTask() {
      super(REQUIRED_MEMORIES, 15);
   }

   protected boolean checkExtraStartConditions(ServerLevel pLevel, BasePet pet) {
      return pet.getState() == PetState.WORK && pet.getJobId() == 3 && pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && ((LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()).isAlive() && ((LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()).distanceTo(pet) <= 10.0F && pet.getAction() == 0 && !pet.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).isPresent() && !Utils.getArrow(pet).isEmpty();
   }

   protected void start(ServerLevel pLevel, BasePet pet, long pGameTime) {
      if (pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
         LivingEntity target = (LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
         pet.setAction(5);
         pet.startUsingItem(InteractionHand.MAIN_HAND);
         pet.getLookControl().setLookAt(target, 30.0F, 30.0F);
      }

   }

   protected void tick(ServerLevel pLevel, BasePet pet, long pGameTime) {
      if (pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
         LivingEntity target = (LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
         pet.getLookControl().setLookAt(target, 30.0F, 30.0F);
      }

      --this.actionTime;
   }

   protected boolean canStillUse(ServerLevel pLevel, BasePet pEntity, long pGameTime) {
      return this.actionTime > 0 && !Utils.getArrow(pEntity).isEmpty();
   }

   protected void stop(ServerLevel pLevel, BasePet pEntity, long pGameTime) {
      pEntity.setAction(0);
      pEntity.performRangedAttack((LivingEntity)pEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 1.0F);
      pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 20L);
      pEntity.stopUsingItem();
      this.actionTime = 20;
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT);
   }
}
