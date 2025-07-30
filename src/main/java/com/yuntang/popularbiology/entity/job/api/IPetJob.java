package com.yuntang.popularbiology.entity.job.api;

import com.yuntang.popularbiology.entity.BasePet;
import net.minecraft.world.entity.ai.Brain;

public interface IPetJob {
   boolean canAssume(BasePet var1);

   void initBrain(Brain<BasePet> var1, BasePet var2);

   int getPriority();

   void tickBrain(Brain<BasePet> var1, BasePet var2);
}
