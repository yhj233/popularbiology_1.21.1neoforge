package com.yuntang.popularbiology.entity.ai.task.fencer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class HurtAttackTargetTask extends Behavior<BasePet> {
   private int actionTime = 10;
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public HurtAttackTargetTask() {
      super(REQUIRED_MEMORIES, 15);
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      return pet.getState() == PetState.WORK && pet.getJobId() == 2 && pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && ((LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()).isAlive() && ((LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()).distanceTo(pet) <= 2.0F && pet.getAction() == 0 && !pet.getBrain().getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).isPresent();
   }

   protected void start(ServerLevel world, BasePet pet, long gameTime) {
      pet.setAction(4);
      LivingEntity target = (LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
      pet.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
   }

   protected boolean canStillUse(ServerLevel pLevel, BasePet pEntity, long pGameTime) {
      return this.actionTime > 0;
   }

   protected void tick(ServerLevel pLevel, BasePet pOwner, long pGameTime) {
      --this.actionTime;
   }

   protected void stop(ServerLevel world, BasePet pet, long pGameTime) {
      pet.setAction(0);
      if (pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
         pet.doHurtTarget((Entity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get());
         pet.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 0L);
      }

      pet.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
      this.actionTime = 10;
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT);
   }
}
