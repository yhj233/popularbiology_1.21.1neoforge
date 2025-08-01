package com.yuntang.popularbiology.entity.ai.task.tameable;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.slf4j.Logger;

public class SitTask<E extends BasePet> extends Behavior<E> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES = ImmutableMap.of();

   public SitTask() {
      super(REQUIRED_MEMORIES, 100);
   }

   protected boolean checkExtraStartConditions(ServerLevel level, BasePet entity) {
      return entity.getState() == PetState.SIT;
   }

   protected boolean canStillUse(ServerLevel level, BasePet entity, long gameTime) {
      return entity.getState() == PetState.SIT;
   }

   protected void start(ServerLevel level, BasePet entity, long gameTime) {
      entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
      entity.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
   }
}
