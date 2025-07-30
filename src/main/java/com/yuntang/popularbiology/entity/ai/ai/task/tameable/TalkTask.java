package com.yuntang.popularbiology.entity.ai.task.tameable;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.slf4j.Logger;

public class TalkTask extends Behavior<BasePet> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES = ImmutableMap.of();

   public TalkTask() {
      super(REQUIRED_MEMORIES, 1000);
   }

   protected boolean checkExtraStartConditions(ServerLevel level, BasePet pet) {
      return pet.getIsChatState();
   }

   protected void start(ServerLevel level, BasePet pet, long gameTime) {
      if (pet.getState() == PetState.SIT) {
         pet.getLookControl().setLookAt(pet.getOwner(), 30.0F, 30.0F);
      } else {
         BehaviorUtils.setWalkAndLookTargetMemories(pet, pet.getOwner(), 0.8F, 1);
      }

   }

   protected void tick(ServerLevel pLevel, BasePet pet, long pGameTime) {
      if (pet.getState() == PetState.SIT) {
         pet.getLookControl().setLookAt(pet.getOwner(), 30.0F, 30.0F);
      } else {
         BehaviorUtils.setWalkAndLookTargetMemories(pet, pet.getOwner(), 0.8F, 2);
      }

   }

   protected boolean canStillUse(ServerLevel pLevel, BasePet pet, long pGameTime) {
      return pet.getIsChatState();
   }

   protected void stop(ServerLevel pLevel, BasePet pet, long pGameTime) {
      pet.setChatState(false);
   }
}
