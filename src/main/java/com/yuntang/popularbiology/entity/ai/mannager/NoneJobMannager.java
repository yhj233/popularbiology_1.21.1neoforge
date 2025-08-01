package com.yuntang.popularbiology.entity.ai.mannager;

import com.google.common.collect.ImmutableSet;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.utils.Utils;
import java.util.List;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.schedule.Activity;

public class NoneJobMannager {
   public static void initBrain(Brain<BasePet> brain) {
      Utils.AIUtils.addCoreTasks(brain);
      Utils.AIUtils.addIdleTasks(brain);
      brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
      brain.setDefaultActivity(Activity.IDLE);
   }

   public static void tickBrain(Brain<BasePet> brain) {
      brain.setActiveActivityToFirstValid(List.of(Activity.IDLE));
   }
}
