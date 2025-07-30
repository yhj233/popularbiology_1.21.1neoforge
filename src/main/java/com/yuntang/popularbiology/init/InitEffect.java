package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.effect.WulayahaEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public class InitEffect {
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "popularbiology");
   
   public static final DeferredHolder<MobEffect, MobEffect> WULAYAH_EFFECT = EFFECTS.register(
       "wulayaha", 
       WulayahaEffect::new
   );

   public static void register(IEventBus eventBus) {
      EFFECTS.register(eventBus);
   }
}