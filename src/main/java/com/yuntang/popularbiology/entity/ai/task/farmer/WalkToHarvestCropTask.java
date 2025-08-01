package com.yuntang.popularbiology.entity.ai.task.farmer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class WalkToHarvestCropTask extends Behavior<BasePet> {
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;
   private final float speed;

   public WalkToHarvestCropTask(float speed) {
      super(REQUIRED_MEMORIES, 15);
      this.speed = speed;
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      return pet.getState() == PetState.WORK && pet.getJobId() == 1 && pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent() && pet.getAction() == 0;
   }

   protected void start(ServerLevel world, BasePet pet, long time) {
      BehaviorUtils.setWalkAndLookTargetMemories(pet, (BlockPos)pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).get(), this.speed, 0);
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of((MemoryModuleType)InitMemory.HARVEST_POS.get(), MemoryStatus.VALUE_PRESENT);
   }
}
