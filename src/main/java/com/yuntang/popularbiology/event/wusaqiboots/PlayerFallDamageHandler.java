package com.yuntang.popularbiology.event.wusaqiboots;

import com.yuntang.popularbiology.item.WusaqiArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
@EventBusSubscriber(modid = "popularbiology")
public class PlayerFallDamageHandler {
   @SubscribeEvent
   public static void onPlayerFall(LivingFallEvent event) {
      LivingEntity var2 = event.getEntity();
      if (var2 instanceof Player) {
         Player player = (Player)var2;
         if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WusaqiArmorItem) {
            event.setCanceled(true);
         }
      }

   }
}
