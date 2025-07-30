package com.yuntang.popularbiology.entity.ai.mannager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.ai.task.archer.HurtRangedAttackTargetTask;
import com.yuntang.popularbiology.utils.Utils;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class ArcherJobMannager {
   public static void initBrain(Brain<BasePet> brain, BasePet pet) {
      Utils.AIUtils.addCoreTasks(brain);
      Utils.AIUtils.addIdleTasks(brain);
      addFencerTasks(brain);
      brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
      brain.setDefaultActivity(Activity.IDLE);
   }

   public static void tickBrain(Brain<BasePet> brain, BasePet pet) {
      Builder<Activity> activities = ImmutableList.builder();
      if (brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && !Utils.getArrow(pet).isEmpty() && !brain.getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).isPresent()) {
         activities.add(Activity.WORK);
      }

      activities.add(Activity.IDLE);
      brain.setActiveActivityToFirstValid(activities.build());
   }

   public static void addFencerTasks(Brain<BasePet> brain) {
      Pair<Integer, BehaviorControl<? super BasePet>> hurtRangedAttackTarget = Pair.of(4, new HurtRangedAttackTargetTask());
      brain.addActivity(Activity.WORK, ImmutableList.of(hurtRangedAttackTarget));
   }
}
