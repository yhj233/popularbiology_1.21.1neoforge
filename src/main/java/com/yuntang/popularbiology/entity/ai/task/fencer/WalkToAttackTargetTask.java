package com.yuntang.popularbiology.entity.ai.task.fencer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class WalkToAttackTargetTask extends Behavior<BasePet> {
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public WalkToAttackTargetTask() {
      super(REQUIRED_MEMORIES, 15);
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      return pet.getState() == PetState.WORK && pet.getJobId() == 2 && pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && pet.getAction() == 0;
   }

   protected void start(ServerLevel world, BasePet pet, long gameTime) {
      LivingEntity target = (LivingEntity)pet.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
      BehaviorUtils.setWalkAndLookTargetMemories(pet, target, 0.9F, 1);
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT);
   }
}
