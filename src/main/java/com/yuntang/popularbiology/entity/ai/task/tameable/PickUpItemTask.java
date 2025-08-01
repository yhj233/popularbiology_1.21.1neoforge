package com.yuntang.popularbiology.entity.ai.task.tameable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import java.util.List;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class PickUpItemTask extends Behavior<BasePet> {
   private final float speedModifier;
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public PickUpItemTask(float speedModifier) {
      super(REQUIRED_MEMORIES, 10);
      this.speedModifier = speedModifier;
   }

   protected boolean checkExtraStartConditions(ServerLevel level, BasePet entity) {
      return entity.isTame() && entity.getState() == PetState.WORK;
   }

   protected void start(ServerLevel level, BasePet entity, long time) {
      List<Entity> pickableItems = (List)entity.getBrain().getMemory((MemoryModuleType)InitMemory.PICKABLE_ITEM.get()).orElse(Lists.newArrayList());
      if (!pickableItems.isEmpty()) {
         BehaviorUtils.setWalkAndLookTargetMemories(entity, (Entity)pickableItems.get(0), this.speedModifier, 0);
      } else {
         entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
      }

   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of((MemoryModuleType)InitMemory.PICKABLE_ITEM.get(), MemoryStatus.VALUE_PRESENT);
   }
}
