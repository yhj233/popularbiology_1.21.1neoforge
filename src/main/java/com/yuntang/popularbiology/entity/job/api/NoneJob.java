package com.yuntang.popularbiology.entity.job.api;

import com.yuntang.popularbiology.entity.BasePet;
import java.util.function.Consumer;
import net.minecraft.world.entity.ai.Brain;

public class NoneJob implements IPetJob {
   private final Consumer<Brain<BasePet>> brainInitializer;
   private final Consumer<Brain<BasePet>> brainTicker;

   public NoneJob(Consumer<Brain<BasePet>> brainInitializer, Consumer<Brain<BasePet>> brainTicker) {
      this.brainInitializer = brainInitializer;
      this.brainTicker = brainTicker;
   }

   public boolean canAssume(BasePet entity) {
      return true;
   }

   public void initBrain(Brain<BasePet> brain, BasePet pet) {
      this.brainInitializer.accept(brain);
   }

   public void tickBrain(Brain<BasePet> brain, BasePet pet) {
      this.brainTicker.accept(brain);
   }

   public int getPriority() {
      return Integer.MIN_VALUE;
   }
}
