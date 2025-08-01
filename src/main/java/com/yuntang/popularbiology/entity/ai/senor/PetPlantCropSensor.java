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

public class PetPlantCropSensor extends Sensor<BasePet> {
   private static final Logger LOGGER = LoggerFactory.getLogger(PetPlantCropSensor.class);
   private static final int MAX_RADIUS = 5;
   private static final int VERTICAL_RANGE = 1;

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of((MemoryModuleType)InitMemory.PLANT_POS.get());
   }

   protected void doTick(ServerLevel level, BasePet pet) {
      if (pet.getState() == PetState.WORK && pet.getJobId() == 1 && Utils.getSeed(pet) != null) {
         if (pet.getBrain().hasMemoryValue((MemoryModuleType)InitMemory.HARVEST_POS.get())) {
            return;
         }

         Utils.DetectAreaIsFitCondition.spiralBlockSearch(level, pet, 5, 1, (lvl, pos, entity) -> {
            return Utils.isCanPlantFarmland(lvl, pos) && Utils.getSeed(entity) != null && Utils.canReach(entity, pos);
         }).ifPresentOrElse((foundPos) -> {
            pet.getBrain().setMemory((MemoryModuleType)InitMemory.PLANT_POS.get(), foundPos);
            Utils.setHomeMemory(pet, foundPos, level);
         }, () -> {
            pet.getBrain().eraseMemory((MemoryModuleType)InitMemory.PLANT_POS.get());
         });
      }

   }
}
