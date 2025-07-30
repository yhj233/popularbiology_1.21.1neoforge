package com.yuntang.popularbiology.config;

import net.neoforged.neoforge.common.ModConfigSpec;
//import net.neoforged.fml.common.EventBusSubscriber;

//@EventBusSubscriber(modid = "popularbiology")

public class CommonConfig {
   private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
   public static final ModConfigSpec.IntValue PET_MAX_HEALTH;
   public static final ModConfigSpec.IntValue PET_ATTACK_DAMAGE;
   public static final ModConfigSpec.DoubleValue PET_MOVEMENT_SPEED;
   public static final ModConfigSpec SPEC;

   static {
      PET_MAX_HEALTH = BUILDER.comment("宠物的最大生命值").defineInRange("petMaxHealth", 20, 1, 1000);
      PET_ATTACK_DAMAGE = BUILDER.comment("宠物的攻击力").defineInRange("petAttackDamage", 3, 1, 100);
      PET_MOVEMENT_SPEED = BUILDER.comment("宠物的移动速度").defineInRange("petMovementSpeed", 0.3D, 0.0D, 10.0D);
      SPEC = BUILDER.build();
   }
}
