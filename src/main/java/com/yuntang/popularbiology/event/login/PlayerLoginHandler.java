package com.yuntang.popularbiology.event.login;

import com.yuntang.popularbiology.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
//import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;



@EventBusSubscriber(modid = "popularbiology")
public class PlayerLoginHandler {
   @SubscribeEvent
   public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.getEntity().level().isClientSide) {
         Player player = event.getEntity();
         if ((Boolean)ClientConfig.SHOW_WELCOME_MESSAGE.get()) {
            MutableComponent part1 = Component.literal("[PopularBiology2.4]").withStyle(ChatFormatting.GREEN);
            MutableComponent part2 = Component.literal("感谢 TouhouFish,黎尤香,半缘修道,Limit小火柴,ChaiLan,scaer 对本模组的支持").withStyle(ChatFormatting.AQUA);
            MutableComponent part3 = Component.literal("PopularBiology 完全免费，如有倒卖盈利情况请立即向作者云糖举报").withStyle(ChatFormatting.AQUA);
            player.sendSystemMessage(part1);
            player.sendSystemMessage(part2);
            player.sendSystemMessage(part3);
            ClientConfig.SHOW_WELCOME_MESSAGE.set(false);
         }

      }
   }
}
