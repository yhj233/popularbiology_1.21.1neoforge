package com.yuntang.popularbiology.entity.ai.mannager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.ai.task.farmer.DeliverCropTask;
import com.yuntang.popularbiology.entity.ai.task.farmer.HarvestCropTask;
import com.yuntang.popularbiology.entity.ai.task.farmer.PlantCropTask;
import com.yuntang.popularbiology.entity.ai.task.farmer.WalkToContainerTask;
import com.yuntang.popularbiology.entity.ai.task.farmer.WalkToHarvestCropTask;
import com.yuntang.popularbiology.entity.ai.task.farmer.WalkToPlantCropTask;
import com.yuntang.popularbiology.init.InitActivity;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class FarmerJobMannager {
   public static void initBrain(Brain<BasePet> brain, BasePet pet) {
      Utils.AIUtils.addCoreTasks(brain);
      Utils.AIUtils.addIdleTasks(brain);
      addFarmHarvestActivity(brain);
      addFarmPlantActivity(brain);
      addDeliverActivity(brain, pet);
      brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
      brain.setDefaultActivity(Activity.IDLE);
   }

   public static void tickBrain(Brain<BasePet> brain, BasePet pet) {
      Builder<Activity> activities = ImmutableList.builder();
      if (brain.getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent()) {
         activities.add((Activity)InitActivity.FARM_HARVEST.get());
      }

      if (brain.getMemory((MemoryModuleType)InitMemory.PLANT_POS.get()).isPresent()) {
         activities.add((Activity)InitActivity.FARM_PLANT.get());
      }

      if (brain.getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).isPresent()) {
         activities.add((Activity)InitActivity.DELEVER.get());
      }

      activities.add(Activity.IDLE);
      brain.setActiveActivityToFirstValid(activities.build());
   }

   private static void addFarmHarvestActivity(Brain<BasePet> brain) {
      brain.addActivity((Activity)InitActivity.FARM_HARVEST.get(), ImmutableList.of(Pair.of(3, new HarvestCropTask()), Pair.of(4, new WalkToHarvestCropTask(0.8F))));
   }

   private static void addFarmPlantActivity(Brain<BasePet> brain) {
      brain.addActivity((Activity)InitActivity.FARM_PLANT.get(), ImmutableList.of(Pair.of(3, new PlantCropTask()), Pair.of(4, new WalkToPlantCropTask(0.8F))));
   }

   private static void addDeliverActivity(Brain<BasePet> brain, BasePet pet) {
      brain.addActivity((Activity)InitActivity.DELEVER.get(), ImmutableList.of(Pair.of(3, new DeliverCropTask()), Pair.of(4, new WalkToContainerTask(0.8F))));
   }
}
