package com.yuntang.popularbiology.entity.ai.task.farmer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PlantCropTask extends Behavior<BasePet> {
   private static final Logger LOGGER = Logger.getLogger(PlantCropTask.class.getName());
   private int actionTime = 100;
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public PlantCropTask() {
      super(REQUIRED_MEMORIES, 20);
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      if (pet.getState() == PetState.WORK && pet.getJobId() == 1 && !pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent() && pet.getBrain().getMemory((MemoryModuleType)InitMemory.PLANT_POS.get()).isPresent() && pet.getAction() == 0) {
         BlockPos farmlandPos = (BlockPos)pet.getBrain().getMemory((MemoryModuleType)InitMemory.PLANT_POS.get()).get();
         if (pet.distanceToSqr((double)farmlandPos.getX() + 0.5D, (double)farmlandPos.getY() + 0.5D, (double)farmlandPos.getZ() + 0.5D) <= 2.0D && Utils.isCanPlantFarmland(world, farmlandPos)) {
            return true;
         }
      }

      return false;
   }

   protected void tick(ServerLevel pLevel, BasePet pOwner, long pGameTime) {
      LOGGER.info("ActionTime: " + this.actionTime);
      --this.actionTime;
   }

   protected boolean canStillUse(ServerLevel pLevel, BasePet pEntity, long pGameTime) {
      return this.actionTime > 0;
   }

   protected void start(ServerLevel world, BasePet pet, long time) {
      pet.setAction(2);
      Brain<BasePet> brain = pet.getBrain();
      Optional<BlockPos> farmlandPosOpt = brain.getMemory((MemoryModuleType)InitMemory.PLANT_POS.get());
      if (!farmlandPosOpt.isEmpty()) {
         BlockPos farmlandPos = (BlockPos)farmlandPosOpt.get();
         brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(farmlandPos));
      }
   }

   protected void stop(ServerLevel world, BasePet pet, long pGameTime) {
      Brain<BasePet> brain = pet.getBrain();
      Optional<BlockPos> farmlandPosOpt = brain.getMemory((MemoryModuleType)InitMemory.PLANT_POS.get());
      if (!farmlandPosOpt.isEmpty()) {
         BlockPos farmlandPos = (BlockPos)farmlandPosOpt.get();
         ItemStack seed = Utils.getSeed(pet);
         if (Utils.isCanPlantFarmland(world, farmlandPos)) {
            BlockState cropBlock = ((BlockItem)seed.getItem()).getBlock().defaultBlockState();
            world.setBlock(farmlandPos.above(), cropBlock, 2);
            seed.shrink(1);
         }

         pet.getBrain().eraseMemory((MemoryModuleType)InitMemory.PLANT_POS.get());
         pet.setAction(0);
         this.actionTime = 100;
      }
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of((MemoryModuleType)InitMemory.PLANT_POS.get(), MemoryStatus.VALUE_PRESENT);
   }
}
