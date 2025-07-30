package com.yuntang.popularbiology.init;

import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;

public class InitActivity {
   public static final DeferredRegister<Activity> ACTIVITIES;
   public static final DeferredHolder<Activity, Activity> FARM_HARVEST;
   public static final DeferredHolder<Activity, Activity> FARM_PLANT;
   public static final DeferredHolder<Activity, Activity> DELEVER;

   public static void register(IEventBus eventBus) {
      ACTIVITIES.register(eventBus);
   }

   static {
      ACTIVITIES = DeferredRegister.create(BuiltInRegistries.ACTIVITY, "popularbiology");
      FARM_HARVEST = ACTIVITIES.register("farm_harvest", () -> {
         return new Activity("farm_harvest");
      });
      FARM_PLANT = ACTIVITIES.register("farm_plant", () -> {
         return new Activity("farm_plant");
      });
      DELEVER = ACTIVITIES.register("delever", () -> {
         return new Activity("delever");
      });
   }
}
