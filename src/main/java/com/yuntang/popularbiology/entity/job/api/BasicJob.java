package com.yuntang.popularbiology.entity.job.api;

import com.yuntang.popularbiology.entity.BasePet;
import java.util.function.BiConsumer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.Item;

public class BasicJob implements IPetJob {
   private final TagKey<Item> items;
   private final BiConsumer<Brain<BasePet>, BasePet> brainInitializer;
   private final BiConsumer<Brain<BasePet>, BasePet> brainTicker;

   public BasicJob(TagKey<Item> items, BiConsumer<Brain<BasePet>, BasePet> brainInitializer, BiConsumer<Brain<BasePet>, BasePet> brainTicker) {
      this.items = items;
      this.brainInitializer = brainInitializer;
      this.brainTicker = brainTicker;
   }

   public boolean canAssume(BasePet entity) {
      return entity.getInventory().getItem(0).is(this.items);
   }

   public void tickBrain(Brain<BasePet> brain, BasePet pet) {
      this.brainTicker.accept(brain, pet);
   }

   public void initBrain(Brain<BasePet> brain, BasePet pet) {
      this.brainInitializer.accept(brain, pet);
   }

   public int getPriority() {
      return 0;
   }
}
