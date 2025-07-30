package com.yuntang.popularbiology.event.wulayaha;

import com.yuntang.popularbiology.init.InitEffect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
//import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Finish;

@EventBusSubscriber(modid = "popularbiology")
public class PlayerEatGlowBerriesHandler {
   @SubscribeEvent
   public static void onPlayerFinishEating(Finish event) {
      LivingEntity entity = event.getEntity();
      ItemStack itemStack = event.getItem();
      if (entity instanceof Player) {
         Player player = (Player)entity;
         if (itemStack.getItem() == Items.GLOW_BERRIES) {
               player.addEffect(new MobEffectInstance((Holder<MobEffect>) InitEffect.WULAYAH_EFFECT.get(), 1200, 1));
         }
      }

   }
}
