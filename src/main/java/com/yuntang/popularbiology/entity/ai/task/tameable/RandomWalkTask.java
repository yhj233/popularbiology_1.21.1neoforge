package com.yuntang.popularbiology.entity.ai.task.tameable;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class RandomWalkTask extends Behavior<BasePet> {
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public RandomWalkTask() {
      super(REQUIRED_MEMORIES, 100);
   }

   protected boolean checkExtraStartConditions(ServerLevel level, BasePet entity) {
      return entity.getState() != PetState.SIT && entity.getAction() == 0;
   }

   protected void start(ServerLevel level, BasePet entity, long gameTime) {
      BlockPos newTarget = this.getRandomTarget(entity);
      BehaviorUtils.setWalkAndLookTargetMemories(entity, newTarget, 0.6F, 0);
   }

   private BlockPos getRandomTarget(BasePet entity) {
      BlockPos currentPosition = entity.blockPosition();

      for(int i = 0; i < 10; ++i) {
         int randomX = (int)((double)currentPosition.getX() + (Math.random() * 8.0D - 4.0D));
         int randomZ = (int)((double)currentPosition.getZ() + (Math.random() * 8.0D - 4.0D));
         BlockPos randomBlockPos = new BlockPos(randomX, currentPosition.getY(), randomZ);
         if (entity.getNavigation().isStableDestination(randomBlockPos)) {
            return randomBlockPos;
         }
      }

      return currentPosition;
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT);
   }
}
