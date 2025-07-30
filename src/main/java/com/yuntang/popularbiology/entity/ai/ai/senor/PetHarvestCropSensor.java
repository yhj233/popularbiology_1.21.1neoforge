package com.yuntang.popularbiology.entity.ai.senor;

import com.google.common.collect.ImmutableSet;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetHarvestCropSensor extends Sensor<BasePet> {
   private static final Logger LOGGER = LoggerFactory.getLogger(PetHarvestCropSensor.class);
   private static final int MAX_RADIUS = 5;
   private static final int VERTICAL_RANGE = 1;

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of((MemoryModuleType)InitMemory.HARVEST_POS.get());
   }

   public PetHarvestCropSensor() {
      super(60);
   }

   protected void doTick(ServerLevel level, BasePet entity) {
      if (entity.getState() == PetState.WORK && entity.getJobId() == 1) {
         Utils.DetectAreaIsFitCondition.spiralBlockSearch(level, entity, 5, 1, (lvl, pos, pet) -> {
            return Utils.canHarvesr(lvl, pos) && Utils.canReach(pet, pos);
         }).ifPresentOrElse((foundPos) -> {
            entity.getBrain().setMemory((MemoryModuleType)InitMemory.HARVEST_POS.get(), foundPos);
            Utils.setHomeMemory(entity, foundPos, level);
         }, () -> {
            entity.getBrain().eraseMemory((MemoryModuleType)InitMemory.HARVEST_POS.get());
         });
      }

   }
}
